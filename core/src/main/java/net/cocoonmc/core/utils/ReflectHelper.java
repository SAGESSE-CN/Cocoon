package net.cocoonmc.core.utils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class ReflectHelper {

    private static final HashMap<Class<?>, HashMap<String, UnsafeMember>> MEMBER_FIELDS = new HashMap<>();

    @Nullable
    public static <T> T getMember(Object owner, String name) {
        // noinspection unchecked
        return (T) getMemberUnsafeField(owner.getClass(), name).get(owner);
    }

    public static <T> Member<T> getMemberField(Class<?> clazz, String name) {
        return new Member<>(getMemberUnsafeField(clazz, name));
    }

    public static UnsafeMember getMemberUnsafeField(Class<?> clazz, String name) {
        return MEMBER_FIELDS.computeIfAbsent(clazz, it -> new HashMap<>()).computeIfAbsent(name, it -> new UnsafeMember(clazz, it));
    }

    public static class Member<T> {

        private final UnsafeMember member;

        private Member(UnsafeMember member) {
            this.member = member;
        }

        public void set(Object owner, T value) {
            member.set(owner, value);
        }

        public T get(Object owner) {
            // noinspection unchecked
            return (T) member.get(owner);
        }

    }

    public static class UnsafeMember {

        private final Class<?> clazz;
        private final String name;

        private Field field;
        private Exception fieldException;
        private boolean forceAccess = true;

        public UnsafeMember(Class<?> clazz, String name) {
            this.clazz = clazz;
            this.name = name;
        }

        public Object get(Object owner) {
            try {
                Field field = getField();
                if (field != null) {
                    return field.get(owner);
                }
            } catch (Exception e) {
                // ignore read exception
            }
            return null;
        }

        public void set(Object owner, Object value) {
            try {
                Field field = getField();
                if (field != null) {
                    field.set(owner, value);
                }
            } catch (Exception e) {
                // ignore write exception
            }
        }

        private Field getField() {
            if (field != null) {
                return field;
            }
            if (fieldException != null) {
                return null;
            }
            try {
                field = lookupField(clazz, name);
                field.setAccessible(true);
            } catch (Exception e) {
                // don't try again
                fieldException = e;
            }
            return field;
        }

        private Field lookupField(Class<?> instanceClass, String fieldName) throws NoSuchFieldException {
            for (Field field : instanceClass.getDeclaredFields()) {
                if ((forceAccess || Modifier.isPublic(field.getModifiers())) && field.getName().equals(fieldName)) {
                    return field;
                }
            }

            // Recursively find the correct field
            if (instanceClass.getSuperclass() != null) {
                return lookupField(instanceClass.getSuperclass(), fieldName);
            }

            throw new NoSuchFieldException(fieldName);
        }
    }
}

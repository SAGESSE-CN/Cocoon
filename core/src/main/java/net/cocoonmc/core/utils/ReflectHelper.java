package net.cocoonmc.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class ReflectHelper {

    private static final HashMap<Class<?>, HashMap<String, MemberField>> MEMBER_FIELDS = new HashMap<>();

    public static MemberField getMemberField(Class<?> clazz, String name) {
        return MEMBER_FIELDS.computeIfAbsent(clazz, it -> new HashMap<>()).computeIfAbsent(name, it -> new MemberField(clazz, it));
    }

    public static class MemberField {

        private final Class<?> clazz;
        private final String name;

        private Field field;
        private Exception fieldException;
        private boolean forceAccess = true;

        public MemberField(Class<?> clazz, String name) {
            this.clazz = clazz;
            this.name = name;
        }

        @SuppressWarnings("unchecked")
        public <T> T get(Object owner) {
            try {
                Field field = getField();
                if (field != null) {
                    return (T) field.get(owner);
                }
            } catch (Exception e) {
                // ignore read exception
            }
            return null;
        }

        public <T> void set(Object owner, T value) {
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

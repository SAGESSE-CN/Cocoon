package net.cocoonmc.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class ReflectUtils {

    private static final HashMap<Class<?>, HashMap<String, MemberField>> MEMBER_FIELDS = new HashMap<>();

    public static MemberField getMemberField(Class<?> clazz, String name) {
        return MEMBER_FIELDS.computeIfAbsent(clazz, it -> new HashMap<>()).computeIfAbsent(name, it -> new MemberField(clazz, it));
    }

    public static class MemberField {

        private final Class<?> clazz;
        private final String name;

        private Field field;
        private Exception exception;
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
                // .
            }
        }


        private Field getField() {
            try {
                if (field == null && exception == null) {
                    field = findField(name);
                    field.setAccessible(true);
                }
                return field;
            } catch (Exception e) {
                // don't try again
                exception = e;
                return field;
            }
        }

        public Field findField(String fieldName) {
            return lookupField(clazz, fieldName);
        }

        // For recursion
        private Field lookupField(Class<?> instanceClass, String fieldName) {
            for (Field field : instanceClass.getDeclaredFields()) {
                if ((forceAccess || Modifier.isPublic(field.getModifiers())) && field.getName().equals(fieldName)) {
                    return field;
                }
            }

            // Recursively find the correct field
            if (instanceClass.getSuperclass() != null) {
                return this.lookupField(instanceClass.getSuperclass(), fieldName);
            }

            return null;
        }
    }
}

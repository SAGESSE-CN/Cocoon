package net.cocoonmc.runtime.client.helper;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ReflectHelper {

    @Nullable
    public static Class<?> getClass(String name) {
        try {
            return ReflectHelper.class.getClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

//    @Nullable
//    public static Object getStatic(String className, String fieldName) {
//        try {
//            Class<?> clazz = getClass(className);
//            if (clazz == null) {
//                return null;
//            }
//            Field field = clazz.getField(fieldName);
//            return field.get(clazz);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    @Nullable
//    public static <T, V> Function<T, V> getMethod(Object obj, String methodName, Class<T> argumentType, Class<V> returnType) {
//        try {
//            if (obj == null) {
//                return null;
//            }
//            Class<?> clazz = obj.getClass();
//            Method method = clazz.getMethod(methodName, argumentType);
//            return arg -> {
//                try {
//                    return returnType.cast(method.invoke(obj, arg));
//                } catch (Exception e) {
//                    return null;
//                }
//            };
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static Object call(Object value, String name, Object... args) {
        if (value == null) {
            return null;
        }
        Class<?> clazz;
        if (value instanceof Class<?>) {
            clazz = (Class<?>) value;
        } else {
            clazz = value.getClass();
        }
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < argTypes.length; ++i) {
            argTypes[i] = args[i].getClass();
        }
        try {
            Method method = lookupMethod0(clazz, name, argTypes);
            if (method != null) {
                return method.invoke(value, args);
            }
        } catch (Exception e) {
            // ignored
        }
        return null;
    }

    private static Method lookupMethod0(Class<?> instanceClass, String methodName, Class<?>... argTypes) {
        if (Modifier.isPublic(instanceClass.getModifiers())) {
            for (Method method : instanceClass.getDeclaredMethods()) {
                if (Modifier.isPublic(method.getModifiers()) && method.getName().equals(methodName) && Arrays.equals(method.getParameterTypes(), argTypes)) {
                    return method;
                }
            }
        }
        for (Class<?> interfaceClass : instanceClass.getInterfaces()) {
            Method method = lookupMethod0(interfaceClass, methodName, argTypes);
            if (method != null) {
                return method;
            }
        }
        if (instanceClass.getSuperclass() != null) {
            return lookupMethod0(instanceClass.getSuperclass(), methodName, argTypes);
        }
        return null;
    }


}

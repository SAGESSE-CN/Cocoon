package net.cocoonmc.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ObjectHelper {

    @SafeVarargs
    public static <T> ArrayList<T> map(T... objects) {
        ArrayList<T> results = new ArrayList<>(objects.length);
        Collections.addAll(results, objects);
        return results;
    }

    // "<%s: 0x%x; arg1 = arg2; ...; argN-1 = argN>"
    public static String makeDescription(Object obj, Object... arguments) {
        StringBuilder builder = new StringBuilder();
        builder.append("<");
        builder.append(getClassName(obj.getClass()));
        builder.append(": ");
        builder.append(String.format("0x%x", System.identityHashCode(obj)));
        for (int i = 0; i < arguments.length; i += 2) {
            if (isEmptyOrNull(arguments[i + 1])) {
                continue;
            }
            builder.append("; ");
            builder.append(arguments[i]);
            builder.append(" = ");
            builder.append(arguments[i + 1]);
        }
        builder.append(">");
        return builder.toString();
    }


    private static boolean isEmptyOrNull(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Collection<?>) {
            return ((Collection<?>) value).isEmpty();
        }
        if (value instanceof String) {
            return ((String) value).isEmpty();
        }
        return false;
    }

    private static String getClassName(Class<?> clazz) {
        String name = clazz.getTypeName();
        Package pkg = clazz.getPackage();
        if (pkg != null) {
            return name.replace(pkg.getName() + ".", "");
        }
        return clazz.getSimpleName();
    }
}

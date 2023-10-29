package net.cocoonmc.core.utils;

import net.cocoonmc.runtime.IAssociatedContainer;
import net.cocoonmc.runtime.IAssociatedContainerKey;

import java.util.HashMap;
import java.util.function.Supplier;

public class SimpleAssociatedStorage implements IAssociatedContainer {

    private final HashMap<IAssociatedContainerKey<?>, Supplier<Object>> values = new HashMap<>();

    @Override
    public <T> T getAssociatedObject(IAssociatedContainerKey<T> key) {
        Supplier<Object> value = values.get(key);
        if (value != null) {
            Object value1 = value.get();
            if (value1 != null) {
                return key.getType().cast(value1);
            }
            return null;
        }
        T value2 = key.getDefaultValue();
        values.put(key, () -> value2);
        return value2;
    }

    @Override
    public <T> void setAssociatedObject(IAssociatedContainerKey<T> key, T value) {
        values.put(key, () -> value);
    }
}

//public class SimpleAssociatedStorage<T> implements IAssociatedContainerKey<T> {
//
//    private static final AtomicInteger GENERATOR = new AtomicInteger();
//
//    private final int id;
//    private final String name;
//    private final Class<T> type;
//    private final Supplier<T> defaultValue;
//
//    public DataStorageKey(String name, Class<T> type, Supplier<T> defaultValue) {
//        this.id = GENERATOR.getAndIncrement();
//        this.name = name;
//        this.type = type;
//        this.defaultValue = defaultValue;
//    }
//
//    public static <T> DataStorageKey<T> of(String name, Class<T> type) {
//        return new DataStorageKey<>(name, type, null);
//    }
//
//    public static <T> DataStorageKey<T> of(String name, Class<T> type, Supplier<T> provider) {
//        return new DataStorageKey<>(name, type, provider);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof DataStorageKey)) return false;
//        DataStorageKey<?> that = (DataStorageKey<?>) o;
//        return id == that.id;
//    }
//
//    @Override
//    public int hashCode() {
//        return id;
//    }
//
//    @Override
//    public Class<T> getType() {
//        return type;
//    }
//
//    @Override
//    public T getDefaultValue() {
//        if (defaultValue != null) {
//            return defaultValue.get();
//        }
//        return null;
//    }
//}

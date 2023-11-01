package net.cocoonmc.core.utils;

import net.cocoonmc.runtime.IAssociatedContainerKey;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class SimpleAssociatedKey<T> implements IAssociatedContainerKey<T> {

    private static final AtomicInteger GENERATOR = new AtomicInteger();

    private final int id;
    private final Class<? extends T> type;
    private final Supplier<T> defaultValue;


    public SimpleAssociatedKey(Class<? extends T> type, Supplier<T> defaultValue) {
        this.id = GENERATOR.getAndIncrement();
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public static <T> SimpleAssociatedKey<T> of(Class<? extends T> type) {
        return new SimpleAssociatedKey<>(type, null);
    }

    public static <T> SimpleAssociatedKey<T> of(Class<? extends T> type, Supplier<T> provider) {
        return new SimpleAssociatedKey<>(type, provider);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleAssociatedKey)) return false;
        SimpleAssociatedKey<?> that = (SimpleAssociatedKey<?>) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public Class<? extends T> getType() {
        return type;
    }

    @Override
    public T getDefaultValue() {
        if (defaultValue != null) {
            return defaultValue.get();
        }
        return null;
    }
}

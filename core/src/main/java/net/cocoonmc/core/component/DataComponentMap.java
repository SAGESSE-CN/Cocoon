package net.cocoonmc.core.component;

import org.jetbrains.annotations.Nullable;

public interface DataComponentMap {

    default boolean has(DataComponentType<?> componentType) {
        return this.get(componentType) != null;
    }

    @Nullable
    <T> T get(DataComponentType<? extends T> componentType);

    default <T> T getOrDefault(DataComponentType<? extends T> componentType, T defaultValue) {
        T value = get(componentType);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    <T> void set(DataComponentType<? super T> dataComponentType, @Nullable T object);

    void remove(DataComponentType<?> dataComponentType);

    DataComponentMap copy();
}

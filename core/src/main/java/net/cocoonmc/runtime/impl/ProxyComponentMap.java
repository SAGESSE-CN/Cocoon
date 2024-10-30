package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.component.DataComponentMap;
import net.cocoonmc.core.component.DataComponentType;
import org.jetbrains.annotations.Nullable;

public class ProxyComponentMap implements DataComponentMap {

    protected final DataComponentMap first;
    protected final DataComponentMap second;

    public ProxyComponentMap(DataComponentMap first, DataComponentMap second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean has(DataComponentType<?> componentType) {
        return first.has(componentType);
    }

    @Override
    public <T> @Nullable T get(DataComponentType<? extends T> componentType) {
        return first.get(componentType);
    }

    @Override
    public <T> T getOrDefault(DataComponentType<? extends T> componentType, T defaultValue) {
        return first.getOrDefault(componentType, defaultValue);
    }

    @Override
    public <T> void set(DataComponentType<? super T> dataComponentType, @Nullable T object) {
        first.set(dataComponentType, object);
        second.set(dataComponentType, object);
    }

    @Override
    public void remove(DataComponentType<?> dataComponentType) {
        first.remove(dataComponentType);
        second.remove(dataComponentType);
    }

    @Override
    public DataComponentMap copy() {
        return first.copy();
    }
}

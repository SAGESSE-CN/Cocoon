package net.cocoonmc.core.utils;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketMap<T, V> {

    private final HashMap<Class<? extends T>, Function<T, V>> registered = new HashMap<>();

    public PacketMap(Consumer<PacketMap<T, V>> handler) {
        handler.accept(this);
    }

    public <T1 extends T, V1 extends V> void put(Class<T1> clazz, Function<T1, V1> transformer) {
        // noinspection unchecked
        registered.put(clazz, (Function<T, V>) transformer);
    }

    public V transform(T packet, Supplier<V> defaultValue) {
        Function<T, V> transformer = registered.get(packet.getClass());
        if (transformer != null) {
            return transformer.apply(packet);
        }
        return defaultValue.get();
    }
}

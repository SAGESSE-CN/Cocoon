package net.cocoonmc.runtime;

import java.util.function.Function;

public interface ICacheFactory {

    <T, V> void set(T obj, IAssociatedContainerKey<V> key, V value);

    <T, V> V get(T obj, IAssociatedContainerKey<V> key);

    default <T, V> V getOrElse(T obj, IAssociatedContainerKey<V> key, Function<T, V> getter) {
        V value = get(obj, key);
        if (value != null) {
            return value;
        }
        return getter.apply(obj);
    }

    default <T, V> V computeIfAbsent(T obj, IAssociatedContainerKey<V> key, Function<T, V> getter) {
        V value = get(obj, key);
        if (value != null) {
            return value;
        }
        value = getter.apply(obj);
        set(obj, key, value);
        return value;
    }
}

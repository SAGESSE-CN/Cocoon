package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.utils.SimpleAssociatedStorage;
import net.cocoonmc.runtime.IAssociatedContainer;
import net.cocoonmc.runtime.IAssociatedContainerKey;
import net.cocoonmc.runtime.ICacheFactory;

import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.function.Function;

public class CacheFactory implements ICacheFactory {

    private final HashMap<Class<?>, WeakHashMap<Object, SimpleAssociatedStorage>> allStorages = new HashMap<>();

    @Override
    public  <T, V> V get(T obj, IAssociatedContainerKey<V> key) {
        return _getContainer(obj, key).getAssociatedObject(key);
    }

    @Override
    public  <T, V> void set(T obj, IAssociatedContainerKey<V> key, V value) {
        _getContainer(obj, key).setAssociatedObject(key, value);
    }

    @Override
    public  <T, V> V computeIfAbsent(T obj, IAssociatedContainerKey<V> key, Function<T, V> getter) {
        IAssociatedContainer container = _getContainer(obj, key);
        V value = container.getAssociatedObject(key);
        if (value != null) {
            return value;
        }
        value = getter.apply(obj);
        container.setAssociatedObject(key, value);
        return value;
    }


    private IAssociatedContainer _getContainer(Object obj, IAssociatedContainerKey<?> key) {
        if (obj instanceof IAssociatedContainer) {
            return (IAssociatedContainer) obj;
        }
        return allStorages.computeIfAbsent(key.getType(), it -> new WeakHashMap<>()).computeIfAbsent(obj, it -> new SimpleAssociatedStorage());
    }
}

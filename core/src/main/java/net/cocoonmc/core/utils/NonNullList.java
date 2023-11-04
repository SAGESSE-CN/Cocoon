package net.cocoonmc.core.utils;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

public class NonNullList<E> extends AbstractList<E> {

    private final List<E> list;
    @Nullable
    private final E defaultValue;

    public static <E> NonNullList<E> create() {
        return new NonNullList<>(Lists.newArrayList(), null);
    }

    public static <E> NonNullList<E> createWithCapacity(int cap) {
        return new NonNullList<>(Lists.newArrayListWithCapacity(cap), null);
    }

    public static <E> NonNullList<E> withSize(int size, E object) {
        //Validate.notNull(object);
        Object[] objects = new Object[size];
        Arrays.fill(objects, object);
        // noinspection unchecked
        return new NonNullList<>(Arrays.asList((E[]) objects), object);
    }

    @SafeVarargs
    public static <E> NonNullList<E> of(E object, E... objects) {
        return new NonNullList<>(Arrays.asList(objects), object);
    }

    protected NonNullList(List<E> list, @Nullable E object) {
        this.list = list;
        this.defaultValue = object;
    }

    @NotNull
    public E get(int i) {
        return this.list.get(i);
    }

    public E set(int i, E object) {
        //Validate.notNull(object);
        return this.list.set(i, object);
    }

    public void add(int i, E object) {
        //Validate.notNull(object);
        this.list.add(i, object);
    }

    public E remove(int i) {
        return this.list.remove(i);
    }

    public int size() {
        return this.list.size();
    }

    public void clear() {
        if (this.defaultValue == null) {
            super.clear();
        } else {
            for (int i = 0; i < this.size(); ++i) {
                this.set(i, this.defaultValue);
            }
        }
    }
}

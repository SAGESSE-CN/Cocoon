package net.cocoonmc.core.nbt;

@SuppressWarnings("unused")
public interface CollectionTag<T extends Tag> extends Tag {

    T get(int index);

    void set(int index, T value);

    void add(int index, T value);

    void remove(int index);

    void clear();

    byte getElementType();

    int size();

    default void add(T value) {
        add(size(), value);
    }

    default boolean isEmpty() {
        return size() == 0;
    }
}

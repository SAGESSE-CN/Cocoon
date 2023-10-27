package net.cocoonmc.core.nbt;

import net.cocoonmc.impl.Versions;

import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("unused")
public interface ListTag extends CollectionTag<Tag> {

    static ListTag newInstance() {
        return Versions.TAG.create(null, 0);
    }

    @Override
    default byte getType() {
        return 9;
    }

    default short getByte(int index) {
        return _get(index, 1, NumericTag::getAsByte).orElse((byte) 0);
    }

    default short getShort(int index) {
        return _get(index, 2, NumericTag::getAsShort).orElse((short) 0);
    }

    default int getInt(int index) {
        return _get(index, 3, NumericTag::getAsInt).orElse(0);
    }

    default long getLong(int index) {
        return _get(index, 4, NumericTag::getAsLong).orElse(0L);
    }

    default float getFloat(int index) {
        return _get(index, 5, NumericTag::getAsFloat).orElse(0.0f);
    }

    default double getDouble(int index) {
        return _get(index, 6, NumericTag::getAsDouble).orElse(0.0);
    }

    default boolean getBoolean(int index) {
        return getByte(index) != 0;
    }

    default String getString(int index) {
        return _get(index, 8, StringTag::getAsString).orElse("");
    }

    default byte[] getByteArray(int index) {
        return _get(index, 7, ByteArrayTag::getAsByteArray).orElseGet(() -> new byte[0]);
    }

    default int[] getIntArray(int index) {
        return _get(index, 11, IntArrayTag::getAsIntArray).orElseGet(() -> new int[0]);
    }

    default long[] getLongArray(int index) {
        return _get(index, 12, LongArrayTag::getAsLongArray).orElseGet(() -> new long[0]);
    }

    default CompoundTag getCompound(int index) {
        return _get(index, 10, it -> (CompoundTag) it).orElseGet(CompoundTag::newInstance);
    }

    default ListTag getList(int index) {
        return _get(index, 9, it -> (ListTag) it).orElseGet(ListTag::newInstance);
    }

    ListTag copy();

    @SuppressWarnings("unchecked")
    default <T extends Tag, V> Optional<V> _get(int index, int type, Function<T, V> getter) {
        if (index >= 0 && index < size()) {
            Tag tag = get(index);
            if (tag.getType() == type) {
                return Optional.ofNullable(getter.apply((T) tag));
            }
        }
        return Optional.empty();
    }
}

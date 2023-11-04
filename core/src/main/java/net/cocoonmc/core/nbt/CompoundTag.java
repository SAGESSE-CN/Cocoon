package net.cocoonmc.core.nbt;

import net.cocoonmc.Cocoon;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

@SuppressWarnings("unused")
public interface CompoundTag extends Tag {

    static CompoundTag newInstance() {
        return Cocoon.API.TAG.create((Map<String, Tag>) null);
    }

    static CompoundTag parseTag(String value) {
        return Cocoon.API.TAG.parseTag(value);
    }

    @Override
    default byte getType() {
        return 10;
    }

    void put(String key, Tag tag);

    default void putByte(String key, byte value) {
        put(key, ByteTag.valueOf(value));
    }

    default void putShort(String key, short value) {
        put(key, ShortTag.valueOf(value));
    }

    default void putInt(String key, int value) {
        put(key, IntTag.valueOf(value));
    }

    default void putLong(String key, long value) {
        put(key, LongTag.valueOf(value));
    }

    default void putFloat(String key, float value) {
        put(key, FloatTag.valueOf(value));
    }

    default void putDouble(String key, double value) {
        put(key, DoubleTag.valueOf(value));
    }

    default void putByteArray(String key, byte[] value) {
        put(key, ByteArrayTag.valueOf(value));
    }

    default void putByteArray(String key, List<Byte> value) {
        put(key, ByteArrayTag.valueOf(value));
    }

    default void putIntArray(String key, int[] value) {
        put(key, IntArrayTag.valueOf(value));
    }

    default void putIntArray(String key, List<Integer> value) {
        put(key, IntArrayTag.valueOf(value));
    }

    default void putLongArray(String key, long[] value) {
        put(key, LongArrayTag.valueOf(value));
    }

    default void putLongArray(String key, List<Long> value) {
        put(key, LongArrayTag.valueOf(value));
    }

    default void putBoolean(String key, boolean value) {
        put(key, ByteTag.valueOf(value));
    }

    default void putString(String key, String value) {
        put(key, StringTag.valueOf(value));
    }

    default void putUUID(String key, UUID value) {
        long var1 = value.getMostSignificantBits();
        long var3 = value.getLeastSignificantBits();
        int[] values = new int[4];
        values[0] = (int) (var1 >> 32);
        values[1] = (int) (var1);
        values[2] = (int) (var3 >> 32);
        values[3] = (int) (var3);
        putIntArray(key, values);
    }

    void remove(String key);

    default void clear() {
        getAllKeys().forEach(this::remove);
    }

    @Nullable
    Tag get(String key);

    default byte getByte(String key) {
        return _get(key, 99, NumericTag::getAsByte).orElse((byte) 0);
    }

    default short getShort(String key) {
        return _get(key, 99, NumericTag::getAsShort).orElse((short) 0);
    }

    default int getInt(String key) {
        return _get(key, 99, NumericTag::getAsInt).orElse(0);
    }

    default long getLong(String key) {
        return _get(key, 99, NumericTag::getAsLong).orElse(0L);
    }

    default float getFloat(String key) {
        return _get(key, 99, NumericTag::getAsFloat).orElse(0.0f);
    }

    default double getDouble(String key) {
        return _get(key, 99, NumericTag::getAsDouble).orElse(0.0);
    }

    default boolean getBoolean(String key) {
        return getByte(key) != 0;
    }

    default String getString(String key) {
        return _get(key, 8, StringTag::getAsString).orElse("");
    }

    default UUID getUUID(String key) {
        int type = getType(key);
        if (getType(key) != 11) {
            throw new IllegalArgumentException("Expected UUID-Tag to be of type " + 11 + ", but found " + type + ".");
        }
        int[] values = getIntArray(key);
        if (values.length != 4) {
            throw new IllegalArgumentException("Expected UUID-Array to be of length 4, but found " + values.length + ".");
        }
        return new UUID((long) values[0] << 32 | (long) values[1], (long) values[2] << 32 | (long) values[3]);
    }

    default byte[] getByteArray(String key) {
        return _get(key, 7, ByteArrayTag::getAsByteArray).orElseGet(() -> new byte[0]);
    }

    default int[] getIntArray(String key) {
        return _get(key, 11, IntArrayTag::getAsIntArray).orElseGet(() -> new int[0]);
    }

    default long[] getLongArray(String key) {
        return _get(key, 12, LongArrayTag::getAsLongArray).orElseGet(() -> new long[0]);
    }

    default CompoundTag getCompound(String key) {
        return _get(key, 10, it -> (CompoundTag) it).orElseGet(CompoundTag::newInstance);
    }

    default ListTag getList(String key, int type) {
        return _get(key, 9, _validator(type)).orElseGet(ListTag::newInstance);
    }

    default byte getType(String key) {
        Tag tag = get(key);
        if (tag != null) {
            return tag.getType();
        }
        return 0;
    }

    default boolean contains(String key) {
        return getType(key) != 0;
    }

    default boolean contains(String key, int type) {
        int tp = getType(key);
        if (tp == type) {
            return true;
        }
        if (type == 99) {
            return tp == 1 || tp == 2 || tp == 3 || tp == 4 || tp == 5 || tp == 6;
        }
        return false;
    }

    Set<String> getAllKeys();

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    CompoundTag copy();

    default <T extends Tag, V> Optional<V> _get(String key, int type, Function<T, V> getter) {
        if (contains(key, type)) {
            Tag tag = get(key);
            if (tag != null) {
                // noinspection unchecked
                return Optional.ofNullable(getter.apply((T) tag));
            }
        }
        return Optional.empty();
    }

    default Function<ListTag, ListTag> _validator(int type) {
        return it -> {
            if (!it.isEmpty() && it.getElementType() != type) {
                return null;
            }
            return it;
        };
    }
}

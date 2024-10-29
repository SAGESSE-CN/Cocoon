package net.cocoonmc.runtime.v1_18_R2;

import net.cocoonmc.runtime.ITagFactory;
import net.cocoonmc.runtime.impl.TagWrapper;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagFactory extends TagWrapper implements ITagFactory {

    @Override
    public WrappedByteTag create(byte value) {
        return wrap(ByteTag.valueOf(value));
    }

    @Override
    public WrappedShortTag create(short value) {
        return wrap(ShortTag.valueOf(value));
    }

    @Override
    public WrappedIntTag create(int value) {
        return wrap(IntTag.valueOf(value));
    }

    @Override
    public WrappedLongTag create(long value) {
        return wrap(LongTag.valueOf(value));
    }

    @Override
    public WrappedFloatTag create(float value) {
        return wrap(FloatTag.valueOf(value));
    }

    @Override
    public WrappedDoubleTag create(double value) {
        return wrap(DoubleTag.valueOf(value));
    }

    @Override
    public WrappedStringTag create(String value) {
        return wrap(StringTag.valueOf(value));
    }

    @Override
    public WrappedByteArrayTag create(byte[] values) {
        return wrap(new ByteArrayTag(values));
    }

    @Override
    public WrappedIntArrayTag create(int[] values) {
        return wrap(new IntArrayTag(values));
    }

    @Override
    public WrappedLongArrayTag create(long[] values) {
        return wrap(new LongArrayTag(values));
    }

    @Override
    public WrappedListTag create(List<net.cocoonmc.core.nbt.Tag> values, int elementType) {
        return wrap(new ListTag());
    }

    @Override
    public WrappedCompoundTag create(Map<String, net.cocoonmc.core.nbt.Tag> values) {
        return wrap(new CompoundTag());
    }


    @Override
    public WrappedCompoundTag read(InputStream inputStream) throws IOException {
        return wrap(NbtIo.read(new DataInputStream(inputStream)));
    }

    @Override
    public void write(OutputStream outputStream, net.cocoonmc.core.nbt.CompoundTag tag) throws IOException {
        NbtIo.write(unwrap(tag), new DataOutputStream(outputStream));
    }

    @Override
    public WrappedCompoundTag readCompressed(InputStream inputStream) throws IOException {
        return wrap(NbtIo.readCompressed(inputStream));
    }

    @Override
    public void writeCompressed(OutputStream outputStream, net.cocoonmc.core.nbt.CompoundTag tag) throws IOException {
        NbtIo.writeCompressed(unwrap(tag), outputStream);
    }

    @Override
    public WrappedCompoundTag parseTag(String tag) {
        try {
            return wrap(TagParser.parseTag(tag));
        } catch (Exception e) {
            return create((Map<String, net.cocoonmc.core.nbt.Tag>) null);
        }
    }

    public static WrappedByteTag wrap(ByteTag tag) {
        return new WrappedByteTag(tag) {

            @Override
            public Number getAsNumber() {
                return tag.getAsNumber();
            }
        };
    }

    public static WrappedShortTag wrap(ShortTag tag) {
        return new WrappedShortTag(tag) {

            @Override
            public Number getAsNumber() {
                return tag.getAsNumber();
            }
        };
    }

    public static WrappedIntTag wrap(IntTag tag) {
        return new WrappedIntTag(tag) {

            @Override
            public Number getAsNumber() {
                return tag.getAsNumber();
            }
        };
    }

    public static WrappedLongTag wrap(LongTag tag) {
        return new WrappedLongTag(tag) {

            @Override
            public Number getAsNumber() {
                return tag.getAsNumber();
            }
        };
    }

    public static WrappedFloatTag wrap(FloatTag tag) {
        return new WrappedFloatTag(tag) {

            @Override
            public Number getAsNumber() {
                return tag.getAsNumber();
            }
        };
    }

    public static WrappedDoubleTag wrap(DoubleTag tag) {
        return new WrappedDoubleTag(tag) {

            @Override
            public Number getAsNumber() {
                return tag.getAsNumber();
            }
        };
    }

    public static WrappedStringTag wrap(StringTag tag) {
        return new WrappedStringTag(tag) {

            @Override
            public String getAsString() {
                return tag.getAsString();
            }
        };
    }

    public static WrappedByteArrayTag wrap(ByteArrayTag tag) {
        return new WrappedByteArrayTag(tag) {

            @Override
            public byte[] getAsByteArray() {
                return tag.getAsByteArray();
            }

            @Override
            public net.cocoonmc.core.nbt.ByteTag get(int index) {
                return wrap(tag.get(index));
            }

            @Override
            public void set(int index, net.cocoonmc.core.nbt.ByteTag value) {
                tag.set(index, unwrap(value));
            }

            @Override
            public void add(int index, net.cocoonmc.core.nbt.ByteTag value) {
                tag.add(index, unwrap(value));
            }

            @Override
            public void remove(int index) {
                tag.remove(index);
            }

            @Override
            public void clear() {
                tag.clear();
            }

            @Override
            public int size() {
                return tag.size();
            }
        };
    }

    public static WrappedIntArrayTag wrap(IntArrayTag tag) {
        return new WrappedIntArrayTag(tag) {

            @Override
            public int[] getAsIntArray() {
                return tag.getAsIntArray();
            }

            @Override
            public net.cocoonmc.core.nbt.IntTag get(int index) {
                return wrap(tag.get(index));
            }

            @Override
            public void set(int index, net.cocoonmc.core.nbt.IntTag value) {
                tag.set(index, unwrap(value));
            }

            @Override
            public void add(int index, net.cocoonmc.core.nbt.IntTag value) {
                tag.add(index, unwrap(value));
            }

            @Override
            public void remove(int index) {
                tag.remove(index);
            }

            @Override
            public void clear() {
                tag.clear();
            }

            @Override
            public int size() {
                return tag.size();
            }
        };
    }

    public static WrappedLongArrayTag wrap(LongArrayTag tag) {
        return new WrappedLongArrayTag(tag) {

            @Override
            public long[] getAsLongArray() {
                return tag.getAsLongArray();
            }

            @Override
            public net.cocoonmc.core.nbt.LongTag get(int index) {
                return wrap(tag.get(index));
            }

            @Override
            public void set(int index, net.cocoonmc.core.nbt.LongTag value) {
                tag.set(index, unwrap(value));
            }

            @Override
            public void add(int index, net.cocoonmc.core.nbt.LongTag value) {
                tag.add(index, unwrap(value));
            }

            @Override
            public void remove(int index) {
                tag.remove(index);
            }

            @Override
            public void clear() {
                tag.clear();
            }

            @Override
            public int size() {
                return tag.size();
            }
        };
    }

    public static WrappedListTag wrap(ListTag tag) {
        return new WrappedListTag(tag) {

            @Override
            public byte getElementType() {
                return tag.getElementType();
            }

            @Override
            public net.cocoonmc.core.nbt.Tag get(int index) {
                return wrap(tag.get(index));
            }

            @Override
            public void set(int index, net.cocoonmc.core.nbt.Tag value) {
                tag.set(index, unwrap(value));
            }

            @Override
            public void add(int index, net.cocoonmc.core.nbt.Tag value) {
                tag.add(index, unwrap(value));
            }

            @Override
            public void remove(int index) {
                tag.remove(index);
            }

            @Override
            public void clear() {
                tag.clear();
            }

            @Override
            public int size() {
                return tag.size();
            }

            @Override
            public net.cocoonmc.core.nbt.ListTag copy() {
                return wrap(tag.copy());
            }
        };
    }

    public static WrappedCompoundTag wrap(CompoundTag tag) {
        return new WrappedCompoundTag(tag) {

            @Override
            public net.cocoonmc.core.nbt.Tag get(String key) {
                return wrap(tag.get(key));
            }

            @Override
            public void put(String key, net.cocoonmc.core.nbt.Tag value) {
                tag.put(key, unwrap(value));
            }

            @Override
            public void remove(String key) {
                tag.remove(key);
            }

            @Override
            public void merge(net.cocoonmc.core.nbt.CompoundTag tag) {
                tag.merge(unwrap(tag));
            }

            @Override
            public Set<String> getAllKeys() {
                return tag.getAllKeys();
            }

            @Override
            public byte getType(String key) {
                return tag.getTagType(key);
            }

            @Override
            public boolean contains(String key) {
                return tag.contains(key);
            }

            @Override
            public int size() {
                return tag.size();
            }

            @Override
            public net.cocoonmc.core.nbt.CompoundTag copy() {
                return wrap(tag.copy());
            }
        };
    }

    public static WrappedTag wrap(Tag tag) {
        if (tag instanceof ByteTag) {
            return wrap((ByteTag) tag);
        }
        if (tag instanceof ShortTag) {
            return wrap((ShortTag) tag);
        }
        if (tag instanceof IntTag) {
            return wrap((IntTag) tag);
        }
        if (tag instanceof LongTag) {
            return wrap((LongTag) tag);
        }
        if (tag instanceof FloatTag) {
            return wrap((FloatTag) tag);
        }
        if (tag instanceof DoubleTag) {
            return wrap((DoubleTag) tag);
        }
        if (tag instanceof StringTag) {
            return wrap((StringTag) tag);
        }
        if (tag instanceof ByteArrayTag) {
            return wrap((ByteArrayTag) tag);
        }
        if (tag instanceof IntArrayTag) {
            return wrap((IntArrayTag) tag);
        }
        if (tag instanceof LongArrayTag) {
            return wrap((LongArrayTag) tag);
        }
        if (tag instanceof ListTag) {
            return wrap((ListTag) tag);
        }
        if (tag instanceof CompoundTag) {
            return wrap((CompoundTag) tag);
        }
        return new WrappedTag(tag) {

            @Override
            public byte getType() {
                return tag.getId();
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends Tag> T unwrap(net.cocoonmc.core.nbt.Tag tag) {
        return (T) tag.getHandle();
    }
}

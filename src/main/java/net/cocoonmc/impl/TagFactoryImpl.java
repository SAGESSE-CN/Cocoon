package net.cocoonmc.impl;

import com.google.common.collect.Lists;
import net.cocoonmc.core.nbt.ByteArrayTag;
import net.cocoonmc.core.nbt.ByteTag;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.DoubleTag;
import net.cocoonmc.core.nbt.FloatTag;
import net.cocoonmc.core.nbt.IntArrayTag;
import net.cocoonmc.core.nbt.IntTag;
import net.cocoonmc.core.nbt.ListTag;
import net.cocoonmc.core.nbt.LongArrayTag;
import net.cocoonmc.core.nbt.LongTag;
import net.cocoonmc.core.nbt.ShortTag;
import net.cocoonmc.core.nbt.StringTag;
import net.cocoonmc.core.nbt.Tag;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.nbt.TagParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagFactoryImpl implements TagFactory {

    @Override
    public ByteTag create(byte value) {
        return wrap(net.minecraft.nbt.ByteTag.valueOf(value));
    }

    @Override
    public ShortTag create(short value) {
        return wrap(net.minecraft.nbt.ShortTag.valueOf(value));
    }

    @Override
    public IntTag create(int value) {
        return wrap(net.minecraft.nbt.IntTag.valueOf(value));
    }

    @Override
    public LongTag create(long value) {
        return wrap(net.minecraft.nbt.LongTag.valueOf(value));
    }

    @Override
    public FloatTag create(float value) {
        return wrap(net.minecraft.nbt.FloatTag.valueOf(value));
    }

    @Override
    public DoubleTag create(double value) {
        return wrap(net.minecraft.nbt.DoubleTag.valueOf(value));
    }

    @Override
    public StringTag create(String value) {
        return wrap(net.minecraft.nbt.StringTag.valueOf(value));
    }

    @Override
    public ByteArrayTag create(byte[] values) {
        return wrap(new net.minecraft.nbt.ByteArrayTag(values));
    }

    @Override
    public IntArrayTag create(int[] values) {
        return wrap(new net.minecraft.nbt.IntArrayTag(values));
    }

    @Override
    public LongArrayTag create(long[] values) {
        return wrap(new net.minecraft.nbt.LongArrayTag(values));
    }

    @Override
    public ListTag create(List<Tag> values, int elementType) {
        return wrap(new net.minecraft.nbt.ListTag());
    }

    @Override
    public CompoundTag create(Map<String, Tag> values) {
        return wrap(new net.minecraft.nbt.CompoundTag());
    }


    @Override
    public CompoundTag read(InputStream inputStream) throws IOException {
        return wrap(net.minecraft.nbt.NbtIo.read(new DataInputStream(inputStream)));
    }

    @Override
    public void write(OutputStream outputStream, CompoundTag tag) throws IOException {
        net.minecraft.nbt.NbtIo.write(unwrap(tag), new DataOutputStream(outputStream));
    }

    @Override
    public CompoundTag readCompressed(InputStream inputStream) throws IOException {
        return wrap(net.minecraft.nbt.NbtIo.readCompressed(inputStream));
    }

    @Override
    public void writeCompressed(OutputStream outputStream, CompoundTag tag) throws IOException {
        net.minecraft.nbt.NbtIo.writeCompressed(unwrap(tag), outputStream);
    }

    @Override
    public CompoundTag fromString(String tag) {
        try {
            return wrap(TagParser.parseTag(tag));
        } catch (Exception e) {
            return CompoundTag.newInstance();
        }
    }

    @Override
    public String toString(CompoundTag tag) {
        return new SnbtPrinterTagVisitor("", 0, Lists.newArrayList()).visit(unwrap(tag));
    }

    public static ByteTag wrap(net.minecraft.nbt.ByteTag tag) {
        return new ByteTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Number getAsNumber() {
                return tag.getAsNumber();
            }
        };
    }

    public static ShortTag wrap(net.minecraft.nbt.ShortTag tag) {
        return new ShortTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Number getAsNumber() {
                return tag.getAsNumber();
            }
        };
    }

    public static IntTag wrap(net.minecraft.nbt.IntTag tag) {
        return new IntTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Number getAsNumber() {
                return tag.getAsNumber();
            }
        };
    }

    public static LongTag wrap(net.minecraft.nbt.LongTag tag) {
        return new LongTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Number getAsNumber() {
                return tag.getAsNumber();
            }
        };
    }

    public static FloatTag wrap(net.minecraft.nbt.FloatTag tag) {
        return new FloatTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Number getAsNumber() {
                return tag.getAsNumber();
            }
        };
    }

    public static DoubleTag wrap(net.minecraft.nbt.DoubleTag tag) {
        return new DoubleTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Number getAsNumber() {
                return tag.getAsNumber();
            }
        };
    }

    public static StringTag wrap(net.minecraft.nbt.StringTag tag) {
        return new StringTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public String getAsString() {
                return tag.getAsString();
            }
        };
    }

    public static ByteArrayTag wrap(net.minecraft.nbt.ByteArrayTag tag) {
        return new ByteArrayTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public byte[] getAsByteArray() {
                return tag.getAsByteArray();
            }

            @Override
            public ByteTag get(int index) {
                return wrap(tag.get(index));
            }

            @Override
            public void set(int index, ByteTag value) {
                tag.set(index, unwrap(value));
            }

            @Override
            public void add(int index, ByteTag value) {
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

    public static IntArrayTag wrap(net.minecraft.nbt.IntArrayTag tag) {
        return new IntArrayTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public int[] getAsIntArray() {
                return tag.getAsIntArray();
            }

            @Override
            public IntTag get(int index) {
                return wrap(tag.get(index));
            }

            @Override
            public void set(int index, IntTag value) {
                tag.set(index, unwrap(value));
            }

            @Override
            public void add(int index, IntTag value) {
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

    public static LongArrayTag wrap(net.minecraft.nbt.LongArrayTag tag) {
        return new LongArrayTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public long[] getAsLongArray() {
                return tag.getAsLongArray();
            }

            @Override
            public LongTag get(int index) {
                return wrap(tag.get(index));
            }

            @Override
            public void set(int index, LongTag value) {
                tag.set(index, unwrap(value));
            }

            @Override
            public void add(int index, LongTag value) {
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

    public static ListTag wrap(net.minecraft.nbt.ListTag tag) {
        return new ListTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public byte getElementType() {
                return tag.getElementType();
            }

            @Override
            public Tag get(int index) {
                return wrap(tag.get(index));
            }

            @Override
            public void set(int index, Tag value) {
                tag.set(index, unwrap(value));
            }

            @Override
            public void add(int index, Tag value) {
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
            public ListTag copy() {
                return wrap(tag.copy());
            }
        };
    }

    public static CompoundTag wrap(net.minecraft.nbt.CompoundTag tag) {
        return new CompoundTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Tag get(String key) {
                return wrap(tag.get(key));
            }

            @Override
            public void put(String key, Tag value) {
                tag.put(key, unwrap(value));
            }

            @Override
            public void remove(String key) {
                tag.remove(key);
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
            public CompoundTag copy() {
                return wrap(tag.copy());
            }
        };
    }

    public static Tag wrap(net.minecraft.nbt.Tag tag) {
        if (tag instanceof net.minecraft.nbt.ByteTag) {
            return wrap((net.minecraft.nbt.ByteTag) tag);
        }
        if (tag instanceof net.minecraft.nbt.ShortTag) {
            return wrap((net.minecraft.nbt.ShortTag) tag);
        }
        if (tag instanceof net.minecraft.nbt.IntTag) {
            return wrap((net.minecraft.nbt.IntTag) tag);
        }
        if (tag instanceof net.minecraft.nbt.LongTag) {
            return wrap((net.minecraft.nbt.LongTag) tag);
        }
        if (tag instanceof net.minecraft.nbt.FloatTag) {
            return wrap((net.minecraft.nbt.FloatTag) tag);
        }
        if (tag instanceof net.minecraft.nbt.DoubleTag) {
            return wrap((net.minecraft.nbt.DoubleTag) tag);
        }
        if (tag instanceof net.minecraft.nbt.StringTag) {
            return wrap((net.minecraft.nbt.StringTag) tag);
        }
        if (tag instanceof net.minecraft.nbt.ByteArrayTag) {
            return wrap((net.minecraft.nbt.ByteArrayTag) tag);
        }
        if (tag instanceof net.minecraft.nbt.IntArrayTag) {
            return wrap((net.minecraft.nbt.IntArrayTag) tag);
        }
        if (tag instanceof net.minecraft.nbt.LongArrayTag) {
            return wrap((net.minecraft.nbt.LongArrayTag) tag);
        }
        if (tag instanceof net.minecraft.nbt.ListTag) {
            return wrap((net.minecraft.nbt.ListTag) tag);
        }
        if (tag instanceof net.minecraft.nbt.CompoundTag) {
            return wrap((net.minecraft.nbt.CompoundTag) tag);
        }
        return new Tag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public byte getType() {
                return tag.getId();
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends net.minecraft.nbt.Tag> T unwrap(Tag tag) {
        return (T) tag.getHandle();
    }
}

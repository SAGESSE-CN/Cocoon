package net.cocoonmc.runtime.v1_16_R3;

import net.cocoonmc.runtime.ITagFactory;
import net.minecraft.server.v1_16_R3.MojangsonParser;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_16_R3.NBTTagByte;
import net.minecraft.server.v1_16_R3.NBTTagByteArray;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagDouble;
import net.minecraft.server.v1_16_R3.NBTTagFloat;
import net.minecraft.server.v1_16_R3.NBTTagInt;
import net.minecraft.server.v1_16_R3.NBTTagIntArray;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.NBTTagLong;
import net.minecraft.server.v1_16_R3.NBTTagLongArray;
import net.minecraft.server.v1_16_R3.NBTTagShort;
import net.minecraft.server.v1_16_R3.NBTTagString;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagFactory implements ITagFactory {

    @Override
    public net.cocoonmc.core.nbt.ByteTag create(byte value) {
        return wrap(NBTTagByte.a(value));
    }

    @Override
    public net.cocoonmc.core.nbt.ShortTag create(short value) {
        return wrap(NBTTagShort.a(value));
    }

    @Override
    public net.cocoonmc.core.nbt.IntTag create(int value) {
        return wrap(NBTTagInt.a(value));
    }

    @Override
    public net.cocoonmc.core.nbt.LongTag create(long value) {
        return wrap(NBTTagLong.a(value));
    }

    @Override
    public net.cocoonmc.core.nbt.FloatTag create(float value) {
        return wrap(NBTTagFloat.a(value));
    }

    @Override
    public net.cocoonmc.core.nbt.DoubleTag create(double value) {
        return wrap(NBTTagDouble.a(value));
    }

    @Override
    public net.cocoonmc.core.nbt.StringTag create(String value) {
        return wrap(NBTTagString.a(value));
    }

    @Override
    public net.cocoonmc.core.nbt.ByteArrayTag create(byte[] values) {
        return wrap(new NBTTagByteArray(values));
    }

    @Override
    public net.cocoonmc.core.nbt.IntArrayTag create(int[] values) {
        return wrap(new NBTTagIntArray(values));
    }

    @Override
    public net.cocoonmc.core.nbt.LongArrayTag create(long[] values) {
        return wrap(new NBTTagLongArray(values));
    }

    @Override
    public net.cocoonmc.core.nbt.ListTag create(List<net.cocoonmc.core.nbt.Tag> values, int elementType) {
        return wrap(new NBTTagList());
    }

    @Override
    public net.cocoonmc.core.nbt.CompoundTag create(Map<String, net.cocoonmc.core.nbt.Tag> values) {
        return wrap(new NBTTagCompound());
    }


    @Override
    public net.cocoonmc.core.nbt.CompoundTag read(InputStream inputStream) throws IOException {
        return wrap(NBTCompressedStreamTools.a((DataInput) new DataInputStream(inputStream)));
    }

    @Override
    public void write(OutputStream outputStream, net.cocoonmc.core.nbt.CompoundTag tag) throws IOException {
        NBTCompressedStreamTools.a(unwrap(tag), (DataOutput) new DataOutputStream(outputStream));
    }

    @Override
    public net.cocoonmc.core.nbt.CompoundTag readCompressed(InputStream inputStream) throws IOException {
        return wrap(NBTCompressedStreamTools.a(inputStream));
    }

    @Override
    public void writeCompressed(OutputStream outputStream, net.cocoonmc.core.nbt.CompoundTag tag) throws IOException {
        NBTCompressedStreamTools.a(unwrap(tag), outputStream);
    }

    @Override
    public net.cocoonmc.core.nbt.CompoundTag fromString(String tag) {
        try {
            return wrap(MojangsonParser.parse(tag));
        } catch (Exception e) {
            return net.cocoonmc.core.nbt.CompoundTag.newInstance();
        }
    }

    @Override
    public String toString(net.cocoonmc.core.nbt.CompoundTag tag) {
        return unwrap(tag).toString();
    }

    public static net.cocoonmc.core.nbt.ByteTag wrap(NBTTagByte tag) {
        return new net.cocoonmc.core.nbt.ByteTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Number getAsNumber() {
                return tag.k();
            }
        };
    }

    public static net.cocoonmc.core.nbt.ShortTag wrap(NBTTagShort tag) {
        return new net.cocoonmc.core.nbt.ShortTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Number getAsNumber() {
                return tag.k();
            }
        };
    }

    public static net.cocoonmc.core.nbt.IntTag wrap(NBTTagInt tag) {
        return new net.cocoonmc.core.nbt.IntTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Number getAsNumber() {
                return tag.k();
            }
        };
    }

    public static net.cocoonmc.core.nbt.LongTag wrap(NBTTagLong tag) {
        return new net.cocoonmc.core.nbt.LongTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Number getAsNumber() {
                return tag.k();
            }
        };
    }

    public static net.cocoonmc.core.nbt.FloatTag wrap(NBTTagFloat tag) {
        return new net.cocoonmc.core.nbt.FloatTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Number getAsNumber() {
                return tag.k();
            }
        };
    }

    public static net.cocoonmc.core.nbt.DoubleTag wrap(NBTTagDouble tag) {
        return new net.cocoonmc.core.nbt.DoubleTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public Number getAsNumber() {
                return tag.k();
            }
        };
    }

    public static net.cocoonmc.core.nbt.StringTag wrap(NBTTagString tag) {
        return new net.cocoonmc.core.nbt.StringTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public String getAsString() {
                return tag.asString();
            }
        };
    }

    public static net.cocoonmc.core.nbt.ByteArrayTag wrap(NBTTagByteArray tag) {
        return new net.cocoonmc.core.nbt.ByteArrayTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public byte[] getAsByteArray() {
                return tag.getBytes();
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

    public static net.cocoonmc.core.nbt.IntArrayTag wrap(NBTTagIntArray tag) {
        return new net.cocoonmc.core.nbt.IntArrayTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public int[] getAsIntArray() {
                return tag.getInts();
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

    public static net.cocoonmc.core.nbt.LongArrayTag wrap(NBTTagLongArray tag) {
        return new net.cocoonmc.core.nbt.LongArrayTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public long[] getAsLongArray() {
                return tag.getLongs();
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

    public static net.cocoonmc.core.nbt.ListTag wrap(NBTTagList tag) {
        return new net.cocoonmc.core.nbt.ListTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public byte getElementType() {
                return tag.d_();
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
                return wrap(tag.clone());
            }
        };
    }

    public static net.cocoonmc.core.nbt.CompoundTag wrap(NBTTagCompound tag) {
        return new net.cocoonmc.core.nbt.CompoundTag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public net.cocoonmc.core.nbt.Tag get(String key) {
                return wrap(tag.get(key));
            }

            @Override
            public void put(String key, net.cocoonmc.core.nbt.Tag value) {
                tag.set(key, unwrap(value));
            }

            @Override
            public void remove(String key) {
                tag.remove(key);
            }

            @Override
            public Set<String> getAllKeys() {
                return tag.getKeys();
            }

            @Override
            public byte getType(String key) {
                return tag.d(key);
            }

            @Override
            public boolean contains(String key) {
                return tag.hasKey(key);
            }

            @Override
            public int size() {
                return tag.e();
            }

            @Override
            public net.cocoonmc.core.nbt.CompoundTag copy() {
                return wrap(tag.clone());
            }
        };
    }

    public static net.cocoonmc.core.nbt.Tag wrap(NBTBase tag) {
        if (tag instanceof NBTTagByte) {
            return wrap((NBTTagByte) tag);
        }
        if (tag instanceof NBTTagShort) {
            return wrap((NBTTagShort) tag);
        }
        if (tag instanceof NBTTagInt) {
            return wrap((NBTTagInt) tag);
        }
        if (tag instanceof NBTTagLong) {
            return wrap((NBTTagLong) tag);
        }
        if (tag instanceof NBTTagFloat) {
            return wrap((NBTTagFloat) tag);
        }
        if (tag instanceof NBTTagDouble) {
            return wrap((NBTTagDouble) tag);
        }
        if (tag instanceof NBTTagString) {
            return wrap((NBTTagString) tag);
        }
        if (tag instanceof NBTTagByteArray) {
            return wrap((NBTTagByteArray) tag);
        }
        if (tag instanceof NBTTagIntArray) {
            return wrap((NBTTagIntArray) tag);
        }
        if (tag instanceof NBTTagLongArray) {
            return wrap((NBTTagLongArray) tag);
        }
        if (tag instanceof NBTTagList) {
            return wrap((NBTTagList) tag);
        }
        if (tag instanceof NBTTagCompound) {
            return wrap((NBTTagCompound) tag);
        }
        return new net.cocoonmc.core.nbt.Tag() {

            @Override
            public Object getHandle() {
                return tag;
            }

            @Override
            public byte getType() {
                return tag.getTypeId();
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends NBTBase> T unwrap(net.cocoonmc.core.nbt.Tag tag) {
        return (T) tag.getHandle();
    }
}

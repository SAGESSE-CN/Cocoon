package net.cocoonmc.runtime.v1_16_R3;

import net.cocoonmc.runtime.ITagFactory;
import net.cocoonmc.runtime.impl.TagWrapper;
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

public class TagFactory extends TagWrapper implements ITagFactory {

    @Override
    public WrappedByteTag create(byte value) {
        return wrap(NBTTagByte.a(value));
    }

    @Override
    public WrappedShortTag create(short value) {
        return wrap(NBTTagShort.a(value));
    }

    @Override
    public WrappedIntTag create(int value) {
        return wrap(NBTTagInt.a(value));
    }

    @Override
    public WrappedLongTag create(long value) {
        return wrap(NBTTagLong.a(value));
    }

    @Override
    public WrappedFloatTag create(float value) {
        return wrap(NBTTagFloat.a(value));
    }

    @Override
    public WrappedDoubleTag create(double value) {
        return wrap(NBTTagDouble.a(value));
    }

    @Override
    public WrappedStringTag create(String value) {
        return wrap(NBTTagString.a(value));
    }

    @Override
    public WrappedByteArrayTag create(byte[] values) {
        return wrap(new NBTTagByteArray(values));
    }

    @Override
    public WrappedIntArrayTag create(int[] values) {
        return wrap(new NBTTagIntArray(values));
    }

    @Override
    public WrappedLongArrayTag create(long[] values) {
        return wrap(new NBTTagLongArray(values));
    }

    @Override
    public WrappedListTag create(List<net.cocoonmc.core.nbt.Tag> values, int elementType) {
        return wrap(new NBTTagList());
    }

    @Override
    public WrappedCompoundTag create(Map<String, net.cocoonmc.core.nbt.Tag> values) {
        return wrap(new NBTTagCompound());
    }


    @Override
    public WrappedCompoundTag read(InputStream inputStream) throws IOException {
        return wrap(NBTCompressedStreamTools.a((DataInput) new DataInputStream(inputStream)));
    }

    @Override
    public void write(OutputStream outputStream, net.cocoonmc.core.nbt.CompoundTag tag) throws IOException {
        NBTCompressedStreamTools.a(unwrap(tag), (DataOutput) new DataOutputStream(outputStream));
    }

    @Override
    public WrappedCompoundTag readCompressed(InputStream inputStream) throws IOException {
        return wrap(NBTCompressedStreamTools.a(inputStream));
    }

    @Override
    public void writeCompressed(OutputStream outputStream, net.cocoonmc.core.nbt.CompoundTag tag) throws IOException {
        NBTCompressedStreamTools.a(unwrap(tag), outputStream);
    }

    @Override
    public WrappedCompoundTag parseTag(String tag) {
        try {
            return wrap(MojangsonParser.parse(tag));
        } catch (Exception e) {
            return create((Map<String, net.cocoonmc.core.nbt.Tag>) null);
        }
    }

    public static WrappedByteTag wrap(NBTTagByte tag) {
        return new WrappedByteTag(tag) {

            @Override
            public Number getAsNumber() {
                return tag.k();
            }
        };
    }

    public static WrappedShortTag wrap(NBTTagShort tag) {
        return new WrappedShortTag(tag) {

            @Override
            public Number getAsNumber() {
                return tag.k();
            }
        };
    }

    public static WrappedIntTag wrap(NBTTagInt tag) {
        return new WrappedIntTag(tag) {

            @Override
            public Number getAsNumber() {
                return tag.k();
            }
        };
    }

    public static WrappedLongTag wrap(NBTTagLong tag) {
        return new WrappedLongTag(tag) {

            @Override
            public Number getAsNumber() {
                return tag.k();
            }
        };
    }

    public static WrappedFloatTag wrap(NBTTagFloat tag) {
        return new WrappedFloatTag(tag) {

            @Override
            public Number getAsNumber() {
                return tag.k();
            }
        };
    }

    public static WrappedDoubleTag wrap(NBTTagDouble tag) {
        return new WrappedDoubleTag(tag) {

            @Override
            public Number getAsNumber() {
                return tag.k();
            }
        };
    }

    public static WrappedStringTag wrap(NBTTagString tag) {
        return new WrappedStringTag(tag) {

            @Override
            public String getAsString() {
                return tag.asString();
            }
        };
    }

    public static WrappedByteArrayTag wrap(NBTTagByteArray tag) {
        return new WrappedByteArrayTag(tag) {

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

    public static WrappedIntArrayTag wrap(NBTTagIntArray tag) {
        return new WrappedIntArrayTag(tag) {

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

    public static WrappedLongArrayTag wrap(NBTTagLongArray tag) {
        return new WrappedLongArrayTag(tag) {

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

    public static WrappedListTag wrap(NBTTagList tag) {
        return new WrappedListTag(tag) {

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

    public static WrappedCompoundTag wrap(NBTTagCompound tag) {
        return new WrappedCompoundTag(tag) {

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

    public static WrappedTag wrap(NBTBase tag) {
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
        return new WrappedTag(tag) {

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

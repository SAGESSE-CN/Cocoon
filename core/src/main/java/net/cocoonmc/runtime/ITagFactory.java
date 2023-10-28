package net.cocoonmc.runtime;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public interface ITagFactory {

    ByteTag create(byte value);

    ShortTag create(short value);

    IntTag create(int value);

    LongTag create(long value);

    FloatTag create(float value);

    DoubleTag create(double value);

    ByteArrayTag create(byte[] values);

    StringTag create(String value);

    ListTag create(List<Tag> values, int elementType);

    CompoundTag create(Map<String, Tag> values);

    IntArrayTag create(int[] values);

    LongArrayTag create(long[] values);


    void write(OutputStream outputStream, CompoundTag tag) throws IOException;

    CompoundTag read(InputStream inputStream) throws IOException;

    void writeCompressed(OutputStream outputStream, CompoundTag tag) throws IOException;

    CompoundTag readCompressed(InputStream inputStream) throws IOException;

    String toString(CompoundTag tag);

    CompoundTag fromString(String tag);
}

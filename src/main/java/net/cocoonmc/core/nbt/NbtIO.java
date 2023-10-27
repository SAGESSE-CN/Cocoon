package net.cocoonmc.core.nbt;

import net.cocoonmc.impl.Versions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface NbtIO {

    static CompoundTag read(InputStream inputStream) throws IOException {
        return Versions.TAG.read(inputStream);
    }

    static void write(CompoundTag tag, OutputStream outputStream) throws IOException {
        Versions.TAG.write(outputStream, tag);
    }

    static CompoundTag readCompressed(InputStream inputStream) throws IOException {
        return Versions.TAG.readCompressed(inputStream);
    }

    static void writeCompressed(CompoundTag tag, OutputStream outputStream) throws IOException {
        Versions.TAG.writeCompressed(outputStream, tag);
    }

    static CompoundTag fromString(String value) throws IOException {
        return Versions.TAG.fromString(value);
    }

    static String toString(CompoundTag tag) throws IOException {
        return Versions.TAG.toString(tag);
    }

}

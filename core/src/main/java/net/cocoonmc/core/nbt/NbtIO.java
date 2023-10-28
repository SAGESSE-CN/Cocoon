package net.cocoonmc.core.nbt;

import net.cocoonmc.Cocoon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface NbtIO {

    static CompoundTag read(InputStream inputStream) throws IOException {
        return Cocoon.API.TAG.read(inputStream);
    }

    static void write(CompoundTag tag, OutputStream outputStream) throws IOException {
        Cocoon.API.TAG.write(outputStream, tag);
    }

    static CompoundTag readCompressed(InputStream inputStream) throws IOException {
        return Cocoon.API.TAG.readCompressed(inputStream);
    }

    static void writeCompressed(CompoundTag tag, OutputStream outputStream) throws IOException {
        Cocoon.API.TAG.writeCompressed(outputStream, tag);
    }

    static CompoundTag fromString(String value) throws IOException {
        return Cocoon.API.TAG.fromString(value);
    }

    static String toString(CompoundTag tag) throws IOException {
        return Cocoon.API.TAG.toString(tag);
    }

}

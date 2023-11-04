package net.cocoonmc.core.nbt;

import net.cocoonmc.Cocoon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface NbtIo {

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
}

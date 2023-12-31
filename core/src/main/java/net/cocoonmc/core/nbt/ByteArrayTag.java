package net.cocoonmc.core.nbt;

import net.cocoonmc.Cocoon;

import java.util.List;

@SuppressWarnings("unused")
public interface ByteArrayTag extends CollectionTag<ByteTag> {

    static ByteArrayTag valueOf(byte[] values) {
        return Cocoon.API.TAG.create(values);
    }

    static ByteArrayTag valueOf(List<Byte> values) {
        byte[] results = new byte[values.size()];
        for (int i = 0; i < results.length; ++i) {
            results[i] = values.get(i);
        }
        return Cocoon.API.TAG.create(results);
    }

    @Override
    default byte getType() {
        return 7;
    }

    @Override
    default byte getElementType() {
        return 1;
    }

    byte[] getAsByteArray();
}

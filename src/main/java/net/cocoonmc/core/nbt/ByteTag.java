package net.cocoonmc.core.nbt;

import net.cocoonmc.impl.Versions;

@SuppressWarnings("unused")
public interface ByteTag extends NumericTag {

    static ByteTag valueOf(boolean value) {
        return valueOf(value ? (byte) 1 : (byte) 0);
    }

    static ByteTag valueOf(byte value) {
        return Versions.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 1;
    }
}

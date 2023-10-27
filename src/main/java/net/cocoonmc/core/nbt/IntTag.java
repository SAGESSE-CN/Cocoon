package net.cocoonmc.core.nbt;

import net.cocoonmc.impl.Versions;

@SuppressWarnings("unused")
public interface IntTag extends NumericTag {

    static IntTag valueOf(int value) {
        return Versions.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 3;
    }
}

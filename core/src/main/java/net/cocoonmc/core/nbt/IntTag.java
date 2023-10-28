package net.cocoonmc.core.nbt;

import net.cocoonmc.Cocoon;

@SuppressWarnings("unused")
public interface IntTag extends NumericTag {

    static IntTag valueOf(int value) {
        return Cocoon.API.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 3;
    }
}

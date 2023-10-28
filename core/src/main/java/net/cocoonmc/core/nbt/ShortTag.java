package net.cocoonmc.core.nbt;

import net.cocoonmc.Cocoon;

@SuppressWarnings("unused")
public interface ShortTag extends NumericTag {

    static ShortTag valueOf(short value) {
        return Cocoon.API.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 2;
    }
}

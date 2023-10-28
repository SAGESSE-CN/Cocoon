package net.cocoonmc.core.nbt;

import net.cocoonmc.Cocoon;

@SuppressWarnings("unused")
public interface LongTag extends NumericTag {

    static LongTag valueOf(long value) {
        return Cocoon.API.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 4;
    }
}

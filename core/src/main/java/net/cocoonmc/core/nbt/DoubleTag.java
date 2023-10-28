package net.cocoonmc.core.nbt;

import net.cocoonmc.Cocoon;

@SuppressWarnings("unused")
public interface DoubleTag extends NumericTag {

    static DoubleTag valueOf(double value) {
        return Cocoon.API.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 6;
    }
}

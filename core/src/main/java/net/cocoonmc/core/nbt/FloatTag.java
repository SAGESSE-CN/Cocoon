package net.cocoonmc.core.nbt;

import net.cocoonmc.Cocoon;

@SuppressWarnings("unused")
public interface FloatTag extends NumericTag {

    static FloatTag valueOf(float value) {
        return Cocoon.API.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 5;
    }
}

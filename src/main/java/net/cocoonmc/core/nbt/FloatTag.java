package net.cocoonmc.core.nbt;

import net.cocoonmc.impl.Versions;

@SuppressWarnings("unused")
public interface FloatTag extends NumericTag {

    static FloatTag valueOf(float value) {
        return Versions.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 5;
    }
}

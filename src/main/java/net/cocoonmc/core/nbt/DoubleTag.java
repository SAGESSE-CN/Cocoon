package net.cocoonmc.core.nbt;

import net.cocoonmc.impl.Versions;

@SuppressWarnings("unused")
public interface DoubleTag extends NumericTag {

    static DoubleTag valueOf(double value) {
        return Versions.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 6;
    }
}

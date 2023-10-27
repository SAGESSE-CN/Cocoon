package net.cocoonmc.core.nbt;

import net.cocoonmc.impl.Versions;

@SuppressWarnings("unused")
public interface LongTag extends NumericTag {

    static LongTag valueOf(long value) {
        return Versions.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 4;
    }
}

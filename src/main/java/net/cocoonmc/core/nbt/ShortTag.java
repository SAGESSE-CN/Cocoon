package net.cocoonmc.core.nbt;

import net.cocoonmc.impl.Versions;

@SuppressWarnings("unused")
public interface ShortTag extends NumericTag {

    static ShortTag valueOf(short value) {
        return Versions.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 2;
    }
}

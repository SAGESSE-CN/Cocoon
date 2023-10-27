package net.cocoonmc.core.nbt;

import net.cocoonmc.impl.Versions;

@SuppressWarnings("unused")
public interface StringTag extends Tag {

    static StringTag valueOf(String value) {
        return Versions.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 8;
    }

    String getAsString();
}

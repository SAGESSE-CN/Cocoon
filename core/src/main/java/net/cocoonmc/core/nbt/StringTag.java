package net.cocoonmc.core.nbt;

import net.cocoonmc.Cocoon;

@SuppressWarnings("unused")
public interface StringTag extends Tag {

    static StringTag valueOf(String value) {
        return Cocoon.API.TAG.create(value);
    }

    @Override
    default byte getType() {
        return 8;
    }

    String getAsString();
}

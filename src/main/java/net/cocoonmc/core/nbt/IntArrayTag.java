package net.cocoonmc.core.nbt;

import net.cocoonmc.impl.Versions;

import java.util.List;

@SuppressWarnings("unused")
public interface IntArrayTag extends CollectionTag<IntTag> {

    static IntArrayTag valueOf(int[] values) {
        return Versions.TAG.create(values);
    }

    static IntArrayTag valueOf(List<Integer> values) {
        int[] results = new int[values.size()];
        for (int i = 0; i < results.length; ++i) {
            results[i] = values.get(i);
        }
        return Versions.TAG.create(results);
    }

    @Override
    default byte getType() {
        return 11;
    }

    @Override
    default byte getElementType() {
        return 3;
    }

    int[] getAsIntArray();
}

package net.cocoonmc.core.nbt;

import net.cocoonmc.impl.Versions;

import java.util.List;

@SuppressWarnings("unused")
public interface LongArrayTag extends CollectionTag<LongTag> {

    static LongArrayTag valueOf(long[] values) {
        return Versions.TAG.create(values);
    }

    static LongArrayTag valueOf(List<Long> values) {
        long[] results = new long[values.size()];
        for (int i = 0; i < results.length; ++i) {
            results[i] = values.get(i);
        }
        return Versions.TAG.create(results);
    }

    @Override
    default byte getType() {
        return 12;
    }

    @Override
    default byte getElementType() {
        return 4;
    }

    long[] getAsLongArray();
}

package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.nbt.CompoundTag;

public interface ItemStackAccessor {

    void setCount(int count);

    CompoundTag getTag();

    void setTag(CompoundTag tag);
}

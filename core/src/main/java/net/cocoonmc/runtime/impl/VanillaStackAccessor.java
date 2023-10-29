package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.nbt.CompoundTag;

public interface VanillaStackAccessor {

    CompoundTag getTag();

    void setTag(CompoundTag tag);
}

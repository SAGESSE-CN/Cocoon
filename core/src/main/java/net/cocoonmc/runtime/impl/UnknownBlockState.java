package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.resources.ResourceLocation;

public class UnknownBlockState {

    private final ResourceLocation registryName;
    private final CompoundTag stateTag;
    private final CompoundTag entityTag;

    public UnknownBlockState(ResourceLocation registryName, CompoundTag stateTag, CompoundTag entityTag) {
        this.registryName = registryName;
        this.stateTag = stateTag;
        this.entityTag = entityTag;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public CompoundTag getState() {
        return stateTag;
    }

    public CompoundTag getTag() {
        return entityTag;
    }
}

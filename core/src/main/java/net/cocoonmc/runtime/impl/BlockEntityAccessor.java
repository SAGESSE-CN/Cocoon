package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockState;

public interface BlockEntityAccessor {

    void setChanged();

    void sendBlockUpdated(BlockPos pos, BlockState oldBlockState, BlockState newBlockState, int flags);
}

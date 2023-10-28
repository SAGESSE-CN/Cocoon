package net.cocoonmc.runtime.forge.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface IOriginalLevelChunk {

    void setOriginalBlockState(BlockPos pos, BlockState blockState);

    BlockState getOriginalBlockState(BlockPos pos);
}

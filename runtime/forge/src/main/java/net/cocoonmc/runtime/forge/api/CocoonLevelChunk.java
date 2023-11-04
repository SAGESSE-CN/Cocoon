package net.cocoonmc.runtime.forge.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface CocoonLevelChunk {

    void cocoon$setOriginalBlockState(BlockPos pos, BlockState blockState);

    BlockState cocoon$getOriginalBlockState(BlockPos pos);
}

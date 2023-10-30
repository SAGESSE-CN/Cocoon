package net.cocoonmc.runtime;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.runtime.impl.BlockData;
import org.jetbrains.annotations.Nullable;

public interface IBlockFactory {

    @Nullable
    BlockData getBlockData(Level level, BlockPos blockPos);

    void setBlockData(Level level, BlockPos blockPos, @Nullable BlockData blockData);
}

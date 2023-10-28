package net.cocoonmc.core.block;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.nbt.CompoundTag;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public class BlockEntity {

    protected BlockState blockState;
    protected CompoundTag tag;

    public BlockEntity(World world, BlockPos pos, BlockState blockState, @Nullable CompoundTag tag) {
        this.blockState = blockState;
        this.tag = tag;
    }

    public Block getBlock() {
        return blockState.getBlock();
    }

    public BlockState getBlockState() {
        return blockState;
    }
}

package net.cocoonmc.core.block;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.SimpleAssociatedStorage;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.runtime.IAssociatedContainer;
import net.cocoonmc.runtime.IAssociatedContainerProvider;

@SuppressWarnings("unused")
public class BlockEntity implements IAssociatedContainerProvider {

    protected BlockState blockState;

    protected final Level level;
    protected final BlockPos blockPos;

    private final SimpleAssociatedStorage storage = new SimpleAssociatedStorage();


    public BlockEntity(Level level, BlockPos pos, BlockState blockState) {
        this.level = level;
        this.blockPos = pos;
        this.blockState = blockState;
    }

    public void readFromNBT(CompoundTag tag) {
    }

    public void writeToNBT(CompoundTag tag) {
    }

    public void setChanged() {
        level.setBlockEntityChanged(blockPos);
    }

    public Block getBlock() {
        return blockState.getBlock();
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public IAssociatedContainer getAssociatedContainer() {
        return storage;
    }


}

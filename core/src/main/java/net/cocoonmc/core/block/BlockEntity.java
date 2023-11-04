package net.cocoonmc.core.block;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.SimpleAssociatedStorage;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.runtime.IAssociatedContainer;
import net.cocoonmc.runtime.IAssociatedContainerProvider;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class BlockEntity implements IAssociatedContainerProvider {

    protected Level level;
    protected BlockState blockState;

    protected final BlockPos blockPos;
    protected final BlockEntityType<?> type;

    private final SimpleAssociatedStorage storage = new SimpleAssociatedStorage();

    public BlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        this.type = type;
        this.blockPos = pos;
        this.blockState = blockState;
    }

    public void readFromNBT(CompoundTag tag) {
    }

    public void writeToNBT(CompoundTag tag) {
    }

    public void setChanged() {
        if (level != null) {
            level.setBlockEntityChanged(blockPos, blockState, 0);
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = CompoundTag.newInstance();
        writeToNBT(tag);
        return tag;
    }

    @Nullable
    public CompoundTag getUpdateTag() {
        return null;
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

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public BlockEntityType<?> getType() {
        return type;
    }

    @Override
    public IAssociatedContainer getAssociatedContainer() {
        return storage;
    }


}

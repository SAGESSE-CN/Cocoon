package net.cocoonmc.core.block;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.SimpleAssociatedStorage;
import net.cocoonmc.runtime.IAssociatedContainer;
import net.cocoonmc.runtime.IAssociatedContainerProvider;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public class BlockEntity implements IAssociatedContainerProvider {

    protected BlockState blockState;
    protected CompoundTag tag;

    private final SimpleAssociatedStorage storage = new SimpleAssociatedStorage();


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

    @Override
    public IAssociatedContainer getAssociatedContainer() {
        return storage;
    }
}

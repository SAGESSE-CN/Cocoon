package net.cocoonmc.runtime.impl;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.world.Level;
import org.bukkit.Bukkit;

public class BlockData {

    private Level level;
    private BlockPos blockPos;

    private Block block;
    private BlockState blockState;
    private BlockEntity blockEntity;

    private CompoundTag blockTag;

    private Block lastBlock;
    private BlockState lastBlockState;
    private CompoundTag lastBlockTag;
    private BlockEntityAccessor lasBlockAccessor;
    private int lasStateUpdateFlags = 3;

    private boolean isTagChangeMark = false;
    private boolean isStateChangeMark = false;

    private boolean isSending = false;

    public BlockData(Level level, BlockPos blockPos, Block block, BlockState blockState, BlockEntity blockEntity) {
        this.level = level;
        this.blockPos = blockPos;
        this.block = block;
        this.blockState = blockState;
        this.blockEntity = blockEntity;
        this.clear();
    }


    public void setChanged() {
        this.isTagChangeMark = true;
        this.sendToBukkit();
    }

    public void sendBlockUpdated(BlockState oldState, BlockState newState, int flags) {
        // TODO: oldState => newState
        this.isStateChangeMark = true;
        this.lasStateUpdateFlags = flags;
        this.sendToBukkit();
    }

    public void setBlockTag(CompoundTag tag) {
        this.blockTag = tag;
        this.lastBlockTag = tag;
        if (this.blockEntity != null && tag != null) {
            this.blockEntity.readFromNBT(tag);
        }
    }

    public void setBlockState(BlockState blockState, int flags) {
        this.isStateChangeMark = true;
        this.lasStateUpdateFlags |= flags;
        this.blockState = blockState;
        if (this.blockEntity != null) {
            this.blockEntity.setBlockState(blockState);
        }
        this.sendToBukkit();
    }

    public void setBlockEntity(BlockEntityAccessor accessor) {
        this.lasBlockAccessor = accessor;
    }


    public Block getBlock() {
        return block;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public CompoundTag getBlockTag() {
        return blockTag;
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    private void sendToBukkit() {
        if (isSending) {
            return;
        }
        isSending = true;
        Bukkit.getScheduler().runTaskLater(Cocoon.getInstance().getPlugin(), () -> {
            isSending = false;
            copyToBukkit();
            clear();
        }, 1);
    }

    private void copyToBukkit() {
        boolean isStateChanged = false;
        boolean isTagChanged = false;
        if (isStateChangeMark) {
            // check the block state has actually changed.
            if (!blockState.equals(lastBlockState)) {
                lasStateUpdateFlags |= 3;
                isStateChanged = true;
            }
            lastBlockState = blockState;
        }
        if (isTagChangeMark && blockEntity != null) {
            // check the block tag has actually changed.
            CompoundTag tag = CompoundTag.newInstance();
            blockEntity.writeToNBT(tag);
            if (!tag.equals(lastBlockTag)) {
                isTagChanged = true;
            }
            blockTag = tag;
            lastBlockTag = tag;
        }
        // only when is a real changes, we need to update the block data to level.
        if (isStateChanged || isTagChanged) {
            Cocoon.API.BLOCK.setBlockData(level, blockPos, this);
        }
        isTagChanged |= isStateChangeMark;
        if (isTagChanged && lasBlockAccessor != null) {
            lasBlockAccessor.setChanged();
        }
        isStateChanged |= isStateChangeMark;
        if (isStateChanged && lasBlockAccessor != null) {
            lasBlockAccessor.sendBlockUpdated(blockPos, blockState, blockState, lasStateUpdateFlags);
        }
        clear();
    }

    private void clear() {
        lastBlockState = blockState;
        lastBlock = block;
        isTagChangeMark = false;
        isStateChangeMark = false;
        lasStateUpdateFlags = 0;
    }
}

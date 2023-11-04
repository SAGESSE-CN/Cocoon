package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;

public class ChunkDataPlaceTask {

    private static final Stack<ChunkDataPlaceTask> STACK = new Stack<>();

    private final Block wrapper;
    private final BlockState blockState;
    private final CompoundTag entityTag;

    public ChunkDataPlaceTask(Block wrapper, BlockState blockState, @Nullable CompoundTag entityTag) {
        this.wrapper = wrapper;
        this.blockState = blockState;
        this.entityTag = entityTag;
    }

    @Nullable
    public static ChunkDataPlaceTask last() {
        if (!STACK.isEmpty()) {
            return STACK.lastElement();
        }
        return null;
    }

    public static void push(Block wrapper, BlockState blockState, @Nullable CompoundTag entityTag) {
        STACK.push(new ChunkDataPlaceTask(wrapper, blockState, entityTag));
    }

    public static void pop(Block wrapper) {
        STACK.pop();
    }

    public Block getBlock() {
        return blockState.getBlock();
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public CompoundTag getEntityTag() {
        return entityTag;
    }

    public Block getWrapper() {
        return wrapper;
    }
}

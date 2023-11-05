package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.item.context.BlockPlaceContext;
import net.cocoonmc.core.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;

public class ChunkDataPlaceTask {

    private static final Stack<ChunkDataPlaceTask> STACK = new Stack<>();

    private final Block wrapper;
    private final BlockState blockState;
    private final CompoundTag entityTag;
    private final BlockPlaceContext context;

    public ChunkDataPlaceTask(Block wrapper, BlockState blockState, @Nullable CompoundTag entityTag, BlockPlaceContext context) {
        this.wrapper = wrapper;
        this.blockState = blockState;
        this.entityTag = entityTag;
        this.context = context;
    }

    @Nullable
    public static ChunkDataPlaceTask last() {
        if (!STACK.isEmpty()) {
            return STACK.lastElement();
        }
        return null;
    }

    public static void push(Block wrapper, BlockState blockState, @Nullable CompoundTag entityTag, BlockPlaceContext context) {
        STACK.push(new ChunkDataPlaceTask(wrapper, blockState, entityTag, context));
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

    public Block getDelegate() {
        return wrapper;
    }

    public BlockPlaceContext getContext() {
        return context;
    }
}

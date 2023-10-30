package net.cocoonmc.core.world;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.runtime.impl.BlockData;
import net.cocoonmc.runtime.impl.CacheKeys;
import org.jetbrains.annotations.Nullable;

public class Level {

    private final org.bukkit.World world;

    public Level(org.bukkit.World world) {
        this.world = world;
    }

    public static Level of(org.bukkit.World world) {
        return Cocoon.API.CACHE.computeIfAbsent(world, CacheKeys.LEVEL_KEY, Level::new);
    }

    public static Level of(org.bukkit.block.Block block) {
        return of(block.getWorld());
    }

    public static Level of(org.bukkit.block.TileState blockState) {
        return of(blockState.getWorld());
    }


    public void sendBlockUpdated(BlockPos pos, BlockState oldState, BlockState newState, int flags) {
        BlockData blockData = Cocoon.API.BLOCK.getBlockData(this, pos);
        if (blockData != null) {
            blockData.sendBlockUpdated(oldState, newState, flags);
        }
    }

    public void setBlockEntityChanged(BlockPos pos) {
        BlockData blockData = Cocoon.API.BLOCK.getBlockData(this, pos);
        if (blockData != null) {
            blockData.setChanged();
        }
    }

    public void setBlock(BlockPos pos, BlockState state, int flags) {
        BlockData blockData = Cocoon.API.BLOCK.getBlockData(this, pos);
        if (blockData != null) {
            blockData.setBlockState(state, flags);
        }
    }

    @Nullable
    public BlockState getBlockState(BlockPos pos) {
        BlockData blockData = Cocoon.API.BLOCK.getBlockData(this, pos);
        if (blockData != null) {
            return blockData.getBlockState();
        }
        return null;
    }

    @Nullable
    public BlockEntity getBlockEntity(BlockPos pos) {
        BlockData blockData = Cocoon.API.BLOCK.getBlockData(this, pos);
        if (blockData != null) {
            return blockData.getBlockEntity();
        }
        return null;
    }

    @Nullable
    public Entity getEntity(int entityId) {
        for (org.bukkit.entity.Entity entity : world.getEntities()) {
            if (entity.getEntityId() == entityId) {
                return Entity.of(entity);
            }
        }
        return null;
    }

    public boolean hasNeighborSignal(BlockPos pos) {
        return world.getBlockAt(pos.getX(), pos.getY(), pos.getZ()).getBlockPower() > 0;
    }

    public org.bukkit.World asBukkit() {
        return world;
    }
}

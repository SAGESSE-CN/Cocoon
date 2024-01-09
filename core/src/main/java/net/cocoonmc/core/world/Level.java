package net.cocoonmc.core.world;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.utils.ObjectHelper;
import net.cocoonmc.core.world.chunk.Chunk;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.runtime.impl.EntityPlaceTask;
import net.cocoonmc.runtime.impl.LevelData;
import net.cocoonmc.runtime.impl.Logs;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class Level {

    private final org.bukkit.World world;
    private final ConcurrentHashMap<Long, Chunk> chunks = new ConcurrentHashMap<>();

    private final UUID uuid;
    private final String name;

    public Level(org.bukkit.World world) {
        this.world = world;
        this.uuid = world.getUID();
        this.name = world.getName();
    }

    public static Level of(org.bukkit.World world) {
        return LevelData.get(world);
    }

    public static Level of(org.bukkit.block.Block block) {
        return of(block.getWorld());
    }

    public static Level of(org.bukkit.block.TileState blockState) {
        return of(blockState.getWorld());
    }

    public void save() {
        int chunkCounter = 0;
        for (Chunk chunk : chunks.values()) {
            if (chunk.isDirty()) {
                chunk.save();
                chunkCounter += 1;
            }
        }
        Logs.debug("{} save {} chunks", getName(), chunkCounter);
    }

    public void addFreshEntity(Entity entity) {
        org.bukkit.entity.Entity entity1 = entity.asBukkit();
        org.bukkit.entity.EntityType entityType1 = entity.getType().asBukkit();
        if (entity1 != null || entityType1 == null) {
            return;
        }
        EntityPlaceTask.push(this, entity);
        world.spawnEntity(entity.getPosition().asBukkit(), entityType1);
        EntityPlaceTask.pop(this, entity);
    }

    public void removeEntity(Entity entity) {
    }

    public void sendBlockUpdated(BlockPos pos, BlockState oldState, BlockState newState, int flags) {
        Chunk chunk = getChunkAt(pos);
        chunk.setBlockDirty(pos, newState, flags);
    }

    public void setBlockEntityChanged(BlockPos pos, BlockState state, int flags) {
        Chunk chunk = getChunkAt(pos);
        chunk.setBlockDirty(pos, state, flags);
    }

    public void setBlock(BlockPos pos, BlockState state, int flags) {
        Chunk chunk = getChunkAt(pos);
        chunk.setBlock(pos, state);
        setBukkitBlockIfNeeded(pos, state);
        setDirty();
    }

    @Nullable
    public BlockState getBlockState(BlockPos pos) {
        return getChunkAt(pos).getBlockState(pos);
    }

    @Nullable
    public BlockEntity getBlockEntity(BlockPos pos) {
        return getChunkAt(pos).getBlockEntity(pos);
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

    public void updateNeighborsAt(BlockPos pos, Block block) {
        LevelData.updateNeighbourShapes(this, pos, block, 0);
    }

    public boolean hasNeighborSignal(BlockPos pos) {
        return world.getBlockAt(pos.getX(), pos.getY(), pos.getZ()).getBlockPower() > 0;
    }

    public void setDirty() {
    }

    public Chunk getChunkAt(BlockPos blockPos) {
        return getChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4);
    }

    public Chunk getChunk(int x, int z) {
        long index = (long) x << 32 | (long) z;
        Chunk chunk = chunks.get(index);
        if (chunk != null) {
            return chunk;
        }
        // when calling getChunkAt the ChunkLoadEvent maybe fired,
        // so we must to attention the reentrancy of the method.
        org.bukkit.Chunk chunk1 = world.getChunkAt(x, z);
        return chunks.computeIfAbsent(index, it -> new Chunk(this, chunk1));
    }

    public String getName() {
        return world.getName();
    }

    public UUID getUUID() {
        return uuid;
    }

    public org.bukkit.World asBukkit() {
        return world;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Level)) return false;
        Level level = (Level) o;
        return Objects.equals(uuid, level.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return ObjectHelper.makeDescription(this, "uuid", getUUID(), "name", name);
    }


    private void setBukkitBlockIfNeeded(BlockPos pos, BlockState state) {
        Block delegate = state.getBlock().getDelegate();
        org.bukkit.Material material = delegate.asBukkit();
        org.bukkit.block.Block target = world.getBlockAt(pos.asBukkit());
        if (material != null && !target.getType().equals(material)) {
            LevelData.commit(() -> target.setType(material));
        }
    }
}

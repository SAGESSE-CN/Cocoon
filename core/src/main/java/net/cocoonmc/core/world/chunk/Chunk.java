package net.cocoonmc.core.world.chunk;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockEntitySupplier;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.Blocks;
import net.cocoonmc.core.math.CollissionBox;
import net.cocoonmc.core.math.VoxelShape;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.utils.ObjectHelper;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.runtime.impl.ConstantKeys;
import net.cocoonmc.runtime.impl.Constants;
import net.cocoonmc.runtime.impl.LevelData;
import net.cocoonmc.runtime.impl.Logs;
import net.cocoonmc.runtime.impl.TagPersistentData;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Chunk {

    private final org.bukkit.Chunk chunk;
    private final org.bukkit.persistence.PersistentDataContainer container;

    private final Level level;
    private final ChunkPos key;
    private final ConcurrentHashMap<BlockPos, BlockState> allStates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<BlockPos, BlockEntity> allEntities = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<BlockPos, CompoundTag> allUpdateTags = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<BlockPos, VoxelShape> allCollissionShaps = new ConcurrentHashMap<>();

    private boolean isSaved = false;
    private boolean isLoaded = false;
    private boolean isDirty = false;

    public Chunk(Level level, org.bukkit.Chunk chunk) {
        this.key = new ChunkPos(level.getUUID(), chunk.getX(), chunk.getZ());
        this.level = level;
        this.container = chunk.getPersistentDataContainer();
        this.chunk = chunk;
    }

    public static Chunk of(org.bukkit.Chunk chunk) {
        Level level = Level.of(chunk.getWorld());
        return level.getChunk(chunk.getX(), chunk.getZ());
    }

    public static Chunk of(org.bukkit.block.Block block) {
        return of(block.getChunk());
    }

    public void load() {
        if (!isLoaded) {
            loadAllBlocks();
            loadAllEntities();
            generateClientBlockTags();
        }
    }

    public void save() {
        saveAllBlocks();
    }

    public void freeze() {
        generateClientBlockTags();
    }

    public void unload() {
        if (isDirty) {
            save();
        }
    }

    public void setBlock(BlockPos pos, BlockState newState) {
        setBlock(pos, newState, null);
    }

    public void setBlock(BlockPos pos, BlockState newState, @Nullable CompoundTag entityTag) {
        //Logs.debug("{} => {}", pos, newState);
        BlockState oldState = allStates.getOrDefault(pos, Blocks.AIR.defaultBlockState());
        // when a block is changes, notify the user.
        if (!oldState.is(newState.getBlock())) {
            oldState.onRemove(level, pos, newState, false);
        }
        if (newState.is(Blocks.AIR)) {
            allStates.remove(pos);
            checkBlockEntity(pos, oldState, newState, null);
        } else {
            allStates.put(pos, newState);
            checkBlockEntity(pos, oldState, newState, entityTag);
        }
        // when a block is changes, notify the user.
        if (!newState.is(oldState.getBlock())) {
            newState.onPlace(level, pos, oldState, false);
        }
        setBlockDirty(pos, newState, 0);
    }

    public void setBlockDirty(BlockPos pos, BlockState state, int flags) {
        setDirty();
        allUpdateTags.remove(pos);
        allCollissionShaps.remove(pos);
        LevelData.updateStates(this, pos);
    }

    @Nullable
    public BlockState getBlockState(BlockPos blockPos) {
        return allStates.get(blockPos);
    }

    @Nullable
    public BlockEntity getBlockEntity(BlockPos blockPos) {
        return allEntities.get(blockPos);
    }

    public int getX() {
        return key.getX();
    }

    public int getZ() {
        return key.getZ();
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chunk)) return false;
        Chunk chunk = (Chunk) o;
        return Objects.equals(key, chunk.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return ObjectHelper.makeDescription(this, "x", key.getX(), "z", key.getZ());
    }


    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty() {
        isDirty = true;
    }


    private void loadAllEntities() {
        int entityCounter = 0;
        for (Entity entity : Arrays.stream(chunk.getEntities()).filter(BukkitHelper::hasCustomEntityType).map(Entity::of).collect(Collectors.toList())) {
            entityCounter += 1;
        }
        if (entityCounter != 0) {
            Logs.debug("{} load entities: {}", this, entityCounter);
        }
    }

    private void loadAllBlocks() {
        allStates.clear();
        allEntities.clear();
        allUpdateTags.clear();
        isLoaded = true;
        FriendlyByteBuf buf = container.get(ConstantKeys.CACHE_KEY, TagPersistentData.DEFAULT);
        if (buf != null) {
            load(buf);
            isSaved = true;
            Logs.debug("{} load blocks: {}", this, allStates.size());
        }
    }

    private void saveAllBlocks() {
        isDirty = false;
        if (allStates.isEmpty()) {
            if (isSaved) {
                Logs.debug("{} clear", this);
                container.remove(ConstantKeys.CACHE_KEY);
                isSaved = false;
            }
            return;
        }
        FriendlyByteBuf buf = new FriendlyByteBuf();
        save(buf);
        container.set(ConstantKeys.CACHE_KEY, TagPersistentData.DEFAULT, buf);
        Logs.debug("{} save blocks: {}", this, allStates.size());
        isSaved = true;
    }

    private void load(FriendlyByteBuf buf) {
        int size = buf.readInt();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = buf.readBlockPos();
            Block block = Block.byKey(buf.readResourceLocation());
            BlockState state = block.defaultBlockState().deserialize(buf.readNbt());
            CompoundTag entityTag = buf.readNbt();
            allStates.put(pos, state);
            BlockEntity blockEntity = createBlockEntity(pos, state);
            if (blockEntity != null) {
                allEntities.put(pos, blockEntity);
                allUpdateTags.remove(pos);
                allCollissionShaps.remove(pos);
                blockEntity.setLevel(level);
                if (entityTag != null) {
                    blockEntity.readFromNBT(entityTag);
                }
            }
        }
    }

    private void save(FriendlyByteBuf buf) {
        buf.writeInt(allStates.size());
        allStates.forEach((pos, state) -> {
            Block block = state.getBlock();
            CompoundTag entityTag = generateEntityFullTag(pos);
            buf.writeBlockPos(pos);
            buf.writeResourceLocation(block.getRegistryName());
            buf.writeNbt(state.serialize());
            buf.writeNbt(entityTag);
        });
    }

    private void checkBlockEntity(BlockPos pos, BlockState oldState, BlockState newState, @Nullable CompoundTag entityTag) {
        BlockEntity oldEntity = allEntities.get(pos);
        if (oldEntity != null) {
            // aha, if the block said it valid, we can reuse it.
            if (oldEntity.getType().isValid(newState)) {
                oldEntity.setBlockState(newState);
                return;
            }
            removeBlockEntity(pos, oldEntity);
        }
        if (newState.hasBlockEntity()) {
            // create a new block if block needs;
            BlockEntity newEntity = createBlockEntity(pos, newState);
            if (newEntity == null) {
                return;
            }
            allEntities.put(pos, newEntity);
            allUpdateTags.remove(pos);
            newEntity.setBlockState(newState);
            newEntity.setLevel(level);
            if (entityTag != null) {
                newEntity.readFromNBT(entityTag);
            }
        }
    }


    private void removeBlockEntity(BlockPos pos, BlockEntity entity) {
        entity.setLevel(null);
        allEntities.remove(pos);
        allUpdateTags.remove(pos);
    }

    @Nullable
    private BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        if (block instanceof BlockEntitySupplier) {
            return ((BlockEntitySupplier) block).newBlockEntity(pos, state);
        }
        return null;
    }

    private void generateClientBlockTags() {
        allStates.forEach((pos, state) -> {
            if (!allUpdateTags.containsKey(pos)) {
                CompoundTag tag = generateClientBlockTag(pos, state);
                //Logs.debug("{} generate block {} => {}", level.getName(), pos, tag);
                allUpdateTags.put(pos, tag);
            }
            if (!allCollissionShaps.containsKey(pos)) {
                VoxelShape shape = state.getCollisionShape(level, pos);
                //Logs.debug("{} generate collission shape {} => {}", level.getName(), pos, shape);
                allCollissionShaps.put(pos, shape);
            }
        });
        LevelData.updateClientChunk(key, allUpdateTags, allCollissionShaps);
    }

    private CompoundTag generateClientBlockTag(BlockPos pos, BlockState state) {
        CompoundTag tag = CompoundTag.newInstance();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        tag.putString(Constants.BLOCK_REDIRECTED_ID_KEY, state.getBlock().getRegistryName().toString());
        CompoundTag stateTag = state.serialize();
        if (stateTag.size() != 0) {
            tag.put(Constants.BLOCK_REDIRECTED_STATE_KEY, stateTag);
        }
        CompoundTag entityTag = generateEntityUpdateTag(pos);
        if (entityTag != null) {
            tag.put(Constants.BLOCK_REDIRECTED_TAG_KEY, entityTag);
        }
        return tag;
    }

    @Nullable
    private CompoundTag generateEntityFullTag(BlockPos pos) {
        BlockEntity entity = allEntities.get(pos);
        if (entity != null) {
            CompoundTag tag = CompoundTag.newInstance();
            entity.writeToNBT(tag);
            return tag;
        }
        return null;
    }

    @Nullable
    private CompoundTag generateEntityUpdateTag(BlockPos pos) {
        BlockEntity entity = allEntities.get(pos);
        if (entity != null) {
            return entity.getUpdateTag();
        }
        return null;
    }

    @Nullable
    private CollissionBox generateCollission(BlockPos pos) {
        return null;
    }
}

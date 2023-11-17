package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.math.CollissionBox;
import net.cocoonmc.core.math.Cursor3D;
import net.cocoonmc.core.math.Vector3d;
import net.cocoonmc.core.math.VoxelShape;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.ListTag;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.network.syncher.SynchedEntityData;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.utils.MathHelper;
import net.cocoonmc.core.utils.PacketHelper;
import net.cocoonmc.core.utils.Pair;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.chunk.Chunk;
import net.cocoonmc.core.world.chunk.ChunkPos;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.EntityType;
import net.cocoonmc.core.world.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LevelData {

    private static ConcurrentHashMap<UUID, Level> LEVELS = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<ChunkPos, ClientInfo> CLIENT_INFOS = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Pair<UUID, Integer>, CompoundTag> CLIENT_ENTITIES = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Pair<UUID, Integer>, FriendlyByteBuf> CLIENT_ENTITIE_DATAS = new ConcurrentHashMap<>();

    private static boolean DISABLE_SAVE_ENTITY_TAG = false;

    private static UpdateBatchTask UPDATE_BATCH_TASK;

    public static Level get(org.bukkit.World world) {
        return LevelData.LEVELS.computeIfAbsent(world.getUID(), it -> new Level(world));
    }

    public static void open() {
        //Bukkit.getServer().getWorlds().forEach(it -> Level.of(it).load());
    }

    public static void close() {
        LEVELS.forEach((uid, level) -> level.save());
        LEVELS.clear();
    }

    public static void loadEntityTag(Entity entity) {
        CompoundTag entityTag = BukkitHelper.readCustomEntityTag(entity.asBukkit());
        if (entityTag != null) {
            DISABLE_SAVE_ENTITY_TAG = true;
            //Logs.debug("{} load entity tag: {}", entity.getId(), entityTag);
            entity.readAdditionalSaveData(entityTag);
            DISABLE_SAVE_ENTITY_TAG = false;
        }
    }

    public static void updateEntityTag(Entity entity) {
        loop().entityTask.add(entity, !DISABLE_SAVE_ENTITY_TAG);
    }

    public static void updateStates(Chunk chunk, BlockPos pos) {
        loop().stateTask.add(chunk, pos);
    }

    public static void updateNeighbourShapes(Level level, BlockPos sourcePos, Block sourceBlock, int flags) {
        loop().neighborTask.add(level, sourcePos, sourceBlock);
    }

    public static void beginUpdates() {
        if (UPDATE_BATCH_TASK != null) {
            return;
        }
        UPDATE_BATCH_TASK = new UpdateBatchTask();
        BukkitHelper.runTask(LevelData::endUpdates);
    }

    public static void endUpdates() {
        flush();
    }

    public static void commit(Runnable r) {
        if (UPDATE_BATCH_TASK != null) {
            UPDATE_BATCH_TASK.stateTask.commitTasks.add(r);
        } else {
            r.run();
        }
    }

    private static void flush() {
        UpdateBatchTask batchTask = UPDATE_BATCH_TASK;
        UPDATE_BATCH_TASK = null;
        if (batchTask != null) {
            batchTask.execute();
        }
    }

    private static UpdateBatchTask loop() {
        if (UPDATE_BATCH_TASK == null) {
            beginUpdates();
        }
        return UPDATE_BATCH_TASK;
    }

    public static void updateClientChunk(ChunkPos key, Map<BlockPos, CompoundTag> blocks, Map<BlockPos, VoxelShape> collissionShaps) {
        if (blocks.size() == 0) {
            CLIENT_INFOS.remove(key);
            return;
        }
        ClientInfo info = new ClientInfo();
        ListTag chunk = ListTag.newInstance();
        blocks.values().forEach(chunk::add);
        info.chunk = chunk;
        info.blocks = new HashMap<>(blocks);
        info.collissionShaps = new HashMap<>();
        collissionShaps.forEach((pos, shape) -> {
            if (!shape.equals(VoxelShape.BLOCK)) {
                info.collissionShaps.put(pos, new CollissionBox(pos, shape));
            }
        });
        CLIENT_INFOS.put(key, info);
    }

//    public static void updateClientCollission(ChunkPos key, BlockPos pos, @Nullable CollissionBox box) {
//        if (box != null) {
//            CLIENT_COLLISSION_BOXS.computeIfAbsent(key.getId(), it -> new ConcurrentHashMap<>()).put(pos, box);
//        } else {
//            CLIENT_COLLISSION_BOXS.computeIfAbsent(key.getId(), it -> new ConcurrentHashMap<>()).remove(pos);
//        }
//    }

    public static void updateClientEntityType(Level level, int entityId, EntityType<?> entityType) {
        if (entityType == null) {
            CLIENT_ENTITIES.remove(Pair.of(level.getUUID(), entityId));
        } else {
            CompoundTag tag = CompoundTag.newInstance();
            tag.putString("type", entityType.getRegistryName().toString());
            CLIENT_ENTITIES.put(Pair.of(level.getUUID(), entityId), tag);
        }
    }

    @Nullable
    public static CompoundTag getClientEntity(Player player, int entityId) {
        return CLIENT_ENTITIES.get(Pair.of(player.getLevel().getUUID(), entityId));
    }

    @Nullable
    public static FriendlyByteBuf getClientEntityData(Player player, int entityId) {
        return CLIENT_ENTITIE_DATAS.get(Pair.of(player.getLevel().getUUID(), entityId));
    }

    @Nullable
    public static ListTag getClientChunk(Player player, int x, int z) {
        ClientInfo info = CLIENT_INFOS.get(new ChunkPos(player.getLevel().getUUID(), x, z));
        if (info != null) {
            return info.chunk;
        }
        return null;
    }

    @Nullable
    public static CompoundTag getClientBlock(Player player, BlockPos pos) {
        ClientInfo info = CLIENT_INFOS.get(new ChunkPos(player.getLevel().getUUID(), pos));
        if (info != null) {
            return info.blocks.get(pos);
        }
        return null;
    }

    @Nullable
    public static CollissionBox getClientBlockCollisions(Player player, Vector3d loc) {
        double width = 0.6;
        double height = 1.8;

        int minX = MathHelper.floor(loc.getX() - width * 0.5);
        int minY = MathHelper.floor(loc.getY());
        int minZ = MathHelper.floor(loc.getZ() - width * 0.5);
        int maxX = MathHelper.floor(loc.getX() + width * 0.5);
        int maxY = MathHelper.floor(loc.getY() + height);
        int maxZ = MathHelper.floor(loc.getZ() + width * 0.5);

        UUID level = player.getLevel().getUUID();
        Cursor3D cursor = new Cursor3D(minX, minY, minZ, maxX, maxY, maxZ);
        for (BlockPos pos : cursor) {
            ClientInfo info = CLIENT_INFOS.get(new ChunkPos(level, pos));
            if (info != null) {
                CollissionBox box = info.collissionShaps.get(pos);
                if (box != null) {
                    return box;
                }
            }
        }
        return null;
    }

    public static class ClientInfo {

        ListTag chunk;
        Map<BlockPos, CompoundTag> blocks;
        Map<BlockPos, CollissionBox> collissionShaps;
    }

    public static class UpdateInfo {

        final Chunk chunk;
        final BlockPos pos;

        UpdateInfo(Chunk chunk, BlockPos pos) {
            this.chunk = chunk;
            this.pos = pos;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UpdateInfo)) return false;
            UpdateInfo that = (UpdateInfo) o;
            return Objects.equals(chunk, that.chunk) && Objects.equals(pos, that.pos);
        }

        @Override
        public int hashCode() {
            return Objects.hash(chunk, pos);
        }
    }

    public static class UpdateBatchTask {

        UpdateTask stateTask = new UpdateTask();
        UpdateNeighborTask neighborTask = new UpdateNeighborTask();
        UpdateEntityTask entityTask = new UpdateEntityTask();

        public void execute() {
            stateTask.execute();
            neighborTask.execute();
            entityTask.execute();
        }
    }

    public static class UpdateTask {

        LinkedHashSet<UpdateInfo> pending = new LinkedHashSet<>();
        ArrayList<Runnable> commitTasks = new ArrayList<>();

        public void execute() {
            HashSet<Chunk> chunks = new HashSet<>();
            pending.forEach(it -> {
                chunks.add(it.chunk);
                commitTasks.add(() -> {
                    it.chunk.getLevel().asBukkit().getBlockAt(it.pos.asBukkit()).getState().update(true, false);
                });
            });
            chunks.forEach(Chunk::freeze);
            commitTasks.forEach(Runnable::run);
        }

        public void add(Chunk chunk, BlockPos pos) {
            pending.add(new UpdateInfo(chunk, pos));
        }
    }

    public static class UpdateNeighborTask {

        private LinkedHashMap<Level, LinkedHashMap<BlockPos, Block>> pending = new LinkedHashMap<>();

        public void execute() {
            if (pending.isEmpty()) {
                return;
            }
            NeighborUpdater updater = new NeighborUpdater();
            pending.forEach((level, tasks) -> tasks.forEach((pos, block) -> {
                //
                updater.updateNeighborsAtExceptFromFacing(level, pos, block, null);
            }));
        }

        public void add(Level level, BlockPos sourcePos, Block sourceBlock) {
            pending.computeIfAbsent(level, it -> new LinkedHashMap<>()).put(sourcePos, sourceBlock);
        }
    }

    public static class UpdateEntityTask {

        private final HashSet<Integer> savingIds = new HashSet<>();
        private final ArrayList<Entity> saving = new ArrayList<>();

        private final HashSet<Integer> pendingIds = new HashSet<>();
        private final ArrayList<Entity> pending = new ArrayList<>();

        public void execute() {
            saving.forEach(it -> {
                CompoundTag tag = CompoundTag.newInstance();
                it.addAdditionalSaveData(tag);
                BukkitHelper.saveCustomEntityTag(it.asBukkit(), tag);
                //Logs.debug("{} save entity tag", it.getId());
            });
            pending.forEach(it -> {
                //Logs.debug("{} generate entity patch", it.getId());
                int entityId = it.getId();
                SynchedEntityData entityData = it.getEntityData();
                FriendlyByteBuf dirtyValues = BukkitHelper.convertToBytes(entityId, entityData.getDirtyValues());
                if (dirtyValues != null) {
                    PacketHelper.sendToTracking(ConstantKeys.NETWORK_KEY, dirtyValues, it);
                }
                FriendlyByteBuf changedValue = BukkitHelper.convertToBytes(entityId, entityData.getChangedValues());
                if (changedValue != null) {
                    CLIENT_ENTITIE_DATAS.put(Pair.of(it.getLevel().getUUID(), entityId), changedValue);
                } else {
                    CLIENT_ENTITIE_DATAS.remove(Pair.of(it.getLevel().getUUID(), entityId));
                }
            });
        }

        public void add(Entity entity, boolean allowsSave) {
            int entityId = entity.getId();
            if (pendingIds.add(entityId)) {
                pending.add(entity);
            }
            if (!allowsSave) {
                return;
            }
            if (savingIds.add(entityId)) {
                saving.add(entity);
            }
        }
    }
}

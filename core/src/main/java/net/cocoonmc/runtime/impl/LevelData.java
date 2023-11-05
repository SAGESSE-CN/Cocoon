package net.cocoonmc.runtime.impl;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.ListTag;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.chunk.Chunk;
import net.cocoonmc.core.world.chunk.ChunkPos;
import org.bukkit.Bukkit;
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

    private static UpdateTask UPDATE_STATE_TASK;
    private static UpdateNeighborTask UPDATE_NEIGHBOR_TASK;

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


    public static void updateStates(Chunk chunk, BlockPos pos) {
        if (UPDATE_STATE_TASK == null) {
            beginUpdates();
        }
        if (UPDATE_STATE_TASK != null) {
            UPDATE_STATE_TASK.add(chunk, pos);
        }
    }

    public static void updateNeighbourShapes(Level level, BlockPos sourcePos, Block sourceBlock, int flags) {
        if (UPDATE_NEIGHBOR_TASK == null) {
            beginUpdates();
        }
        if (UPDATE_NEIGHBOR_TASK != null) {
            UPDATE_NEIGHBOR_TASK.add(level, sourcePos, sourceBlock);
        }
    }

    public static void beginUpdates() {
        if (UPDATE_STATE_TASK != null) {
            return;
        }
        UPDATE_STATE_TASK = new UpdateTask();
        UPDATE_NEIGHBOR_TASK = new UpdateNeighborTask();
        Bukkit.getScheduler().runTask(Cocoon.getPlugin(), LevelData::endUpdates);
    }

    public static void endUpdates() {
        flush();
    }

    public static void commit(Runnable r) {
        if (UPDATE_STATE_TASK != null) {
            UPDATE_STATE_TASK.commitTasks.add(r);
        } else {
            r.run();
        }
    }

    private static void flush() {
        UpdateTask task1 = UPDATE_STATE_TASK;
        UpdateNeighborTask task2 = UPDATE_NEIGHBOR_TASK;
        UPDATE_STATE_TASK = null;
        UPDATE_NEIGHBOR_TASK = null;
        if (task1 != null) {
            task1.execute();
        }
        if (task2 != null) {
            task2.execute();
        }
    }

    public static void updateClientChunk(ChunkPos key, Map<BlockPos, CompoundTag> blocks) {
        if (blocks.size() == 0) {
            CLIENT_INFOS.remove(key);
            return;
        }
        ClientInfo info = new ClientInfo();
        ListTag chunk = ListTag.newInstance();
        blocks.values().forEach(chunk::add);
        info.chunk = chunk;
        info.blocks = new HashMap<>(blocks);
        CLIENT_INFOS.put(key, info);
    }

    @Nullable
    public static ListTag getClientChunk(UUID id, int x, int z) {
        ClientInfo info = CLIENT_INFOS.get(new ChunkPos(id, x, z));
        if (info != null) {
            return info.chunk;
        }
        return null;
    }

    @Nullable
    public static CompoundTag getClientBlock(UUID id, BlockPos pos) {
        ClientInfo info = CLIENT_INFOS.get(new ChunkPos(id, pos));
        if (info != null) {
            return info.blocks.get(pos);
        }
        return null;
    }

    public static class ClientInfo {

        ListTag chunk;
        Map<BlockPos, CompoundTag> blocks;
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
            commitTasks.forEach(it -> {
                it.run();
            });
        }

        public void add(Chunk chunk, BlockPos pos) {
            pending.add(new UpdateInfo(chunk, pos));
        }
    }

    public static class UpdateNeighborTask {

        private LinkedHashMap<Level, LinkedHashMap<BlockPos, Block>> pending = new LinkedHashMap<>();

        public void execute() {
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
}

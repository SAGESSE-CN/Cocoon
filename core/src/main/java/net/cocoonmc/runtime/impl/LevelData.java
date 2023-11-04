package net.cocoonmc.runtime.impl;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.ListTag;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.chunk.Chunk;
import net.cocoonmc.core.world.chunk.ChunkPos;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LevelData {

    private static ConcurrentHashMap<UUID, Level> LEVELS = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<ChunkPos, ClientInfo> CLIENT_INFOS = new ConcurrentHashMap<>();

    private static UpdateTask UPDATE_TASK;

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


    public static void updateBukkit(Chunk chunk, BlockPos pos) {
        if (UPDATE_TASK == null) {
            UPDATE_TASK = new UpdateTask();
            Bukkit.getScheduler().runTaskLater(Cocoon.getPlugin(), () -> {
                UpdateTask task = UPDATE_TASK;
                UPDATE_TASK = null;
                task.execute();
            }, 1);
        }
        UPDATE_TASK.add(chunk, pos);
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

        private LinkedHashSet<UpdateInfo> pending = new LinkedHashSet<>();

        public void execute() {
            HashSet<Chunk> chunks = new HashSet<>();
            pending.forEach(it -> {
                chunks.add(it.chunk);
                it.chunk.getLevel().asBukkit().getBlockAt(it.pos.asBukkit()).getState().update(true, false);
            });
            chunks.forEach(Chunk::freeze);
        }

        public void add(Chunk chunk, BlockPos pos) {
            pending.add(new UpdateInfo(chunk, pos));
        }
    }
}

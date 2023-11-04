package net.cocoonmc.core.world.chunk;

import net.cocoonmc.core.BlockPos;

import java.util.Objects;
import java.util.UUID;

public class ChunkPos {

    private final int x;
    private final int z;

    private final UUID id;

    public ChunkPos(UUID id, int x, int z) {
        this.x = x;
        this.z = z;
        this.id = id;
    }

    public ChunkPos(UUID id, BlockPos pos) {
        this(id, pos.getX() >> 4, pos.getZ() >> 4);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkPos)) return false;
        ChunkPos that = (ChunkPos) o;
        return x == that.x && z == that.z && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z, id);
    }
}

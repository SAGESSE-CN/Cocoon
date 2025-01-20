package net.cocoonmc.core;

import com.mojang.serialization.Codec;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.utils.MathHelper;

import java.util.Objects;

public class BlockPos {

    public static final Codec<BlockPos> CODEC = Cocoon.API.CODEC.getBlockPos();

    public static BlockPos ZERO = new BlockPos(0, 0, 0);

    private static final int PACKED_X_LENGTH;
    private static final int PACKED_Z_LENGTH;
    private static final int PACKED_Y_LENGTH;
    private static final long PACKED_X_MASK;
    private static final long PACKED_Y_MASK;
    private static final long PACKED_Z_MASK;
    private static final int Y_OFFSET = 0;
    private static final int Z_OFFSET;
    private static final int X_OFFSET;

    private final int x;
    private final int y;
    private final int z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static BlockPos of(org.bukkit.Location loc) {
        return new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static BlockPos of(org.bukkit.block.Block block) {
        return of(block.getLocation());
    }

    public static BlockPos of(long l) {
        return new BlockPos(BlockPos.getX(l), BlockPos.getY(l), BlockPos.getZ(l));
    }

    public static int getX(long l) {
        return (int) (l << 64 - X_OFFSET - PACKED_X_LENGTH >> 64 - PACKED_X_LENGTH);
    }

    public static int getY(long l) {
        return (int) (l << 64 - PACKED_Y_LENGTH >> 64 - PACKED_Y_LENGTH);
    }

    public static int getZ(long l) {
        return (int) (l << 64 - Z_OFFSET - PACKED_Z_LENGTH >> 64 - PACKED_Z_LENGTH);
    }

    public static long asLong(int i, int j, int k) {
        long l = 0L;
        l |= ((long) i & PACKED_X_MASK) << X_OFFSET;
        l |= ((long) j & PACKED_Y_MASK) << 0;
        return l |= ((long) k & PACKED_Z_MASK) << Z_OFFSET;
    }

    public BlockPos relative(Direction dir) {
        return new BlockPos(getX() + dir.getStepX(), getY() + dir.getStepY(), getZ() + dir.getStepZ());
    }

    public BlockPos offset(BlockPos pos) {
        return new BlockPos(getX() + pos.getX(), getY() + pos.getY(), getZ() + pos.getZ());
    }

    public BlockPos subtract(BlockPos pos) {
        return new BlockPos(getX() - pos.getX(), getY() - pos.getY(), getZ() - pos.getZ());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockPos)) return false;
        BlockPos pos = (BlockPos) o;
        return x == pos.x && y == pos.y && z == pos.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", x, y, z);
    }

    public long asLong() {
        return BlockPos.asLong(x, y, z);
    }

    public org.bukkit.Location asBukkit() {
        return new org.bukkit.Location(null, x, y, z);
    }

    public static class Mutable extends BlockPos {

        private int x;
        private int y;
        private int z;

        public Mutable(int x, int y, int z) {
            super(0, 0, 0);
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void set(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void setX(int x) {
            this.x = x;
        }

        @Override
        public int getX() {
            return x;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public int getY() {
            return y;
        }

        public void setZ(int z) {
            this.z = z;
        }

        @Override
        public int getZ() {
            return z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BlockPos)) return false;
            BlockPos pos = (BlockPos) o;
            return x == pos.getX() && y == pos.getY() && z == pos.getZ();
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return String.format("(%d, %d, %d)", x, y, z);
        }
    }


    static {
        PACKED_X_LENGTH = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
        PACKED_Z_LENGTH = PACKED_X_LENGTH;
        PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH;
        PACKED_X_MASK = (1L << PACKED_X_LENGTH) - 1L;
        PACKED_Y_MASK = (1L << PACKED_Y_LENGTH) - 1L;
        PACKED_Z_MASK = (1L << PACKED_Z_LENGTH) - 1L;
        Z_OFFSET = PACKED_Y_LENGTH;
        X_OFFSET = PACKED_Y_LENGTH + PACKED_Z_LENGTH;
    }
}

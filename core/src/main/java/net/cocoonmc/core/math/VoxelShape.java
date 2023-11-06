package net.cocoonmc.core.math;

import java.util.Objects;

public class VoxelShape {

    public static final VoxelShape EMPTY = new VoxelShape(0, 0, 0, 0, 0, 0);
    public static final VoxelShape BLOCK = new VoxelShape(0, 0, 0, 1, 1, 1);

    private final float minX;
    private final float minY;
    private final float minZ;

    private final float maxX;
    private final float maxY;
    private final float maxZ;

    public VoxelShape(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public boolean contains(double x, double y, double z) {
        return minX <= x && minY <= y && minZ <= z && x <= maxX && y <= maxY && z <= maxZ;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMinZ() {
        return minZ;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMaxZ() {
        return maxZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoxelShape)) return false;
        VoxelShape that = (VoxelShape) o;
        return Float.compare(that.minX, minX) == 0 && Float.compare(that.minY, minY) == 0 && Float.compare(that.minZ, minZ) == 0 && Float.compare(that.maxX, maxX) == 0 && Float.compare(that.maxY, maxY) == 0 && Float.compare(that.maxZ, maxZ) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public String toString() {
        return String.format("(%f %f %f; %f %f %f)", minX, minY, minZ, maxX, maxY, maxZ);
    }
}

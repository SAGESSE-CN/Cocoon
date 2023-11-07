package net.cocoonmc.core.math;

import net.cocoonmc.core.BlockPos;

import java.util.Objects;

public class Vector3d {

    public static final Vector3d ZERO = new Vector3d(0, 0, 0);

    private double x;
    private double y;
    private double z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3d of(org.bukkit.Location loc) {
        return new Vector3d(loc.getX(), loc.getY(), loc.getZ());
    }

    public static Vector3d of(BlockPos loc) {
        return new Vector3d(loc.getX(), loc.getY(), loc.getZ());
    }

    public double distanceTo(Vector3d pos) {
        return distanceTo(pos.x, pos.y, pos.z);
    }

    public double distanceTo(double tx, double ty, double tz) {
        double d7 = tx - x;
        double d8 = ty - y;
        double d9 = tz - z;
        return d7 * d7 + d8 * d8 + d9 * d9;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3d)) return false;
        Vector3d vector3f = (Vector3d) o;
        return Double.compare(vector3f.x, x) == 0 && Double.compare(vector3f.y, y) == 0 && Double.compare(vector3f.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("(%f %f %f)", x, y, z);
    }

    public org.bukkit.Location asBukkit() {
        return new org.bukkit.Location(null, x, y, z);
    }

}

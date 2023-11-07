package net.cocoonmc.core.item.context;

import net.cocoonmc.core.math.Vector3d;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public abstract class HitResult {

    protected final Vector3d location;

    protected HitResult(Vector3d location) {
        this.location = location;
    }

    public double distanceTo(Entity player) {
        Location var0 = player.getLocation();
        double var1 = location.getX() - var0.getX();
        double var3 = location.getY() - var0.getY();
        double var5 = location.getZ() - var0.getZ();
        return var1 * var1 + var3 * var3 + var5 * var5;
    }

    public abstract Type getType();

    public Vector3d getLocation() {
        return this.location;
    }

    public enum Type {
        MISS,
        BLOCK,
        ENTITY
    }
}


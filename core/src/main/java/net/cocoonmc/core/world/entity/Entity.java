package net.cocoonmc.core.world.entity;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.math.Vector3d;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.utils.ObjectHelper;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.runtime.impl.ConstantKeys;
import net.cocoonmc.runtime.impl.LevelData;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class Entity {

    private int id;
    private UUID uuid = BukkitHelper.INVALID_UUID;

    protected Level level;
    protected final EntityType<?> entityType;

    private Vector3d location = Vector3d.ZERO;
    private Vector3f bodyRot = Vector3f.ZERO;
    private org.bukkit.entity.Entity delegate;

    public Entity(EntityType<?> entityType, Level level) {
        this.entityType = entityType;
        this.level = level;
    }

    public static Entity of(org.bukkit.entity.Entity entity) {
        if (entity instanceof org.bukkit.entity.LivingEntity) {
            return LivingEntity.of((org.bukkit.entity.LivingEntity) entity);
        }
        return Cocoon.API.CACHE.computeIfAbsent(entity, ConstantKeys.ENTITY_KEY, it -> {
            EntityType<Entity> entityType = EntityTypes.findEntityType(it);
            Entity entity1 = entityType.create(Level.of(it.getWorld()), BlockPos.ZERO, null);
            entity1.setDelegate(it);
            return entity1;
        });
    }

    public double distanceTo(Entity entity) {
        return asBukkit().getLocation().distance(entity.asBukkit().getLocation());
    }

    public double distanceToSqr(double x, double y, double z) {
        return asBukkit().getLocation().distance(new org.bukkit.Location(null, x, y, z));
    }

    public float getViewXRot(float f) {
        // x + (x - ox) * f;
        return (float) delegate.getLocation().getDirection().getX() * f;
    }

    public float getViewYRot(float f) {
        // y + (y - oy) * f;
        return (float) delegate.getLocation().getDirection().getY() * f;
    }

    public void setBodyRot(Vector3f rot) {
        if (delegate == null) {
            bodyRot = rot;
            return;
        }
        delegate.setRotation(rot.getY(), rot.getX());
    }

    public Vector3f getBodyRot() {
        if (delegate == null) {
            return bodyRot;
        }
        org.bukkit.Location loc = delegate.getLocation();
        return new Vector3f(loc.getPitch(), loc.getYaw(), 0);
    }

    public int getId() {
        return id;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getStringUUID() {
        return getUUID().toString();
    }

    public EntityType<?> getType() {
        return entityType;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public void setLocation(Vector3d location) {
        if (this.delegate == null) {
            this.location = location;
            return;
        }
        org.bukkit.Location loc = delegate.getLocation().clone();
        loc.setX(loc.getX());
        loc.setY(loc.getY());
        loc.setZ(loc.getZ());
        delegate.teleport(loc);
    }

    public Vector3d getLocation() {
        if (this.delegate == null) {
            return this.location;
        }
        return Vector3d.of(delegate.getLocation());
    }

    public boolean isValid() {
        if (this.delegate == null) {
            return false;
        }
        return delegate.isValid();
    }

    public <T, V> void setPersistentData(ResourceLocation key, PersistentDataType<T, V> dataType, V value) {
        delegate.getPersistentDataContainer().set(key.asBukkit(), dataType, value);
    }

    public <T, V> V getPersistentData(ResourceLocation key, PersistentDataType<T, V> dataType) {
        return delegate.getPersistentDataContainer().get(key.asBukkit(), dataType);
    }

    public void setDelegate(org.bukkit.entity.Entity delegate) {
        this.delegate = delegate;
        if (delegate != null) {
            this.id = delegate.getEntityId();
            this.uuid = delegate.getUniqueId();
            this.location = Vector3d.of(delegate.getLocation());
        }
        if (entityType.getDelegate() != null) {
            LevelData.updateClientEntityType(level, id, entityType);
        }
    }

    public org.bukkit.entity.Entity asBukkit() {
        return delegate;
    }

    @Override
    public String toString() {
        return ObjectHelper.makeDescription(this, "uuid", getStringUUID());
    }
}

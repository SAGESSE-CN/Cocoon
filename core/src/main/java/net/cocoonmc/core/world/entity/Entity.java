package net.cocoonmc.core.world.entity;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.utils.ObjectHelper;
import net.cocoonmc.runtime.impl.ConstantKeys;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class Entity {

    private final org.bukkit.entity.Entity entity;

    public Entity(org.bukkit.entity.Entity entity) {
        this.entity = entity;
    }

    public static Entity of(org.bukkit.entity.Entity entity) {
        if (entity instanceof org.bukkit.entity.LivingEntity) {
            return LivingEntity.of((org.bukkit.entity.LivingEntity) entity);
        }
        return Cocoon.API.CACHE.computeIfAbsent(entity, ConstantKeys.ENTITY_KEY, Entity::new);
    }

    public double distanceTo(Entity entity) {
        return asBukkit().getLocation().distance(entity.asBukkit().getLocation());
    }

    public double distanceToSqr(double x, double y, double z) {
        return asBukkit().getLocation().distance(new org.bukkit.Location(null, x, y, z));
    }

    public float getViewXRot(float f) {
        // x + (x - ox) * f;
        return (float) entity.getLocation().getDirection().getX() * f;
    }

    public float getViewYRot(float f) {
        // y + (y - oy) * f;
        return (float) entity.getLocation().getDirection().getY() * f;
    }

    public int getId() {
        return entity.getEntityId();
    }

    public UUID getUUID() {
        return entity.getUniqueId();
    }

    public String getStringUUID() {
        return entity.getUniqueId().toString();
    }

    public boolean isValid() {
        return entity.isValid();
    }


    public <T, V> void setPersistentData(ResourceLocation key, PersistentDataType<T, V> dataType, V value) {
        entity.getPersistentDataContainer().set(key.asBukkit(), dataType, value);
    }

    public <T, V> V getPersistentData(ResourceLocation key, PersistentDataType<T, V> dataType) {
        return entity.getPersistentDataContainer().get(key.asBukkit(), dataType);
    }

    public org.bukkit.entity.Entity asBukkit() {
        return entity;
    }

    @Override
    public String toString() {
        return ObjectHelper.makeDescription(this, "uuid", getUUID());
    }
}

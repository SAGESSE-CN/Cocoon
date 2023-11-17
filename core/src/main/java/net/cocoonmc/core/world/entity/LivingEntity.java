package net.cocoonmc.core.world.entity;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.runtime.impl.ConstantKeys;
import net.cocoonmc.runtime.impl.LevelData;

public class LivingEntity extends Entity {

    private org.bukkit.entity.LivingEntity delegate;

    public LivingEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public static LivingEntity of(org.bukkit.entity.LivingEntity entity) {
        if (entity instanceof org.bukkit.entity.Player) {
            return Player.of((org.bukkit.entity.Player) entity);
        }
        return Cocoon.API.CACHE.computeIfAbsent(entity, ConstantKeys.LIVING_ENTITY_KEY, it -> {
            EntityType<LivingEntity> entityType = EntityTypes.findEntityType(it);
            LivingEntity entity1 = entityType.create(Level.of(it.getWorld()), BlockPos.ZERO, null);
            entity1.setDelegate(it);
            LevelData.loadEntityTag(entity1);
            return entity1;
        });
    }

    @Override
    public void setDelegate(org.bukkit.entity.Entity delegate) {
        super.setDelegate(delegate);
        if (delegate instanceof org.bukkit.entity.LivingEntity) {
            this.delegate = (org.bukkit.entity.LivingEntity) delegate;
        }
    }

    @Override
    public org.bukkit.entity.LivingEntity asBukkit() {
        return delegate;
    }
}

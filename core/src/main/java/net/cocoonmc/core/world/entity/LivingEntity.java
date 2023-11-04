package net.cocoonmc.core.world.entity;

import net.cocoonmc.Cocoon;
import net.cocoonmc.runtime.impl.ConstantKeys;

public class LivingEntity extends Entity {

    private final org.bukkit.entity.LivingEntity livingEntity;

    public LivingEntity(org.bukkit.entity.LivingEntity livingEntity) {
        super(livingEntity);
        this.livingEntity = livingEntity;
    }

    public static LivingEntity of(org.bukkit.entity.LivingEntity livingEntity) {
        if (livingEntity instanceof org.bukkit.entity.Player) {
            return Player.of((org.bukkit.entity.Player) livingEntity);
        }
        return Cocoon.API.CACHE.computeIfAbsent(livingEntity, ConstantKeys.LIVING_ENTITY_KEY, LivingEntity::new);
    }

    @Override
    public org.bukkit.entity.LivingEntity asBukkit() {
        return livingEntity;
    }
}

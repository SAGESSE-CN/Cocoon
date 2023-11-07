package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;

public class EntityPlaceTask {

    private static final Stack<EntityPlaceTask> STACK = new Stack<>();

    private final Level level;
    private final Entity entity;

    public EntityPlaceTask(Level level, Entity entity) {
        this.level = level;
        this.entity = entity;
    }

    public void apply(org.bukkit.entity.Entity entityIn) {
        Vector3f bodyRot = entity.getBodyRot();
        Level level = Level.of(entityIn.getWorld());
        entity.setLevel(level);
        entity.setDelegate(entityIn);
        entityIn.setRotation(bodyRot.getY(), bodyRot.getX());
        BukkitHelper.setCustomEntityType(entityIn, entity.getType());
    }

    @Nullable
    public static EntityPlaceTask last() {
        if (!STACK.isEmpty()) {
            return STACK.lastElement();
        }
        return null;
    }

    public static void push(Level level, Entity entity) {
        STACK.push(new EntityPlaceTask(level, entity));
    }

    public static void pop(Level level, Entity entity) {
        STACK.pop();
    }

    public Level getLevel() {
        return level;
    }

    public Entity getEntity() {
        return entity;
    }

    public EntityType<?> getEntityType() {
        return entity.getType();
    }
}

package net.cocoonmc.core.world.entity;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.world.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class EntityType<T extends Entity> {

    private static final Map<ResourceLocation, EntityType<?>> KEYED_ENTITY_TYPES = new ConcurrentHashMap<>();

    private ResourceLocation registryName;
    private EntityType<?> delegate;
    private final Factory<T> factory;

    public EntityType(Factory<T> factory) {
        this.factory = factory;
    }

    public T create(Level level, BlockPos pos, @Nullable CompoundTag tag) {
        return factory.create(this, level);
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public void setDelegate(EntityType<?> delegate) {
        this.delegate = delegate;
    }

    public EntityType<?> getDelegate() {
        return delegate;
    }

    public org.bukkit.entity.EntityType asBukkit() {
        if (delegate != null) {
            return delegate.asBukkit();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityType<?>)) return false;
        EntityType<?> that = (EntityType<?>) o;
        return Objects.equals(registryName, that.registryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registryName);
    }

    public static Collection<EntityType<?>> values() {
        return KEYED_ENTITY_TYPES.values();
    }

    public static EntityType<?> byKey(ResourceLocation registryName) {
        return KEYED_ENTITY_TYPES.get(registryName);
    }

    public static <T extends Entity> EntityType<T> register(ResourceLocation registryName, EntityType<T> entityType) {
        entityType.registryName = registryName;
        KEYED_ENTITY_TYPES.put(registryName, entityType);
        return entityType;
    }

    public interface Factory<T extends Entity> {

        T create(EntityType<T> entityType, Level level);
    }
}

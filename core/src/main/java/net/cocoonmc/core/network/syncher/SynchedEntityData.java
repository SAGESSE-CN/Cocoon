package net.cocoonmc.core.network.syncher;

import com.google.common.base.Objects;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.runtime.impl.LevelData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SynchedEntityData {

    private static final Map<Class<?>, Integer> ENTITY_ID_POOL = new ConcurrentHashMap<>();

    private final Entity entity;
    private final HashMap<Integer, DataItem<?>> itemsById = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private boolean isDirty;

    public SynchedEntityData(Entity entity) {
        this.entity = entity;
    }

    public static <T> EntityDataAccessor<T> defineId(Class<?> entityType, int ordinal, EntityDataSerializer<T> serializer) {
        int id = ENTITY_ID_POOL.getOrDefault(entityType, 0);
        ENTITY_ID_POOL.put(entityType, id + 1);
        return new EntityDataAccessor<>(id, ordinal, serializer);
    }

    public <T> void define(EntityDataAccessor<T> accessor, T object) {
        int i = accessor.getId();
        if (i > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + i + "! (Max is 254)");
        }
        if (this.itemsById.containsKey(i)) {
            throw new IllegalArgumentException("Duplicate id value for " + i + "!");
        }
//        if (EntityDataSerializers.getSerializedId(accessor.getSerializer()) < 0) {
//            throw new IllegalArgumentException("Unregistered serializer " + accessor.getSerializer() + " for " + i + "!");
//        }
        this.createDataItem(accessor, object);
    }

    private <T> void createDataItem(EntityDataAccessor<T> entityDataAccessor, T object) {
        DataItem<T> dataItem = new DataItem<T>(entityDataAccessor, object);
        this.lock.writeLock().lock();
        this.itemsById.put(entityDataAccessor.getId(), dataItem);
        this.lock.writeLock().unlock();
    }

    public <T> boolean hasItem(EntityDataAccessor<T> accessor) {
        return this.itemsById.containsKey(accessor.getId());
    }

    private <T> DataItem<T> getItem(EntityDataAccessor<T> accessor) {
        DataItem<T> dataItem;
        this.lock.readLock().lock();
        try {
            // noinspection unchecked
            dataItem = (DataItem<T>) this.itemsById.get(accessor.getId());
        } catch (Throwable throwable) {
            throw new RuntimeException("Getting synched entity data, Data ID: " + accessor.getId());
        } finally {
            this.lock.readLock().unlock();
        }
        return dataItem;
    }

    public <T> T get(EntityDataAccessor<T> accessor) {
        return this.getItem(accessor).getValue();
    }

    public <T> void set(EntityDataAccessor<T> accessor, T object) {
        this.set(accessor, object, false);
    }

    public <T> void set(EntityDataAccessor<T> accessor, T object, boolean bl) {
        DataItem<T> dataItem = getItem(accessor);
        if (bl || !Objects.equal(object, dataItem.getValue())) {
            dataItem.setValue(object);
            this.entity.onSyncedDataUpdated(accessor);
            dataItem.setDirty(true);
            this.isDirty = true;
            LevelData.updateEntityTag(this.entity);
        }
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    @Nullable
    public List<DataItem<?>> getChangedValues() {
        ArrayList<DataItem<?>> results = null;
        this.lock.readLock().lock();
        for (DataItem<?> dataItem : itemsById.values()) {
            if (dataItem.isSetToDefault()) continue;
            if (results == null) {
                results = new ArrayList<>();
            }
            results.add(dataItem);
        }
        this.lock.readLock().unlock();
        return results;
    }

    @Nullable
    public List<DataItem<?>> getDirtyValues() {
        ArrayList<DataItem<?>> results = null;
        if (isDirty) {
            this.lock.readLock().lock();
            for (DataItem<?> dataItem : itemsById.values()) {
                if (!dataItem.isDirty()) {
                    continue;
                }
                dataItem.setDirty(false);
                if (results == null) {
                    results = new ArrayList<>();
                }
                results.add(dataItem);
            }
            this.lock.readLock().unlock();
        }
        this.isDirty = false;
        return results;
    }

    public static class DataItem<T> {
        final EntityDataAccessor<T> accessor;
        T value;
        private final T initialValue;
        private boolean dirty;

        public DataItem(EntityDataAccessor<T> entityDataAccessor, T object) {
            this.accessor = entityDataAccessor;
            this.initialValue = object;
            this.value = object;
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeInt(accessor.getOrdinal());
            buf.writeInt(accessor.getId());
            FriendlyByteBuf buf1 = new FriendlyByteBuf();
            accessor.getSerializer().write(buf1, value);
            buf.writeByteArray(buf1.array());
        }

        public EntityDataAccessor<T> getAccessor() {
            return this.accessor;
        }

        public void setValue(T object) {
            this.value = object;
        }

        public T getValue() {
            return this.value;
        }

        public boolean isDirty() {
            return this.dirty;
        }

        public void setDirty(boolean bl) {
            this.dirty = bl;
        }

        public boolean isSetToDefault() {
            return this.initialValue.equals(this.value);
        }
    }
}

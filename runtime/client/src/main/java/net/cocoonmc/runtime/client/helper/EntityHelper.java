package net.cocoonmc.runtime.client.helper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

public class EntityHelper {

    public static final HashMap<Integer, CompoundTag> PENDING_NEW_ENTITIES = new HashMap<>();
    public static final HashMap<Class<?>, Layer> REGISTERED_ENTITY_ACCESSORS = new HashMap<>();
    public static final HashMap<Class<?>, Hierarchy> REGISTERED_ENTITY_HIERARCHIES = new HashMap<>();

    public static void registerData(Class<?> clazz, IntSupplier id, IntSupplier serializerId) {
        REGISTERED_ENTITY_ACCESSORS.computeIfAbsent(clazz, Layer::new).add(new DataItem(id, serializerId));
    }

    public static DataList createDataList(FriendlyByteBuf buf) {
        return new DataList(buf);
    }

    public static class DataItem {

        private int id = 0;
        private int serializerId = 0;

        private IntSupplier idProvider;
        private IntSupplier serializerIdProvider;

        public DataItem(IntSupplier idProvider, IntSupplier serializerIdProvider) {
            this.idProvider = idProvider;
            this.serializerIdProvider = serializerIdProvider;
        }

        public int getId() {
            if (idProvider != null) {
                id = idProvider.getAsInt();
                idProvider = null;
            }
            return id;
        }

        public int getSerializerId() {
            if (serializerIdProvider != null) {
                serializerId = serializerIdProvider.getAsInt();
                serializerIdProvider = null;
            }
            return serializerId;
        }
    }

    public static class DataValue {

        private final int ordinal;
        private final int idx;
        private final byte[] value;

        private int id = -1;
        private int serializerId = -1;

        public DataValue(int ordinal, int idx, byte[] value) {
            this.ordinal = ordinal;
            this.idx = idx;
            this.value = value;
        }

        protected boolean freeze(Hierarchy hierarchy) {
            DataItem accessor = hierarchy.getItem(ordinal, idx);
            if (accessor == null) {
                return false;
            }
            this.id = accessor.getId();
            this.serializerId = accessor.getSerializerId();
            return true;
        }

        public int getId() {
            return id;
        }

        public int getSerializerId() {
            return serializerId;
        }

        public byte[] getValue() {
            return value;
        }
    }

    public static class DataList {

        private final ArrayList<DataValue> items = new ArrayList<>();

        public DataList(FriendlyByteBuf buf) {
            int size = buf.readVarInt();
            for (int i = 0; i < size; ++i) {
                int level = buf.readInt();
                int idx = buf.readInt();
                byte[] value = buf.readByteArray();
                items.add(new DataValue(level, idx, value));
            }
        }

        public Collection<DataValue> freeze(Entity entity) {
            Hierarchy hierarchy = REGISTERED_ENTITY_HIERARCHIES.computeIfAbsent(entity.getClass(), Hierarchy::new);
            return items.stream().filter(it -> it.freeze(hierarchy)).collect(Collectors.toList());
        }

        public boolean isEmpty() {
            return items.isEmpty();
        }
    }

    public static class Layer {

        private final ArrayList<DataItem> keys = new ArrayList<>();

        public Layer(Class<?> entityType) {
        }

        public void add(DataItem item) {
            keys.add(item);
        }

        public DataItem get(int index) {
            if (index >= 0 && index < keys.size()) {
                return keys.get(index);
            }
            return null;
        }
    }

    public static class Hierarchy {

        private final ArrayList<Layer> layers = new ArrayList<>();

        public Hierarchy(Class<?> entityType) {
            while (entityType != null && entityType != Object.class) {
                Layer layer = REGISTERED_ENTITY_ACCESSORS.get(entityType);
                if (layer != null) {
                    layers.add(0, layer);
                }
                entityType = entityType.getSuperclass();
            }
        }

        public DataItem getItem(int ordinal, int idx) {
            int size = layers.size();
            if (ordinal >= 0 && ordinal < size) {
                return layers.get(ordinal).get(idx);
            }
            ordinal += size;
            if (ordinal >= 0 && ordinal < size) {
                return layers.get(ordinal).get(idx);
            }
            return null;
        }
    }
}

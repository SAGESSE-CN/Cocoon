package net.cocoonmc.core.network.syncher;

public class EntityDataAccessor<T> {

    private final int id;
    private final int ordinal;
    private final EntityDataSerializer<T> serializer;

    public EntityDataAccessor(int id, int ordinal, EntityDataSerializer<T> serializer) {
        this.id = id;
        this.ordinal = ordinal;
        this.serializer = serializer;
    }

    public int getId() {
        return id;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public EntityDataSerializer<T> getSerializer() {
        return serializer;
    }
}

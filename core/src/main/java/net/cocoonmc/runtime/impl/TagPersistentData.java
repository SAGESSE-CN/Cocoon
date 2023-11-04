package net.cocoonmc.runtime.impl;

import io.netty.buffer.Unpooled;
import net.cocoonmc.core.network.FriendlyByteBuf;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class TagPersistentData implements PersistentDataType<byte[], FriendlyByteBuf> {

    public static final TagPersistentData DEFAULT = new TagPersistentData();

    @NotNull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @NotNull
    @Override
    public Class<FriendlyByteBuf> getComplexType() {
        return FriendlyByteBuf.class;
    }

    @NotNull
    @Override
    public byte[] toPrimitive(@NotNull FriendlyByteBuf complex, @NotNull PersistentDataAdapterContext context) {
        return complex.array();
    }

    @NotNull
    @Override
    public FriendlyByteBuf fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        return new FriendlyByteBuf(Unpooled.wrappedBuffer(primitive));
    }
}

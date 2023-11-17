package net.cocoonmc.core.utils;

import io.netty.buffer.Unpooled;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.network.FriendlyByteBuf;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class PersistentDataHelper {

    public static final BufferData BYTE_BUFFER = new BufferData();

    public static final CompoundTagData COMPOUND_TAG = new CompoundTagData();


    public static class CompoundTagData implements PersistentDataType<byte[], CompoundTag> {

        @NotNull
        @Override
        public Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @NotNull
        @Override
        public Class<CompoundTag> getComplexType() {
            return CompoundTag.class;
        }

        @NotNull
        @Override
        public byte[] toPrimitive(@NotNull CompoundTag complex, @NotNull PersistentDataAdapterContext context) {
            FriendlyByteBuf buf = new FriendlyByteBuf();
            buf.writeNbt(complex);
            return buf.array();
        }

        @NotNull
        @Override
        public CompoundTag fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(primitive));
            CompoundTag tag = buf.readNbt();
            if (tag != null) {
                return tag;
            }
            return CompoundTag.newInstance();
        }
    }

    ;

    public static class BufferData implements PersistentDataType<byte[], FriendlyByteBuf> {

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
}

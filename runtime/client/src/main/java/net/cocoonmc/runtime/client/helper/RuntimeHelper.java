package net.cocoonmc.runtime.client.helper;

import io.netty.buffer.Unpooled;
import net.cocoonmc.runtime.client.api.Available;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Available("[1.18, )")
public class RuntimeHelper {

    public static ClientboundBlockUpdatePacket buildBlockUpdatePacket(BlockPos pos, BlockState state) {
        return new ClientboundBlockUpdatePacket(pos, state);
    }

    public static ClientboundBlockEntityDataPacket buildBlockEntityDataPacket(BlockPos pos, BlockEntityType<?> type, CompoundTag tag) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.directBuffer());
        buf.writeBlockPos(pos);
        buf.writeVarInt(RegistryHelper.BLOCK_ENTITY_TYPES.getId(type));
        buf.writeNbt(tag);
        return new ClientboundBlockEntityDataPacket(buf);
    }

    public static ClientboundCustomPayloadPacket buildCustomPayloadPacket(ResourceLocation id, FriendlyByteBuf buf) {
        return new ClientboundCustomPayloadPacket(id, buf);
    }
}

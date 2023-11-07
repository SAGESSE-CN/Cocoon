package net.cocoonmc.runtime.forge.helper;

import io.netty.buffer.Unpooled;
import net.cocoonmc.runtime.forge.api.Available;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

@Available("[1.19, )")
public class RuntimeHelper {

    public static int getIdByMenuKey(ResourceLocation rl) {
        return ((ForgeRegistry<?>) ForgeRegistries.MENU_TYPES).getID(rl);
    }

    public static int getBlockEntityId(ResourceLocation rl) {
        if (rl != null) {
            return ((ForgeRegistry<?>) ForgeRegistries.BLOCK_ENTITY_TYPES).getID(rl);
        }
        return 0;
    }

    public static EntityType<?> getEntityType(ResourceLocation rl) {
        return ForgeRegistries.ENTITY_TYPES.getValue(rl);
    }

    public static ResourceLocation getBlockEntityKey(ChunkAccess chunk, BlockPos pos) {
        BlockEntity entity = chunk.getBlockEntity(pos);
        if (entity != null) {
            return ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(entity.getType());
        }
        return null;
    }


    public static void handleBlockEntity(ChunkAccess chunk, BlockPos pos, BlockState state, CompoundTag tag) {
        if (!state.hasBlockEntity()) {
            return;
        }
        BlockEntity blockEntity = chunk.getBlockEntity(pos);
        if (blockEntity != null) {
            blockEntity.handleUpdateTag(tag);
        }
    }

    public static ClientboundBlockUpdatePacket buildBlockUpdatePacket(BlockPos pos, BlockState state) {
        return new ClientboundBlockUpdatePacket(pos, state);
    }

    public static ClientboundBlockEntityDataPacket buildBlockEntityDataPacket(ChunkAccess chunk, BlockPos pos, BlockState state, CompoundTag tag) {
        if (tag != null && state.hasBlockEntity()) {
            BlockEntity blockEntity = chunk.getBlockEntity(pos);
            if (blockEntity != null) {
                return ClientboundBlockEntityDataPacket.create(blockEntity, it -> tag);
            }
        }
        return null;
    }

    public static ClientboundBlockEntityDataPacket buildBlockEntityDataPacket(BlockPos pos, ResourceLocation type, CompoundTag tag) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.directBuffer());
        buf.writeBlockPos(pos);
        buf.writeVarInt(getBlockEntityId(type));
        buf.writeNbt(tag);
        return new ClientboundBlockEntityDataPacket(buf);
    }

    public static ClientboundCustomPayloadPacket buildCustomPayloadPacket(ResourceLocation id, FriendlyByteBuf buf) {
        return new ClientboundCustomPayloadPacket(id, buf);
    }

    public static ClientboundSetEntityDataPacket buildSetEntityDataPacket(FriendlyByteBuf buf) {
        return new ClientboundSetEntityDataPacket(buf);
    }
}

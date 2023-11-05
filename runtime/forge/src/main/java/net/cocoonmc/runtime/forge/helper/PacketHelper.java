package net.cocoonmc.runtime.forge.helper;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import net.cocoonmc.runtime.forge.api.CocoonLevelChunk;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Optional;
import java.util.function.BiConsumer;

public class PacketHelper {

    private static final ResourceLocation FML_PLAY = new ResourceLocation("fml", "play");
    private static final ResourceLocation COCOON_PLAY = new ResourceLocation("cocoon", "play");

    private static final ImmutableMap<Integer, BiConsumer<ClientGamePacketListener, FriendlyByteBuf>> HANDLERS = ImmutableMap.<Integer, BiConsumer<ClientGamePacketListener, FriendlyByteBuf>>builder()
            .put(0, PacketHelper::handleOpenWindow)
            .put(1, PacketHelper::handleBlockChange)
            .put(2, PacketHelper::handleBlockData)
            .put(3, PacketHelper::handleSectionUpdate)
            .build();

    public static boolean test(ResourceLocation id) {
        return id.equals(COCOON_PLAY);
    }

    public static void handle(Connection connection, FriendlyByteBuf payload) {
        BiConsumer<ClientGamePacketListener, FriendlyByteBuf> handler = HANDLERS.get(payload.readVarInt());
        if (handler != null) {
            handler.accept((ClientGamePacketListener) connection.getPacketListener(), payload);
        }
    }

    private static void handleOpenWindow(ClientGamePacketListener packetListener, FriendlyByteBuf payload) {
        ResourceLocation rl = payload.readResourceLocation();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer(4 + payload.readableBytes()));
        buf.writeVarInt(1); // open container
        buf.writeVarInt(RuntimeHelper.getIdByMenuKey(rl));
        buf.writeBytes(payload);
        Packet<ClientGamePacketListener> newPacket = RuntimeHelper.buildCustomPayloadPacket(FML_PLAY, buf);
        Minecraft.getInstance().execute(() -> {
            newPacket.handle(packetListener);
        });
    }

    private static void handleBlockChange(ClientGamePacketListener packetListener, FriendlyByteBuf payload) {
        BlockState oldState = Block.stateById(payload.readVarInt());
        CompoundTag blockTag = payload.readNbt();
        if (blockTag == null) {
            return;
        }
        BlockPos pos = BlockHelper.getBlockPos(blockTag);
        Pair<BlockState, CompoundTag> pair = BlockHelper.getBlockFromTag(blockTag);
        if (pair == null) {
            return;
        }
        BlockState newState = pair.getFirst();
        CompoundTag newEntityTag = pair.getSecond();
        Packet<ClientGamePacketListener> newPacket = RuntimeHelper.buildBlockUpdatePacket(pos, newState);
        Minecraft.getInstance().execute(() -> {
            ChunkAccess chunk = Optional.ofNullable(Minecraft.getInstance().level).map(it -> it.getChunk(pos)).orElse(null);
            if (!(chunk instanceof CocoonLevelChunk)) {
                return;
            }
            CocoonLevelChunk originalChunk = (CocoonLevelChunk) chunk;
            newPacket.handle(packetListener);
            originalChunk.cocoon$setOriginalBlockState(pos, oldState);
            if (newEntityTag != null) {
                ResourceLocation type = RuntimeHelper.getBlockEntityKey(chunk, pos);
                Packet<ClientGamePacketListener> newDataPacket = RuntimeHelper.buildBlockEntityDataPacket(pos, type, newEntityTag);
                newDataPacket.handle(packetListener);
            }
        });
    }

    private static void handleSectionUpdate(ClientGamePacketListener packetListener, FriendlyByteBuf payload) {
        int size = payload.readVarInt();
        for (int i = 0; i < size; ++i) {
            handleBlockChange(packetListener, payload);
        }
    }

    private static void handleBlockData(ClientGamePacketListener packetListener, FriendlyByteBuf payload) {
        BlockPos pos = payload.readBlockPos();
        ResourceLocation type = payload.readResourceLocation();
        CompoundTag tag = payload.readNbt();
        Packet<ClientGamePacketListener> newPacket = RuntimeHelper.buildBlockEntityDataPacket(pos, type, tag);
        Minecraft.getInstance().execute(() -> {
            newPacket.handle(packetListener);
        });
    }
}


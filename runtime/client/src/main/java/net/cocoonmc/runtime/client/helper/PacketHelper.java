package net.cocoonmc.runtime.client.helper;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import net.cocoonmc.runtime.client.api.CocoonLevelChunk;
import net.cocoonmc.runtime.client.api.CocoonPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Optional;
import java.util.function.BiConsumer;

public class PacketHelper {

    private static final ResourceLocation COCOON_PLAY = new ResourceLocation("cocoon", "play");

    private static final ImmutableMap<Integer, BiConsumer<Connection, FriendlyByteBuf>> HANDLERS = ImmutableMap.<Integer, BiConsumer<Connection, FriendlyByteBuf>>builder()
            .put(0, PacketHelper::handleInit)
            .put(1, PacketHelper::handleBlockChange)
            .put(2, PacketHelper::handleBlockData)
            .put(3, PacketHelper::handleSectionUpdate)
            .put(4, PacketHelper::handleAddEntity)
            .put(5, PacketHelper::handleSetEntityData)
            .put(6, PacketHelper::handleOpenWindow)
            .build();

    public static boolean test(ResourceLocation id) {
        return id.equals(COCOON_PLAY);
    }

    public static void send(Packet<ClientGamePacketListener> packet) {
        Minecraft instance = Minecraft.getInstance();
        ClientGamePacketListener packetListener = instance.getConnection();
        if (packetListener == null) {
            return;
        }
        packet.handle(packetListener);
    }

    public static void handle(Connection connection, FriendlyByteBuf payload) {
        int id = payload.readVarInt();
        BiConsumer<Connection, FriendlyByteBuf> handler = HANDLERS.get(id);
        if (handler != null) {
            LogHelper.log("receive packet: " + id);
            handler.accept(connection, payload);
        }
    }

    private static void handleInit(Connection connection, FriendlyByteBuf payload) {
        int flags = payload.readVarInt();
        ItemHelper.setEnableRedirect((flags & 0x01) != 0);
    }

    private static void handleOpenWindow(Connection connection, FriendlyByteBuf payload) {
        switch (CocoonPlatform.get()) {
            case FORGE: {
                // send open menu packet to forge.
                ResourceLocation rl = payload.readResourceLocation();
                MenuType<?> menuType = RegistryHelper.MENU_TYPES.getOptional(rl).orElse(null);
                if (menuType != null) {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer(4 + payload.readableBytes()));
                    buf.writeVarInt(1); // open container
                    buf.writeVarInt(RegistryHelper.MENU_TYPES.getId(menuType)); // menu type
                    buf.writeVarInt(payload.readVarInt()); // window id
                    buf.writeComponent(payload.readComponent()); // title
                    buf.writeVarInt(payload.readableBytes()); // payload length
                    buf.writeBytes(payload); // payload
                    send(RuntimeHelper.buildCustomPayloadPacket(new ResourceLocation("fml", "play"), buf));
                }
                break;
            }
            case FABRIC: {
                // send open menu packet to fabric.
                FriendlyByteBuf buf = new FriendlyByteBuf(payload.duplicate());
                send(RuntimeHelper.buildCustomPayloadPacket(new ResourceLocation("fabric-screen-handler-api-v1", "open_screen"), buf));
                break;
            }
            case UNKNOWN: {
                // !!! unknown platform.
                break;
            }
        }
    }

    private static void handleBlockChange(Connection connection, FriendlyByteBuf payload) {
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
            send(newPacket);
            originalChunk.cocoon$setOriginalBlockState(pos, oldState);
            if (newEntityTag != null) {
                BlockEntity blockEntity = chunk.getBlockEntity(pos);
                if (blockEntity == null) {
                    return;
                }
                BlockEntityType<?> type = blockEntity.getType();
                send(RuntimeHelper.buildBlockEntityDataPacket(pos, type, newEntityTag));
            }
        });
    }

    private static void handleSectionUpdate(Connection connection, FriendlyByteBuf payload) {
        int size = payload.readVarInt();
        for (int i = 0; i < size; ++i) {
            handleBlockChange(connection, payload);
        }
    }

    private static void handleBlockData(Connection connection, FriendlyByteBuf payload) {
        BlockPos pos = payload.readBlockPos();
        ResourceLocation rl = payload.readResourceLocation();
        CompoundTag tag = payload.readNbt();
        BlockEntityType<?> type = RegistryHelper.BLOCK_ENTITY_TYPES.getOptional(rl).orElse(null);
        if (type == null) {
            return;
        }
        send(RuntimeHelper.buildBlockEntityDataPacket(pos, type, tag));
    }

    private static void handleAddEntity(Connection connection, FriendlyByteBuf payload) {
        int entityId = payload.readVarInt();
        CompoundTag tag = payload.readNbt();
        EntityHelper.PENDING_NEW_ENTITIES.put(entityId, tag);
    }

    private static void handleSetEntityData(Connection connection, FriendlyByteBuf payload) {
        int entityId = payload.readVarInt();
        EntityHelper.DataList dataList = EntityHelper.createDataList(payload);
        if (dataList.isEmpty()) {
            return;
        }
        Minecraft.getInstance().execute(() -> {
            Entity entity = Optional.ofNullable(Minecraft.getInstance().level).map(it -> it.getEntity(entityId)).orElse(null);
            if (entity == null) {
                return;
            }
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer(payload.readableBytes()));
            buf.writeVarInt(entityId);
            for (EntityHelper.DataValue it : dataList.freeze(entity)) {
                buf.writeByte(it.getId());
                buf.writeVarInt(it.getSerializerId());
                buf.writeBytes(it.getValue());
            }
            buf.writeByte(255);
            send(RuntimeHelper.buildSetEntityDataPacket(buf));
        });
    }
}


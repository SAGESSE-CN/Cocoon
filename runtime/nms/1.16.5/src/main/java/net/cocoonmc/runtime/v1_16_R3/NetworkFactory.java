package net.cocoonmc.runtime.v1_16_R3;

import io.netty.channel.Channel;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.ReflectHelper;
import net.cocoonmc.runtime.INetworkFactory;
import net.minecraft.server.v1_16_R3.Block;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NetworkManager;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketListener;
import net.minecraft.server.v1_16_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_16_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_16_R3.PacketPlayOutMapChunk;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class NetworkFactory extends TransformFactory implements INetworkFactory {

    private static final ReflectHelper.Member<Integer> ADD_ENTITY_GET_ID = ReflectHelper.getMemberField(PacketPlayOutSpawnEntity.class, "a");
    private static final ReflectHelper.Member<BlockPosition> CHANGE_GET_POS = ReflectHelper.getMemberField(PacketPlayOutBlockChange.class, "a");

    private static final ReflectHelper.Member<Integer> CHUNK_GET_X = ReflectHelper.getMemberField(PacketPlayOutMapChunk.class, "a");
    private static final ReflectHelper.Member<Integer> CHUNK_GET_Z = ReflectHelper.getMemberField(PacketPlayOutMapChunk.class, "b");
    private static final ReflectHelper.Member<NBTTagCompound> CHUNK_GET_HEIGHT_MAPS = ReflectHelper.getMemberField(PacketPlayOutMapChunk.class, "d");

    private static final ReflectHelper.Member<MinecraftKey> CUSTOM_GET_NAME = ReflectHelper.getMemberField(PacketPlayOutCustomPayload.class, "r");
    private static final ReflectHelper.Member<PacketDataSerializer> CUSTOM_GET_PAYLOAD = ReflectHelper.getMemberField(PacketPlayOutCustomPayload.class, "s");

    @Override
    public void register(net.cocoonmc.core.world.entity.Player player, Consumer<Channel> handler) {
        EntityPlayer player1 = convertToVanilla(player);
        NetworkManager connection = findConnectionByPlayer(player1);
        if (connection == null) {
            return;
        }
        Channel channel = connection.channel;
        channel.eventLoop().submit(() -> handler.accept(channel));
    }

//    @Override
//    public void broadcastTo(net.cocoonmc.core.network.packet.Packet packet, net.cocoonmc.core.world.chunk.Chunk chunk) {
//        WorldServer level = convertToVanilla(chunk.getLevel());
//        ChunkProviderServer cache = level.getChunkProvider();
//        cache.playerChunkMap.a(new ChunkCoordIntPair(chunk.getX(), chunk.getZ()), true).forEach(player -> {
//            player.playerConnection.sendPacket(unwrap(packet));
//        });
//    }

    @Override
    public net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket create(net.cocoonmc.core.resources.ResourceLocation identifier, net.cocoonmc.core.network.FriendlyByteBuf payload) {
        return wrap(new PacketPlayOutCustomPayload(convertToVanilla(identifier), convertToVanilla(payload)));
    }

    @Override
    public net.cocoonmc.core.network.protocol.Packet convertTo(Object packet) {
        if (packet instanceof PacketPlayOutMapChunk) {
            return wrap((PacketPlayOutMapChunk) packet);
        }
        if (packet instanceof PacketPlayOutBlockChange) {
            return wrap((PacketPlayOutBlockChange) packet);
        }
        if (packet instanceof PacketPlayOutCustomPayload) {
            return wrap((PacketPlayOutCustomPayload) packet);
        }
        if (packet instanceof PacketPlayOutSpawnEntity) {
            return wrap((PacketPlayOutSpawnEntity) packet);
        }
        return () -> packet;
    }

    public net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket wrap(PacketPlayOutCustomPayload packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket() {
            @Override
            public net.cocoonmc.core.resources.ResourceLocation getName() {
                return convertToCocoon(CUSTOM_GET_NAME.get(packet));
            }

            @Override
            public net.cocoonmc.core.network.FriendlyByteBuf getPayload() {
                return convertToCocoon(CUSTOM_GET_PAYLOAD.get(packet));
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ClientboundLevelChunkWithLightPacket wrap(PacketPlayOutMapChunk packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundLevelChunkWithLightPacket() {

            @Override
            public int getX() {
                return CHUNK_GET_X.get(packet);
            }

            @Override
            public int getZ() {
                return CHUNK_GET_Z.get(packet);
            }

            @Override
            public CompoundTag getHeightmaps() {
                return TagFactory.wrap(CHUNK_GET_HEIGHT_MAPS.get(packet));
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ClientboundBlockUpdatePacket wrap(PacketPlayOutBlockChange packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundBlockUpdatePacket() {

            @Override
            public BlockPos getPos() {
                return convertToCocoon(CHANGE_GET_POS.get(packet));
            }

            @Override
            public int getStateId() {
                return Block.getCombinedId(packet.block);
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ClientboundAddEntityPacket wrap(PacketPlayOutSpawnEntity packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundAddEntityPacket() {

            @Override
            public int getEntityId() {
                return ADD_ENTITY_GET_ID.get(packet);
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private Packet<PacketListener> unwrap(net.cocoonmc.core.network.protocol.Packet packet) {
        // noinspection unchecked
        return (Packet<PacketListener>) packet.getHandle();
    }

    @Nullable
    private static NetworkManager findConnectionByPlayer(EntityPlayer player) {
        return player.playerConnection.networkManager;
    }
}

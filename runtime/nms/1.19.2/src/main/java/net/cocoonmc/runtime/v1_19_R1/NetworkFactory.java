package net.cocoonmc.runtime.v1_19_R1;

import io.netty.channel.Channel;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.runtime.INetworkFactory;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.level.block.Block;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class NetworkFactory extends TransformFactory implements INetworkFactory {

    @Override
    public void register(net.cocoonmc.core.world.entity.Player player, Consumer<Channel> handler) {
        ServerPlayer player1 = convertToVanilla(player);
        Connection connection = findConnectionByPlayer(player1);
        if (connection == null) {
            return;
        }
        Channel channel = connection.channel;
        channel.eventLoop().submit(() -> handler.accept(channel));
    }

//    @Override
//    public void broadcastTo(net.cocoonmc.core.network.packet.Packet packet, net.cocoonmc.core.world.chunk.Chunk chunk) {
//        ServerLevel level = convertToVanilla(chunk.getLevel());
//        ServerChunkCache cache = level.getChunkSource();
//        ChunkPos pos = new ChunkPos(chunk.getX(), chunk.getZ());
//        cache.chunkMap.getPlayers(pos, false).forEach(player -> player.connection.send(unwrap(packet)));
//    }

    @Override
    public net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket create(net.cocoonmc.core.resources.ResourceLocation identifier, net.cocoonmc.core.network.FriendlyByteBuf payload) {
        return wrap(new ClientboundCustomPayloadPacket(convertToVanilla(identifier), convertToVanilla(payload)));
    }

    @Override
    public net.cocoonmc.core.network.protocol.Packet convertTo(Object packet) {
        if (packet instanceof ClientboundLevelChunkWithLightPacket) {
            return wrap((ClientboundLevelChunkWithLightPacket) packet);
        }
        if (packet instanceof ClientboundBlockUpdatePacket) {
            return wrap((ClientboundBlockUpdatePacket) packet);
        }
        if (packet instanceof ClientboundCustomPayloadPacket) {
            return wrap((ClientboundCustomPayloadPacket) packet);
        }
        if (packet instanceof ClientboundAddEntityPacket) {
            return wrap((ClientboundAddEntityPacket) packet);
        }
        return () -> packet;
    }

    public net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket wrap(ClientboundCustomPayloadPacket packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket() {
            @Override
            public net.cocoonmc.core.resources.ResourceLocation getName() {
                return convertToCocoon(packet.getIdentifier());
            }

            @Override
            public net.cocoonmc.core.network.FriendlyByteBuf getPayload() {
                return convertToCocoon(packet.getData());
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ClientboundLevelChunkWithLightPacket wrap(ClientboundLevelChunkWithLightPacket packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundLevelChunkWithLightPacket() {

            @Override
            public int getX() {
                return packet.getX();
            }

            @Override
            public int getZ() {
                return packet.getZ();
            }

            @Override
            public CompoundTag getHeightmaps() {
                return TagFactory.wrap(packet.getChunkData().getHeightmaps());
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ClientboundBlockUpdatePacket wrap(ClientboundBlockUpdatePacket packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundBlockUpdatePacket() {

            @Override
            public BlockPos getPos() {
                return convertToCocoon(packet.getPos());
            }

            @Override
            public int getStateId() {
                return Block.getId(packet.blockState);
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ClientboundAddEntityPacket wrap(ClientboundAddEntityPacket packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundAddEntityPacket() {

            @Override
            public int getEntityId() {
                return packet.getId();
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private Packet<ClientGamePacketListener> unwrap(net.cocoonmc.core.network.protocol.Packet packet) {
        // noinspection unchecked
        return (Packet<ClientGamePacketListener>) packet.getHandle();
    }

    @Nullable
    private static Connection findConnectionByPlayer(ServerPlayer player) {
        ServerConnectionListener listener = ((CraftServer) Bukkit.getServer()).getServer().getConnection();
        if (listener == null) {
            return null;
        }
        for (Connection connection : listener.getConnections()) {
            PacketListener packetListener = connection.getPacketListener();
            if (packetListener instanceof ServerPlayerConnection) {
                ServerPlayer targetPlayer = ((ServerPlayerConnection) packetListener).getPlayer();
                if (player.equals(targetPlayer)) {
                    return connection;
                }
            }
        }
        return null;
    }
}

package net.cocoonmc.runtime.v1_18_R2;

import io.netty.channel.Channel;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.math.Vector3d;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.PacketMap;
import net.cocoonmc.runtime.INetworkFactory;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.level.block.Block;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class NetworkFactory extends TransformFactory implements INetworkFactory {

    private static final PacketMap<Object, net.cocoonmc.core.network.protocol.Packet> MAP = new PacketMap<>(it -> {
        it.put(ClientboundLevelChunkWithLightPacket.class, NetworkFactory::wrap);
        it.put(ClientboundBlockUpdatePacket.class, NetworkFactory::wrap);
        it.put(ClientboundSectionBlocksUpdatePacket.class, NetworkFactory::wrap);
        it.put(ClientboundCustomPayloadPacket.class, NetworkFactory::wrap);
        it.put(ClientboundAddEntityPacket.class, NetworkFactory::wrap);
        it.put(ClientboundSetEntityDataPacket.class, NetworkFactory::wrap);
        it.put(ClientboundAddPlayerPacket.class, NetworkFactory::wrap);
        it.put(ClientboundPlayerPositionPacket.class, NetworkFactory::wrap);
        it.put(ServerboundMovePlayerPacket.Pos.class, NetworkFactory::wrap);
        it.put(ServerboundMovePlayerPacket.PosRot.class, NetworkFactory::wrap);
    });

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

    @Override
    public void sendTo(net.cocoonmc.core.network.protocol.Packet packet, net.cocoonmc.core.world.entity.Player player) {
        ServerPlayer serverPlayer = convertToVanilla(player);
        serverPlayer.connection.send(unwrap(packet));
    }

    @Override
    public void sendToAll(net.cocoonmc.core.network.protocol.Packet packet) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        server.getPlayerList().broadcastAll(unwrap(packet));
    }

    @Override
    public void sendToTracking(net.cocoonmc.core.network.protocol.Packet packet, net.cocoonmc.core.world.entity.Entity entity) {
        ServerLevel serverLevel = convertToVanilla(entity.getLevel());
        serverLevel.getChunkSource().broadcast(convertToVanilla(entity), unwrap(packet));
    }

    @Override
    public net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket create(net.cocoonmc.core.resources.ResourceLocation identifier, net.cocoonmc.core.network.FriendlyByteBuf payload) {
        return wrap(new ClientboundCustomPayloadPacket(convertToVanilla(identifier), convertToVanilla(payload)));
    }

    @Override
    public net.cocoonmc.core.network.protocol.Packet convertTo(Object packet) {
        return MAP.transform(packet, () -> () -> packet);
    }

    public static net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket wrap(ClientboundCustomPayloadPacket packet) {
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

    private static net.cocoonmc.core.network.protocol.ClientboundSectionBlocksUpdatePacket wrap(ClientboundSectionBlocksUpdatePacket packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundSectionBlocksUpdatePacket() {

            @Override
            public Map<BlockPos, Integer> getChanges() {
                HashMap<BlockPos, Integer> changes = new HashMap<>();
                packet.runUpdates((pos, state) -> changes.put(convertToCocoon(pos), Block.getId(state)));
                return changes;
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
            public int getId() {
                return packet.getId();
            }

            @Override
            public UUID getUUID() {
                return packet.getUUID();
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ClientboundSetEntityDataPacket wrap(ClientboundSetEntityDataPacket packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundSetEntityDataPacket() {

            @Override
            public int getId() {
                return packet.getId();
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ClientboundAddEntityPacket wrap(ClientboundAddPlayerPacket packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundAddEntityPacket() {

            @Override
            public int getId() {
                return packet.getEntityId();
            }

            @Override
            public UUID getUUID() {
                return packet.getPlayerId();
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ClientboundPlayerPositionPacket wrap(ClientboundPlayerPositionPacket packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundPlayerPositionPacket() {

            @Override
            public net.cocoonmc.core.network.protocol.ClientboundPlayerPositionPacket setPos(Vector3d pos) {
                double x = pos.getX();
                double y = pos.getY();
                double z = pos.getZ();
                return wrap(new ClientboundPlayerPositionPacket(x, y, z, packet.getYRot(), packet.getXRot(), packet.getRelativeArguments(), packet.getId(), packet.requestDismountVehicle()));
            }

            @Override
            public Vector3d getPos() {
                return new Vector3d(packet.getX(), packet.getY(), packet.getZ());
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ServerboundMovePlayerPacket wrap(ServerboundMovePlayerPacket packet) {
        return new net.cocoonmc.core.network.protocol.ServerboundMovePlayerPacket() {

            @Override
            public net.cocoonmc.core.network.protocol.ServerboundMovePlayerPacket setPos(Vector3d pos) {
                double x = pos.getX();
                double y = pos.getY();
                double z = pos.getZ();
                if (packet.hasPosition() && packet.hasRotation()) {
                    return wrap(new ServerboundMovePlayerPacket.PosRot(x, y, z, packet.yRot, packet.xRot, packet.isOnGround()));
                }
                return wrap(new ServerboundMovePlayerPacket.Pos(x, y, z, packet.isOnGround()));
            }

            @Override
            public Vector3d getPos() {
                return new Vector3d(packet.x, packet.y, packet.z);
            }

            @Override
            public Object getHandle() {
                return packet;
            }

            @Override
            public void applyTo(net.cocoonmc.core.world.entity.Player player) {
                Vector3d pos = getPos();
                MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
                ServerPlayer serverPlayer = convertToVanilla(player);
                server.submit(() -> serverPlayer.moveTo(pos.getX(), pos.getY(), pos.getZ()));
            }
        };
    }

    private static <T> T unwrap(net.cocoonmc.core.network.protocol.Packet packet) {
        // noinspection unchecked
        return (T) packet.getHandle();
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

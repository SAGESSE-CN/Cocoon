package net.cocoonmc.runtime.v1_16_R3;

import io.netty.channel.Channel;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.math.Vector3d;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.PacketMap;
import net.cocoonmc.core.utils.ReflectHelper;
import net.cocoonmc.runtime.INetworkFactory;
import net.minecraft.server.v1_16_R3.Block;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NetworkManager;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayInFlying;
import net.minecraft.server.v1_16_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_16_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutMapChunk;
import net.minecraft.server.v1_16_R3.PacketPlayOutMultiBlockChange;
import net.minecraft.server.v1_16_R3.PacketPlayOutPosition;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class NetworkFactory extends TransformFactory implements INetworkFactory {

    private static final ReflectHelper.Member<Integer> ENTITY_ADD_GET_ID = ReflectHelper.getMemberField(PacketPlayOutSpawnEntity.class, "a");
    private static final ReflectHelper.Member<UUID> ENTITY_ADD_GET_UUID = ReflectHelper.getMemberField(PacketPlayOutSpawnEntity.class, "b");
    private static final ReflectHelper.Member<Integer> ENTITY_SET_GET_ID = ReflectHelper.getMemberField(PacketPlayOutEntityMetadata.class, "a");

    private static final ReflectHelper.Member<Integer> LIVING_ENTITY_ADD_GET_ID = ReflectHelper.getMemberField(PacketPlayOutSpawnEntityLiving.class, "a");
    private static final ReflectHelper.Member<UUID> LIVING_ENTITY_ADD_GET_UUID = ReflectHelper.getMemberField(PacketPlayOutSpawnEntityLiving.class, "b");

    private static final ReflectHelper.Member<BlockPosition> CHANGE_GET_POS = ReflectHelper.getMemberField(PacketPlayOutBlockChange.class, "a");

    private static final ReflectHelper.Member<Integer> CHUNK_GET_X = ReflectHelper.getMemberField(PacketPlayOutMapChunk.class, "a");
    private static final ReflectHelper.Member<Integer> CHUNK_GET_Z = ReflectHelper.getMemberField(PacketPlayOutMapChunk.class, "b");
    private static final ReflectHelper.Member<NBTTagCompound> CHUNK_GET_HEIGHT_MAPS = ReflectHelper.getMemberField(PacketPlayOutMapChunk.class, "d");

    private static final ReflectHelper.Member<Integer> MOVE_IN_GET_ID = ReflectHelper.getMemberField(PacketPlayOutPosition.class, "g");
    private static final ReflectHelper.Member<Double> MOVE_IN_GET_X = ReflectHelper.getMemberField(PacketPlayOutPosition.class, "a");
    private static final ReflectHelper.Member<Double> MOVE_IN_GET_Y = ReflectHelper.getMemberField(PacketPlayOutPosition.class, "b");
    private static final ReflectHelper.Member<Double> MOVE_IN_GET_Z = ReflectHelper.getMemberField(PacketPlayOutPosition.class, "c");
    private static final ReflectHelper.Member<Float> MOVE_IN_GET_YR = ReflectHelper.getMemberField(PacketPlayOutPosition.class, "d");
    private static final ReflectHelper.Member<Float> MOVE_IN_GET_XR = ReflectHelper.getMemberField(PacketPlayOutPosition.class, "e");
    private static final ReflectHelper.Member<Set<PacketPlayOutPosition.EnumPlayerTeleportFlags>> MOVE_IN_GET_FLAGS = ReflectHelper.getMemberField(PacketPlayOutPosition.class, "f");

    private static final ReflectHelper.Member<MinecraftKey> CUSTOM_GET_NAME = ReflectHelper.getMemberField(PacketPlayOutCustomPayload.class, "r");
    private static final ReflectHelper.Member<PacketDataSerializer> CUSTOM_GET_PAYLOAD = ReflectHelper.getMemberField(PacketPlayOutCustomPayload.class, "s");

    private static final PacketMap<Object, net.cocoonmc.core.network.protocol.Packet> MAP = new PacketMap<>(it -> {
        it.put(PacketPlayOutMapChunk.class, NetworkFactory::wrap);
        it.put(PacketPlayOutBlockChange.class, NetworkFactory::wrap);
        it.put(PacketPlayOutMultiBlockChange.class, NetworkFactory::wrap);
        it.put(PacketPlayOutCustomPayload.class, NetworkFactory::wrap);
        it.put(PacketPlayOutSpawnEntity.class, NetworkFactory::wrap);
        it.put(PacketPlayOutSpawnEntityLiving.class, NetworkFactory::wrap);
        it.put(PacketPlayOutEntityMetadata.class, NetworkFactory::wrap);
        it.put(PacketPlayOutPosition.class, NetworkFactory::wrap);
        it.put(PacketPlayInFlying.PacketPlayInPosition.class, NetworkFactory::wrap);
        it.put(PacketPlayInFlying.PacketPlayInPositionLook.class, NetworkFactory::wrap);
    });

    @Override
    public void register(net.cocoonmc.core.world.entity.Player player, Consumer<Channel> handler) {
        EntityPlayer serverPlayer = convertToVanilla(player);
        NetworkManager connection = findConnectionByPlayer(serverPlayer);
        if (connection == null) {
            return;
        }
        Channel channel = connection.channel;
        channel.eventLoop().submit(() -> handler.accept(channel));
    }

    @Override
    public void sendTo(net.cocoonmc.core.network.protocol.Packet packet, net.cocoonmc.core.world.entity.Player player) {
        EntityPlayer serverPlayer = convertToVanilla(player);
        serverPlayer.playerConnection.sendPacket(unwrap(packet));
    }

    @Override
    public void sendToAll(net.cocoonmc.core.network.protocol.Packet packet) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        server.getPlayerList().sendAll(unwrap(packet));
    }

    @Override
    public void sendToTracking(net.cocoonmc.core.network.protocol.Packet packet, net.cocoonmc.core.world.entity.Entity entity) {
        WorldServer serverLevel = convertToVanilla(entity.getLevel());
        serverLevel.getChunkProvider().broadcast(convertToVanilla(entity), unwrap(packet));
    }

    @Override
    public net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket create(net.cocoonmc.core.resources.ResourceLocation identifier, net.cocoonmc.core.network.FriendlyByteBuf payload) {
        return wrap(new PacketPlayOutCustomPayload(convertToVanilla(identifier), convertToVanilla(payload)));
    }

    @Override
    public net.cocoonmc.core.network.protocol.Packet convertTo(Object packet) {
        return MAP.transform(packet, () -> () -> packet);
    }

    public static net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket wrap(PacketPlayOutCustomPayload packet) {
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

    private static net.cocoonmc.core.network.protocol.ClientboundSectionBlocksUpdatePacket wrap(PacketPlayOutMultiBlockChange packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundSectionBlocksUpdatePacket() {

            @Override
            public Map<BlockPos, Integer> getChanges() {
                HashMap<BlockPos, Integer> changes = new HashMap<>();
                packet.a((pos, state) -> changes.put(convertToCocoon(pos), Block.getCombinedId(state)));
                return changes;
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
            public int getId() {
                return ENTITY_ADD_GET_ID.get(packet);
            }

            @Override
            public UUID getUUID() {
                return ENTITY_ADD_GET_UUID.get(packet);
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ClientboundSetEntityDataPacket wrap(PacketPlayOutEntityMetadata packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundSetEntityDataPacket() {

            @Override
            public int getId() {
                return ENTITY_SET_GET_ID.get(packet);
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ClientboundAddEntityPacket wrap(PacketPlayOutSpawnEntityLiving packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundAddEntityPacket() {

            @Override
            public int getId() {
                return LIVING_ENTITY_ADD_GET_ID.get(packet);
            }

            @Override
            public UUID getUUID() {
                return LIVING_ENTITY_ADD_GET_UUID.get(packet);
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ClientboundPlayerPositionPacket wrap(PacketPlayOutPosition packet) {
        return new net.cocoonmc.core.network.protocol.ClientboundPlayerPositionPacket() {

            @Override
            public net.cocoonmc.core.network.protocol.ClientboundPlayerPositionPacket setPos(Vector3d pos) {
                double x = pos.getX();
                double y = pos.getY();
                double z = pos.getZ();
                float yRot = MOVE_IN_GET_YR.get(packet);
                float xRot = MOVE_IN_GET_XR.get(packet);
                return wrap(new PacketPlayOutPosition(x, y, z, yRot, xRot, MOVE_IN_GET_FLAGS.get(packet), MOVE_IN_GET_ID.get(packet)));
            }

            @Override
            public Vector3d getPos() {
                double x = MOVE_IN_GET_X.get(packet);
                double y = MOVE_IN_GET_Y.get(packet);
                double z = MOVE_IN_GET_Z.get(packet);
                return new Vector3d(x, y, z);
            }

            @Override
            public Object getHandle() {
                return packet;
            }
        };
    }

    private static net.cocoonmc.core.network.protocol.ServerboundMovePlayerPacket wrap(PacketPlayInFlying packet) {
        return new net.cocoonmc.core.network.protocol.ServerboundMovePlayerPacket() {

            @Override
            public net.cocoonmc.core.network.protocol.ServerboundMovePlayerPacket setPos(Vector3d pos) {
                double x = pos.getX();
                double y = pos.getY();
                double z = pos.getZ();
                if (packet.hasPos && packet.hasLook) {
                    PacketPlayInFlying packet2 = new PacketPlayInFlying.PacketPlayInPositionLook();
                    packet2.x = x;
                    packet2.y = y;
                    packet2.z = z;
                    packet2.yaw = packet.yaw;
                    packet2.pitch = packet.pitch;
                    return wrap(packet2);
                }
                PacketPlayInFlying packet2 = new PacketPlayInFlying.PacketPlayInPosition();
                packet2.x = x;
                packet2.y = y;
                packet2.z = z;
                packet2.yaw = packet.yaw;
                packet2.pitch = packet.pitch;
                return wrap(packet2);
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
                EntityPlayer serverPlayer = convertToVanilla(player);
                server.b(() -> serverPlayer.setPosition(pos.getX(), pos.getY(), pos.getZ()));
            }
        };
    }

    private <T> T unwrap(net.cocoonmc.core.network.protocol.Packet packet) {
        // noinspection unchecked
        return (T) packet.getHandle();
    }

    @Nullable
    private static NetworkManager findConnectionByPlayer(EntityPlayer player) {
        return player.playerConnection.networkManager;
    }
}

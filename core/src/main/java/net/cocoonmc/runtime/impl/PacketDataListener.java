package net.cocoonmc.runtime.impl;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.math.CollissionBox;
import net.cocoonmc.core.math.Vector3d;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.ListTag;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.network.protocol.ClientboundAddEntityPacket;
import net.cocoonmc.core.network.protocol.ClientboundBlockUpdatePacket;
import net.cocoonmc.core.network.protocol.ClientboundBundlePacket;
import net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket;
import net.cocoonmc.core.network.protocol.ClientboundLevelChunkWithLightPacket;
import net.cocoonmc.core.network.protocol.ClientboundPlayerPositionPacket;
import net.cocoonmc.core.network.protocol.ClientboundSectionBlocksUpdatePacket;
import net.cocoonmc.core.network.protocol.Packet;
import net.cocoonmc.core.network.protocol.ServerboundMovePlayerPacket;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.utils.Pair;
import net.cocoonmc.core.utils.ThrowableConsumer;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PacketDataListener implements Listener {

    private static final ConcurrentHashMap<Integer, Pair<Vector3d, Vector3d>> OFFSETS = new ConcurrentHashMap<>();

    //private static final AttributeKey<Object> PACKET_KEY = AttributeKey.valueOf("cocoon_packet");
    private static final AttributeKey<Player> OWNER_KEY = AttributeKey.valueOf("cocoon_owner");

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        // add a new packet handler into player connection.
        Player player = Player.of(event.getPlayer());
        player.addChannel(Constants.NETWORK_KEY);
        Cocoon.API.NETWORK.register(player, channel -> {
            ChannelPipeline pipeline = channel.pipeline();
            ChannelHandler handler = pipeline.get(Constants.PACKET_HANDLER_KEY);
            if (handler != null) {
                return; // already added.
            }
            channel.attr(OWNER_KEY).set(player);
            pipeline.addBefore("packet_handler", Constants.PACKET_HANDLER_KEY, new WrappedPacketHandler());
            // send init packet.
            FriendlyByteBuf buffer = new FriendlyByteBuf();
            buffer.writeVarInt(0);  // init
            buffer.writeVarInt(0x01); // options
            channel.write(ClientboundCustomPayloadPacket.create(buffer).getHandle());
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
//        // add a new packet handler into player connection.
//        Player player = Player.of(event.getPlayer());
//        player.addChannel(Constants.NETWORK_KEY);
//        // send init packet.
//        FriendlyByteBuf buffer = new FriendlyByteBuf();
//        buffer.writeVarInt(0);  // init
//        buffer.writeVarInt(0x01); // options
//        BukkitHelper.sendCustomPacket(event.getPlayer(), buffer);
    }

    public static Packet handleChunkUpdate(ClientboundLevelChunkWithLightPacket packet, Player player) {
        ListTag tag = LevelData.getClientChunk(player, packet.getX(), packet.getZ());
        if (tag == null) {
            return packet;
        }
        //Logs.debug("{} patch chunk ({},{})", player.getLevel().getName(), packet.getX(), packet.getZ());
        packet.getHeightmaps().put(Constants.CHUNK_REDIRECTED_KEY, tag);
        return packet;
    }

    public static Packet handleBlockUpdate(ClientboundBlockUpdatePacket packet, Player player) {
        CompoundTag tag = LevelData.getClientBlock(player, packet.getPos());
        if (tag == null) {
            return packet;
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.directBuffer());
        buf.writeVarInt(1); // block update
        buf.writeVarInt(packet.getStateId());
        buf.writeNbt(tag);
        Packet pre = ClientboundCustomPayloadPacket.create(buf);
        //Logs.debug("{} patch block {} => {}", player.getLevel().getName(), packet.getPos(), tag);
        return ClientboundBundlePacket.create(Lists.newArrayList(pre, packet));
    }

    public static Packet handleSectionUpdate(ClientboundSectionBlocksUpdatePacket packet, Player player) {
        ArrayList<Pair<Integer, CompoundTag>> pairs = new ArrayList<>();
        for (Map.Entry<BlockPos, Integer> entry : packet.getChanges().entrySet()) {
            CompoundTag tag = LevelData.getClientBlock(player, entry.getKey());
            if (tag != null) {
                pairs.add(Pair.of(entry.getValue(), tag));
            }
        }
        if (pairs.isEmpty()) {
            return packet;
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.directBuffer());
        buf.writeVarInt(3); // section update
        buf.writeVarInt(pairs.size());
        for (Pair<Integer, CompoundTag> pair : pairs) {
            buf.writeVarInt(pair.getFirst());
            buf.writeNbt(pair.getSecond());
        }
        Packet pre = ClientboundCustomPayloadPacket.create(buf);
        //Logs.debug("{} patch section {}", player.getLevel().getName(), pairs.stream().map(Pair::getSecond).collect(Collectors.toList()));
        return ClientboundBundlePacket.create(Lists.newArrayList(pre, packet));
    }

    public static Packet handlePlayerMove(ClientboundPlayerPositionPacket packet, Player player) {
        Pair<Vector3d, Vector3d> pair = OFFSETS.get(player.getId());
        if (pair == null) {
            return packet;
        }
        Vector3d clientPos = pair.getFirst();
        Vector3d lastPos = pair.getSecond();
        Vector3d serverPos = packet.getPos();
        if (!lastPos.equals(serverPos)) {
            //Logs.debug("{} patch move out fail, server changed pos!", player.getUUID());
            return packet;
        }
        //Logs.debug("{} patch move out {} => {} ", player.getUUID(), serverPos, clientPos);
        return packet.setPos(clientPos);
    }

    public static Packet handlePlayerMove(ServerboundMovePlayerPacket packet, Player player) {
        int playerId = player.getId();
        Vector3d clientPos = packet.getPos();
        Pair<Vector3d, Vector3d> pair = OFFSETS.get(playerId);
        if (pair != null && pair.getFirst().equals(clientPos)) {
            //Logs.debug("{} not location changes, keep last pos {}", player.getUUID(), pair.getSecond());
            return packet.setPos(pair.getSecond());
        }
        Vector3d lastPos = player.getLocation();
        if (pair == null && lastPos.equals(clientPos)) {
            //Logs.debug("{} not location changes, apply the client pos {}", player.getUUID(), clientPos);
            return packet;
        }
        CollissionBox box = LevelData.getClientBlockCollisions(player, clientPos);
        if (box == null) {
            OFFSETS.remove(playerId);
            if (pair != null && pair.getFirst().distanceTo(clientPos) <= 1) {
                //Logs.debug("{} reset location {}", player.getUUID(), pair.getFirst());
                packet.applyTo(player);
            }
            return packet;
        }
        if (pair != null && pair.getFirst().distanceTo(clientPos) > 1) {
            //Logs.debug("{} patch move in fail, client move too fast!", player.getUUID(), clientPos);
            return packet;
        }
        if (pair == null && lastPos.distanceTo(clientPos) > 1) {
            //Logs.debug("{} patch move in fail, client move too fast!!", player.getUUID(), clientPos);
            return packet;
        }
        OFFSETS.put(playerId, Pair.of(clientPos, lastPos));
        //Logs.debug("{} patch move in {} => {}", player.getUUID(), clientPos, lastPos);
        return packet.setPos(lastPos);
    }

    public static Packet handleAddEntity(ClientboundAddEntityPacket packet, Player player) {
        int entityId = packet.getId();
        CompoundTag tag = LevelData.getClientEntity(player, entityId);
        if (tag == null) {
            return packet;
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.directBuffer());
        buf.writeVarInt(4); // add entity
        buf.writeVarInt(entityId);
        buf.writeNbt(tag);
        Packet pre = ClientboundCustomPayloadPacket.create(buf);
        Logs.debug("{} patch entity {} => {}", player.getUUID(), entityId, tag);
        return ClientboundBundlePacket.create(Lists.newArrayList(pre, packet));
    }

    public static class WrappedPacketHandler extends ChannelDuplexHandler {

        @Override
        public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
            Player owner = ctx.channel().attr(OWNER_KEY).get();
            if (owner != null) {
                Packet oldPacket = Cocoon.API.NETWORK.convertTo(msg);
                Packet newPacket = Cocoon.API.TRANSFORMER.transform(oldPacket, owner);
                if (newPacket != oldPacket) {
                    msg = newPacket.getHandle();
                }
            }
            super.channelRead(ctx, msg);
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            Player owner = ctx.channel().attr(OWNER_KEY).get();
            if (owner != null) {
                Packet oldPacket = Cocoon.API.NETWORK.convertTo(msg);
                Packet newPacket = Cocoon.API.TRANSFORMER.transform(oldPacket, owner);
                if (newPacket != oldPacket) {
                    msg = expand(newPacket.getHandle(), it -> {
                        // we need to ensure sequential.
                        super.write(ctx, it, ctx.voidPromise());
                    });
                }
            }
            super.write(ctx, msg, promise);
        }

        private Object expand(Object msg, ThrowableConsumer<Object> handler) throws Exception {
            if (msg instanceof List<?>) {
                List<?> packets = (List<?>) msg;
                for (Object packet : packets) {
                    if (msg != packets) {
                        handler.accept(msg);
                    }
                    msg = packet;
                }
            }
            return msg;
        }
    }

//    public static class WrappedPacketEncoder extends ChannelOutboundHandlerAdapter {
//
//        @Override
//        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//            Object packet = ctx.channel().attr(PACKET_KEY).getAndSet(null);
//            if (packet instanceof IVanillaBlockUpdatePacket) {
//                Player owner = ctx.channel().attr(SENDER_KEY).get();
//                if (owner != null) {
//                    msg = attachBlockTo((IVanillaBlockUpdatePacket) packet, owner, (ByteBuf) msg);
//                }
//            }
//            super.write(ctx, msg, promise);
//        }
//    }
}

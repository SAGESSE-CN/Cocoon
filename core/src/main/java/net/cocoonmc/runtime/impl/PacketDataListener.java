package net.cocoonmc.runtime.impl;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.ListTag;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.network.protocol.ClientboundBlockUpdatePacket;
import net.cocoonmc.core.network.protocol.ClientboundBundlePacket;
import net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket;
import net.cocoonmc.core.network.protocol.ClientboundLevelChunkWithLightPacket;
import net.cocoonmc.core.network.protocol.Packet;
import net.cocoonmc.core.utils.ThrowableConsumer;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class PacketDataListener implements Listener {

    //private static final AttributeKey<Object> PACKET_KEY = AttributeKey.valueOf("cocoon_packet");
    private static final AttributeKey<Player> SENDER_KEY = AttributeKey.valueOf("cocoon_sender");

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        // add a new packet handler into player connection.
        Player player = Player.of(event.getPlayer());
        player.addChannel("cocoon:play");
        Cocoon.API.NETWORK.register(player, channel -> {
            ChannelPipeline pipeline = channel.pipeline();
            ChannelHandler handler = pipeline.get(Constants.PACKET_HANDLER_KEY);
            if (handler != null) {
                return; // already added.
            }
            channel.attr(SENDER_KEY).set(player);
            pipeline.addBefore("packet_handler", Constants.PACKET_HANDLER_KEY, new WrappedPacketHandler());
            //pipeline.addBefore("encoder", Constants.PACKET_ENCODER_KEY, new WrappedPacketEncoder());
        });
    }

    public static Packet handleChunkUpdate(ClientboundLevelChunkWithLightPacket packet, Player player) {
        ListTag tag = LevelData.getClientChunk(player.getLevel().getUUID(), packet.getX(), packet.getZ());
        if (tag != null) {
            packet.getHeightmaps().put(Constants.CHUNK_REDIRECTED_KEY, tag);
        }
        return packet;
    }

    public static Packet handleBlockUpdate(ClientboundBlockUpdatePacket packet, Player player) {
        CompoundTag tag = LevelData.getClientBlock(player.getLevel().getUUID(), packet.getPos());
        if (tag == null) {
            return packet;
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.directBuffer());
        buf.writeVarInt(1); // block update
        buf.writeVarInt(packet.getStateId());
        buf.writeNbt(tag);
        Packet pre = ClientboundCustomPayloadPacket.create(buf);
        return ClientboundBundlePacket.create(Lists.newArrayList(pre, packet));
    }

    public static class WrappedPacketHandler extends ChannelOutboundHandlerAdapter {

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            Player owner = ctx.channel().attr(SENDER_KEY).get();
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

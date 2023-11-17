package net.cocoonmc.core.utils;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;

public class PacketHelper {

    public static void sendTo(ResourceLocation channel, FriendlyByteBuf buffer, Player player) {
        Cocoon.API.NETWORK.sendTo(ClientboundCustomPayloadPacket.create(channel, buffer), player);
    }

    public static void sendToAll(ResourceLocation channel, FriendlyByteBuf buffer) {
        Cocoon.API.NETWORK.sendToAll(ClientboundCustomPayloadPacket.create(channel, buffer));
    }

    public static void sendToTracking(ResourceLocation channel, FriendlyByteBuf buffer, Entity entity) {
        Cocoon.API.NETWORK.sendToTracking(ClientboundCustomPayloadPacket.create(channel, buffer), entity);
    }

    public static void sendToTrackingAndSelf(ResourceLocation channel, FriendlyByteBuf buffer, Entity entity) {
        ClientboundCustomPayloadPacket packet = ClientboundCustomPayloadPacket.create(channel, buffer);
        Cocoon.API.NETWORK.sendToTracking(packet, entity);
        if (entity instanceof Player) {
            Cocoon.API.NETWORK.sendTo(packet, (Player) entity);
        }
    }
}

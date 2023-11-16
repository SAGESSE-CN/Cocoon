package net.cocoonmc.runtime;

import io.netty.channel.Channel;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket;
import net.cocoonmc.core.network.protocol.Packet;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;

import java.util.function.Consumer;

public interface INetworkFactory {

    void register(Player player, Consumer<Channel> handler);

    ClientboundCustomPayloadPacket create(ResourceLocation identifier, FriendlyByteBuf payload);

    Packet convertTo(Object object);

    void sendTo(Packet packet, Player player);

    void sendToAll(Packet packet);

    void sendToTracking(Packet packet, Entity entity);
}

package net.cocoonmc.runtime;

import io.netty.channel.Channel;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.network.protocol.ClientboundBundlePacket;
import net.cocoonmc.core.network.protocol.ClientboundCustomPayloadPacket;
import net.cocoonmc.core.network.protocol.Packet;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.world.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public interface INetworkFactory {

    void register(Player player, Consumer<Channel> handler);

    //void broadcastTo(Packet packet, Chunk chunk);

    ClientboundCustomPayloadPacket create(ResourceLocation identifier, FriendlyByteBuf payload);

    Packet convertTo(Object object);
}

package net.cocoonmc.core.network.protocol;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.runtime.impl.ConstantKeys;

public interface ClientboundCustomPayloadPacket extends Packet {

    static ClientboundCustomPayloadPacket create(FriendlyByteBuf payload) {
        return create(ConstantKeys.NETWORK_KEY, payload);
    }

    static ClientboundCustomPayloadPacket create(ResourceLocation name, FriendlyByteBuf payload) {
        return Cocoon.API.NETWORK.create(name, payload);
    }

    ResourceLocation getName();
}

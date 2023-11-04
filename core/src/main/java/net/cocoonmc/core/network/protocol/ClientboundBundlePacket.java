package net.cocoonmc.core.network.protocol;

import java.util.List;
import java.util.stream.Collectors;

public interface ClientboundBundlePacket extends Packet {

    static ClientboundBundlePacket create(List<Packet> packets) {
        return new ClientboundBundlePacket() {
            @Override
            public List<Packet> getPackets() {
                return packets;
            }

            @Override
            public List<Object> getHandle() {
                return packets.stream().map(Packet::getHandle).collect(Collectors.toList());
            }
        };
    }

    List<Packet> getPackets();
}

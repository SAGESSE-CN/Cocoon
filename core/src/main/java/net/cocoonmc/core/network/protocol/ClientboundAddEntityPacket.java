package net.cocoonmc.core.network.protocol;

import java.util.UUID;

public interface ClientboundAddEntityPacket extends Packet {

    int getId();

    UUID getUUID();
}

package net.cocoonmc.core.network.protocol;

import net.cocoonmc.core.BlockPos;

public interface ClientboundBlockUpdatePacket extends Packet {

    int getStateId();

    BlockPos getPos();
}

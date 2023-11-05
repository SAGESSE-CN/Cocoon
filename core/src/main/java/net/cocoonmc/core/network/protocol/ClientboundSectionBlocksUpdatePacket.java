package net.cocoonmc.core.network.protocol;

import net.cocoonmc.core.BlockPos;

import java.util.Map;

public interface ClientboundSectionBlocksUpdatePacket extends Packet {

    Map<BlockPos, Integer> getChanges();
}

package net.cocoonmc.core.network.protocol;

import net.cocoonmc.core.nbt.CompoundTag;

public interface ClientboundLevelChunkWithLightPacket extends Packet {

    int getX();

    int getZ();

    CompoundTag getHeightmaps();
}

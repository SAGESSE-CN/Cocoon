package net.cocoonmc.core.network.protocol;

import net.cocoonmc.core.math.Vector3d;

public interface ClientboundPlayerPositionPacket extends Packet {

    Vector3d getPos();

    ClientboundPlayerPositionPacket setPos(Vector3d pos);
}

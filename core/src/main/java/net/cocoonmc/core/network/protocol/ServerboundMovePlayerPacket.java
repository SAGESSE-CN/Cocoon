package net.cocoonmc.core.network.protocol;

import net.cocoonmc.core.math.Vector3d;
import net.cocoonmc.core.world.entity.Player;

public interface ServerboundMovePlayerPacket extends Packet {

    ServerboundMovePlayerPacket setPos(Vector3d pos);

    Vector3d getPos();

    void applyTo(Player player);
}

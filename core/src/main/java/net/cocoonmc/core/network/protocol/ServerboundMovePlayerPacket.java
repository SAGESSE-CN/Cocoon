package net.cocoonmc.core.network.protocol;

import net.cocoonmc.core.world.entity.Player;

public interface ServerboundMovePlayerPacket extends Packet {

    double getX();

    double getY();

    double getZ();

    boolean onGround();

    void applyTo(Player player);

    ServerboundMovePlayerPacket moveTo(double x, double y, double z, boolean onGround);
}

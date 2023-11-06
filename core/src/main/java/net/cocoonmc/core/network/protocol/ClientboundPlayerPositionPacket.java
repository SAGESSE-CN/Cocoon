package net.cocoonmc.core.network.protocol;

public interface ClientboundPlayerPositionPacket extends Packet {

    double getX();

    double getY();

    double getZ();

    ClientboundPlayerPositionPacket moveTo(double x, double y, double z);
}

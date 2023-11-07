package net.cocoonmc.core.network;

import net.cocoonmc.core.network.protocol.ClientboundAddEntityPacket;
import net.cocoonmc.core.network.protocol.ClientboundBlockUpdatePacket;
import net.cocoonmc.core.network.protocol.ClientboundBundlePacket;
import net.cocoonmc.core.network.protocol.ClientboundLevelChunkWithLightPacket;
import net.cocoonmc.core.network.protocol.ClientboundPlayerPositionPacket;
import net.cocoonmc.core.network.protocol.ClientboundSectionBlocksUpdatePacket;
import net.cocoonmc.core.network.protocol.Packet;
import net.cocoonmc.core.network.protocol.ServerboundMovePlayerPacket;
import net.cocoonmc.core.world.entity.Player;
import net.cocoonmc.runtime.impl.PacketDataListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class PacketTransformer {

    private final ConcurrentHashMap<Class<?>, List<Handler<Packet>>> registered = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<?>, Handler<Packet>> applying = new ConcurrentHashMap<>();

    public Packet transform(Packet packet, Player player) {
        return applying.computeIfAbsent(packet.getClass(), this::build).apply(packet, player);
    }

    public <T extends Packet> void register(Handler<T> handler, Class<T> type) {
        // noinspection unchecked
        Handler<Packet> packetHandler = (Handler<Packet>) handler;
        getOrCreate(type).add(packetHandler);
        applying.clear();
    }

    public <T extends Packet> void unregister(Handler<T> handler, Class<T> type) {
        getOrCreate(type).remove(handler);
        applying.clear();
    }

    public void enable() {
        register(this::unpack, ClientboundBundlePacket.class);
        register(PacketDataListener::handleChunkUpdate, ClientboundLevelChunkWithLightPacket.class);
        register(PacketDataListener::handleBlockUpdate, ClientboundBlockUpdatePacket.class);
        register(PacketDataListener::handleSectionUpdate, ClientboundSectionBlocksUpdatePacket.class);
        register(PacketDataListener::handleAddEntity, ClientboundAddEntityPacket.class);
        register(PacketDataListener::handlePlayerMove, ClientboundPlayerPositionPacket.class);
        register(PacketDataListener::handlePlayerMove, ServerboundMovePlayerPacket.class);
    }

    public void disable() {
        registered.clear();
        applying.clear();
    }

    private Packet unpack(ClientboundBundlePacket packet, Player player) {
        int changedPackets = 0;
        List<Packet> newPackets = new ArrayList<>();
        for (Packet oldPacket : packet.getPackets()) {
            Packet newPacket = transform(oldPacket, player);
            if (newPacket != oldPacket) {
                changedPackets += 1;
                if (newPacket instanceof ClientboundBundlePacket) {
                    newPackets.addAll(((ClientboundBundlePacket) newPacket).getPackets());
                    continue;
                }
            }
            newPackets.add(newPacket);
        }
        if (changedPackets != 0) {
            return packet.setPackets(newPackets);
        }
        return packet;
    }

    private Handler<Packet> build(Class<?> packetType) {
        List<Handler<Packet>> activatedHandlers = new ArrayList<>();
        registered.forEach((type, handlers) -> {
            if (type.isAssignableFrom(packetType)) {
                activatedHandlers.addAll(handlers);
            }
        });
        return (packetIn, player) -> {
            for (Handler<Packet> handler : activatedHandlers) {
                Packet packetOut = handler.apply(packetIn, player);
                if (packetOut != packetIn) {
                    return packetOut;
                }
            }
            return packetIn;
        };
    }

    private List<Handler<Packet>> getOrCreate(Class<?> packetType) {
        return registered.computeIfAbsent(packetType, it -> {
            return new ArrayList<>();
        });
    }

    public interface Handler<T extends Packet> extends BiFunction<T, Player, Packet> {
    }
}

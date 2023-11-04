package net.cocoonmc.core.network;

import net.cocoonmc.core.network.protocol.Packet;
import net.cocoonmc.core.world.entity.Player;

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
    }

    public <T extends Packet> void unregister(Handler<T> handler, Class<T> type) {
        getOrCreate(type).remove(handler);
    }

    public void clear() {
        registered.clear();
        applying.clear();
    }

    private Handler<Packet> build(Class<?> packetType) {
        List<List<Handler<Packet>>> activatedHandlers = new ArrayList<>();
        registered.forEach((type, handlers) -> {
            if (type.isAssignableFrom(packetType)) {
                activatedHandlers.add(handlers);
            }
        });
        return (packet, player) -> {
            for (List<Handler<Packet>> handlers : activatedHandlers) {
                for (Handler<Packet> handler : handlers) {
                    packet = handler.apply(packet, player);
                }
            }
            return packet;
        };
    }

    private List<Handler<Packet>> getOrCreate(Class<?> packetType) {
        return registered.computeIfAbsent(packetType, it -> {
            applying.clear();
            return new ArrayList<>();
        });
    }

    public interface Handler<T extends Packet> extends BiFunction<T, Player, Packet> {
    }
}

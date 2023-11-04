package net.cocoonmc.runtime.impl;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.chunk.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;

public class ChunkDataListener implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk.of(event.getChunk()).load();
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk.of(event.getChunk()).unload();
    }


//    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
//    public void onWorldLoad(WorldLoadEvent event) {
//        Level.of(event.getWorld()).load();
//    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onWorldSave(WorldSaveEvent event) {
        Level.of(event.getWorld()).save();
    }

//    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
//    public void onWorldUnload(WorldUnloadEvent event) {
//        Level.of(event.getWorld()).unload();
//    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPluginDisableEvent(PluginDisableEvent event) {
        if (event.getPlugin().equals(Cocoon.getPlugin())) {
            LevelData.close();
        }
    }
}

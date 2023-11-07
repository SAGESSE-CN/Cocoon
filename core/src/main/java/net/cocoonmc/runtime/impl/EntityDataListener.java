package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.world.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntityDataListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event) {
        EntityPlaceTask placeTask = EntityPlaceTask.last();
        if (placeTask != null && event.getEntityType().equals(placeTask.getEntityType().getDelegate().asBukkit())) {
            placeTask.apply(event.getEntity());
        }
    }
}

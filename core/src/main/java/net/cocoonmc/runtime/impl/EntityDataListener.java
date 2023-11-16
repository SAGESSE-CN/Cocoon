package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.math.Vector3d;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityDataListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event) {
        EntityPlaceTask placeTask = EntityPlaceTask.last();
        if (placeTask != null && event.getEntityType().equals(placeTask.getEntityType().getDelegate().asBukkit())) {
            placeTask.apply(event.getEntity());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = Player.of(event.getPlayer());
        Entity entity = Entity.of(event.getRightClicked());
        InteractionHand hand = InteractionHand.by(event.getHand());
        if (entity.interactAt(player, Vector3d.ZERO, hand) != InteractionResult.PASS) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityInteractAt(PlayerInteractAtEntityEvent event) {
        Player player = Player.of(event.getPlayer());
        Entity entity = Entity.of(event.getRightClicked());
        InteractionHand hand = InteractionHand.by(event.getHand());
        Vector3d position = Vector3d.of(event.getClickedPosition());
        if (entity.interactAt(player, position, hand) != InteractionResult.PASS) {
            event.setCancelled(true);
        }
    }
}

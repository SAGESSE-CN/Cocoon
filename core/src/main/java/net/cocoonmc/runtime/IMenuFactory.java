package net.cocoonmc.runtime;

import net.cocoonmc.core.inventory.Container;
import net.cocoonmc.core.inventory.Menu;
import net.cocoonmc.core.inventory.MenuImpl;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.inventory.SlotImpl;
import net.cocoonmc.core.network.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface IMenuFactory {

    MenuImpl create(Menu impl, Player player, Component title);

    SlotImpl create(Slot impl, Inventory inventory, int index, int x, int y);

    Inventory create(InventoryHolder owner, int size, String title);

    Inventory create(Container container);
}



package net.cocoonmc.runtime;

import net.cocoonmc.core.inventory.Container;
import net.cocoonmc.core.inventory.Menu;
import net.cocoonmc.core.inventory.MenuImpl;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.inventory.SlotImpl;
import net.cocoonmc.core.network.Component;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IMenuFactory {

    MenuImpl createProxy(Menu impl, Player player, Component title);

    SlotImpl createProxy(Slot impl, Inventory inventory, int index, int x, int y);

    Inventory createInventory(Container container);

    Menu getActivedMenu(net.cocoonmc.core.world.entity.Player player);
}



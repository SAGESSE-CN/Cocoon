package net.cocoonmc.core.inventory;

import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.inventory.Inventory;

public interface MenuImpl {

    void super$removed(Player player);

    void super$clearContainer(Player player, Inventory inventory);

    void super$broadcastChanges();

    boolean super$moveItemStackTo(ItemStack itemStack, int i, int j, boolean bl);

    void super$addSlot(SlotImpl slot);

    void super$openContainer();

    void super$closeContainer();

    void super$sendOpenWindowPacket(int windowId);

    void super$sendCloseWindowPacket(int windowId);
}

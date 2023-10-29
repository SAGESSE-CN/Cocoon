package net.cocoonmc.core.inventory;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.item.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public interface Container {

    boolean isEmpty();

    int getContainerSize();

    int getMaxStackSize();

    ItemStack getItem(int slot);

    ItemStack removeItem(int slot, int count);

    ItemStack removeItemNoUpdate(int slot);

    void setItem(int slot, ItemStack itemStack);

    void setChanged();

    boolean stillValid(Player player);

    List<ItemStack> getContents();

    void clearContent();

    default boolean canPlaceItem(int slot, ItemStack itemStack) {
        return true;
    }

    default boolean canTakeItem(int slot, ItemStack itemStack) {
        return true;
    }


    default Inventory asBukkit() {
        return Cocoon.API.MENU.create(this);
    }
}

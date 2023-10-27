package net.cocoonmc.core.inventory;

import net.cocoonmc.core.item.ItemStack;
import org.bukkit.entity.Player;

public interface SlotImpl {

    boolean super$hasItem();

    ItemStack super$getItem();

    void super$setItem(ItemStack itemStack);

    void super$setItemByPlayer(ItemStack itemStack);

    ItemStack super$removeItem(int i);

    boolean super$mayPlace(ItemStack itemStack);

    boolean super$mayPickup(Player player);

    void super$setChanged();

}

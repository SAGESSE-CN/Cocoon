package net.cocoonmc.core.inventory;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.item.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public class Slot {

    public int index;

    protected final Inventory inventory;
    protected final int slot;
    protected final SlotImpl impl;

    public Slot(Inventory inventory, int index, int x, int y) {
        this.inventory = inventory;
        this.slot = index;
        this.impl = Cocoon.API.MENU.create(this, inventory, index, x, y);
    }

    public boolean hasItem() {
        return impl.super$hasItem();
    }

    public ItemStack getItem() {
        return impl.super$getItem();
    }

    public void setItem(ItemStack itemStack) {
        impl.super$setItem(itemStack);
    }

    public void setItemByPlayer(ItemStack itemStack) {
        impl.super$setItemByPlayer(itemStack);
    }

    public void setItemNoUpdate(ItemStack itemStack) {
        inventory.setItem(getSlot(), itemStack.asBukkit());
    }

    public ItemStack removeItem(int size) {
        return impl.super$removeItem(size);
    }

    public boolean mayPlace(ItemStack itemStack) {
        return impl.super$mayPlace(itemStack);
    }

    public boolean mayPickup(Player player) {
        return impl.super$mayPickup(player);
    }

    public void setChanged() {
        impl.super$setChanged();
    }

    public int getSlot() {
        return slot;
    }
}

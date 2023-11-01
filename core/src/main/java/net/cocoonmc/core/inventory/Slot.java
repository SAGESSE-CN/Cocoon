package net.cocoonmc.core.inventory;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.utils.SimpleAssociatedStorage;
import net.cocoonmc.core.world.entity.Player;
import net.cocoonmc.runtime.IAssociatedContainer;
import net.cocoonmc.runtime.IAssociatedContainerProvider;
import org.bukkit.inventory.Inventory;


public class Slot implements IAssociatedContainerProvider {

    public int index;

    protected final Inventory inventory;
    protected final int slot;
    protected final SlotImpl impl;

    private final SimpleAssociatedStorage storage = new SimpleAssociatedStorage();

    public Slot(Inventory inventory, int index, int x, int y) {
        this.inventory = inventory;
        this.slot = index;
        this.impl = Cocoon.API.MENU.createProxy(this, inventory, index, x, y);
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

    @Override
    public IAssociatedContainer getAssociatedContainer() {
        return storage;
    }
}

package net.cocoonmc.core.inventory;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.network.Component;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.impl.Versions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public abstract class Menu {

    protected final Component title;
    protected final MenuType<?> menuType;
    protected final ArrayList<Slot> slots = new ArrayList<>();

    protected final Player player;
    protected final MenuImpl impl;

    public Menu(MenuType<?> menuType, Component title, Player player) {
        this.title = title;
        this.menuType = menuType;
        this.player = player;
        this.impl = Versions.MENU.create(this, player, null, title);
    }

    public void openMenu() {
        impl.super$openContainer();
    }

    public void closeMenu() {
        impl.super$closeContainer();
    }

    public void removed(Player player) {
        impl.super$removed(player);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        return slots.get(index).getItem();
    }

    public void broadcastChanges() {
        impl.super$broadcastChanges();
    }

    public void handleOpenWindowPacket(int windowId) {
        // we need send custom event.
        FriendlyByteBuf additionalData = new FriendlyByteBuf();
        serialize(additionalData);
        // create a fml open container package.
        FriendlyByteBuf buffer = new FriendlyByteBuf();
        buffer.writeByte(1);  // open container id
        buffer.writeVarInt(Integer.MAX_VALUE); // yep, a special flag by the addon mod.
        buffer.writeResourceLocation(getType().getRegistryName());
        buffer.writeVarInt(windowId);
        buffer.writeComponent(title);
        buffer.writeByteArray(additionalData.array());
        // send to player.
        player.sendPluginMessage(Cocoon.getInstance().getPlugin(), "fml:play", buffer.array());
    }

    public void handleCloseWindowPacket(int windowId) {
        impl.super$sendCloseWindowPacket(windowId);
    }


    public boolean moveItemStackTo(ItemStack itemStack, int i, int j, boolean bl) {
        return impl.super$moveItemStackTo(itemStack, i, j, bl);
    }

    public Slot addSlot(Slot slot) {
        slot.index = slots.size();
        slots.add(slot);
        impl.super$addSlot(slot.impl);
        return slot;
    }

    public MenuType<?> getType() {
        return menuType;
    }

    public Inventory getInventory() {
        return impl.super$getInventory();
    }

    public abstract void serialize(FriendlyByteBuf buffer);

    public abstract boolean stillValid(Player player);

    public abstract int getSlotSize();
}

package net.cocoonmc.runtime.v1_16_R3;

import net.cocoonmc.core.inventory.MenuImpl;
import net.cocoonmc.core.inventory.SlotImpl;
import net.cocoonmc.runtime.IMenuFactory;
import net.minecraft.server.v1_16_R3.Container;
import net.minecraft.server.v1_16_R3.Containers;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IInventory;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.PacketPlayOutCloseWindow;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_16_R3.Slot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

import java.util.function.Supplier;

public class MenuFactory extends TransformFactory implements IMenuFactory {

    @Override
    public SlotImpl create(net.cocoonmc.core.inventory.Slot impl, Inventory inventory, int index, int x, int y) {
        IInventory container = convertToVanilla(inventory);
        return new ProxySlot(impl, container, index, x, y);
    }

    @Override
    public MenuImpl create(net.cocoonmc.core.inventory.Menu impl, org.bukkit.entity.Player player, InventoryHolder holder, net.cocoonmc.core.network.Component title) {
        ProxyMenu menu = new ProxyMenu(impl, convertToVanilla(player), () -> createInventory(holder, impl.getSlotSize(), title));
        menu.setTitle(IChatBaseComponent.ChatSerializer.a(title.serialize()));
        return menu;
    }

    public static class ProxySlot extends Slot implements SlotImpl {

        private final net.cocoonmc.core.inventory.Slot impl;

        public ProxySlot(net.cocoonmc.core.inventory.Slot impl, IInventory container, int slot, int x, int y) {
            super(container, slot, x, y);
            this.impl = impl;
        }

        @Override
        public void d() {
            impl.setChanged();
        }

        @Override
        public boolean hasItem() {
            return impl.hasItem();
        }

        @Override
        public ItemStack getItem() {
            return convertToVanilla(impl.getItem());
        }

        @Override
        public void set(ItemStack itemStack) {
            impl.setItem(convertToCocoon(itemStack));
        }

        @Override
        public ItemStack a(int i) {
            return convertToVanilla(impl.removeItem(i));
        }

        @Override
        public boolean isAllowed(ItemStack itemStack) {
            return impl.mayPlace(convertToCocoon(itemStack));
        }

        @Override
        public boolean isAllowed(EntityHuman player) {
            return impl.mayPickup(convertToCocoon(player));
        }

        @Override
        public void super$setChanged() {
            super.d();
        }

        @Override
        public boolean super$hasItem() {
            return super.hasItem();
        }

        @Override
        public net.cocoonmc.core.item.ItemStack super$getItem() {
            return convertToCocoon(super.getItem());
        }

        @Override
        public void super$setItem(net.cocoonmc.core.item.ItemStack itemStack) {
            super.set(convertToVanilla(itemStack));
        }

        @Override
        public void super$setItemByPlayer(net.cocoonmc.core.item.ItemStack itemStack) {
            super$setItem(itemStack);
        }

        @Override
        public net.cocoonmc.core.item.ItemStack super$removeItem(int i) {
            return convertToCocoon(super.a(i));
        }

        @Override
        public boolean super$mayPlace(net.cocoonmc.core.item.ItemStack itemStack) {
            return super.isAllowed(convertToVanilla(itemStack));
        }

        @Override
        public boolean super$mayPickup(org.bukkit.entity.Player player) {
            return super.isAllowed(convertToVanilla(player));
        }
    }

    public static class ProxyMenu extends Container implements MenuImpl {

        private final net.cocoonmc.core.inventory.Menu impl;

        private final EntityPlayer player;
        private final Supplier<Inventory> provider;

        private Inventory inventory;
        private InventoryView inventoryView;

        protected ProxyMenu(net.cocoonmc.core.inventory.Menu impl, EntityPlayer player, Supplier<Inventory> provider) {
            super(Containers.GENERIC_9X6, player.nextContainerCounter());
            this.impl = impl;
            this.player = player;
            this.provider = provider;
        }

        @Override
        public InventoryView getBukkitView() {
            if (inventoryView == null) {
                inventoryView = createInventoryView(player.getBukkitEntity(), super$getInventory(), this);
            }
            return inventoryView;
        }

        @Override
        public boolean canUse(EntityHuman player) {
            return impl.stillValid(convertToCocoon(player));
        }

        @Override
        public void b(EntityHuman player) {
            impl.removed(convertToCocoon(player));
        }

        @Override
        public void c() {
            impl.broadcastChanges();
        }

        @Override
        public ItemStack shiftClick(EntityHuman player, int i) {
            return convertToVanilla(impl.quickMoveStack(convertToCocoon(player), i));
        }

        @Override
        public void super$removed(org.bukkit.entity.Player player) {
            super.b(convertToVanilla(player));
        }

        @Override
        public void super$broadcastChanges() {
            super.c();
        }

        @Override
        public boolean super$moveItemStackTo(net.cocoonmc.core.item.ItemStack itemStack, int i, int j, boolean bl) {
            return super.a(convertToVanilla(itemStack), i, j, bl);
        }

        @Override
        public void super$addSlot(SlotImpl slot) {
            super.a((ProxySlot) slot);
        }

        @Override
        public void super$openContainer() {
            impl.handleOpenWindowPacket(windowId);
            player.activeContainer = this;
            addSlotListener(player);
        }

        @Override
        public void super$closeContainer() {
            handleInventoryCloseEvent(player);
            player.activeContainer = player.defaultContainer;
            impl.handleCloseWindowPacket(windowId);
        }

        @Override
        public void super$sendOpenWindowPacket(int windowId) {
            player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(windowId, getType(), getTitle()));
        }

        @Override
        public void super$sendCloseWindowPacket(int windowId) {
            player.playerConnection.sendPacket(new PacketPlayOutCloseWindow(windowId));
        }

        @Override
        public Inventory super$getInventory() {
            if (inventory == null) {
                inventory = provider.get();
            }
            return inventory;
        }
    }
}

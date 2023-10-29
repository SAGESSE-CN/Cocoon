package net.cocoonmc.runtime.v1_19_R1;

import net.cocoonmc.core.inventory.MenuImpl;
import net.cocoonmc.core.inventory.SlotImpl;
import net.cocoonmc.runtime.IMenuFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_19_R1.event.CraftEventFactory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuFactory extends TransformFactory implements IMenuFactory {

    @Override
    public SlotImpl create(net.cocoonmc.core.inventory.Slot impl, Inventory inventory, int index, int x, int y) {
        Container container = convertToVanilla(inventory);
        return new ProxySlot(impl, container, index, x, y);
    }

    @Override
    public MenuImpl create(net.cocoonmc.core.inventory.Menu impl, org.bukkit.entity.Player player, net.cocoonmc.core.network.Component title) {
        ProxyMenu menu = new ProxyMenu(impl, convertToVanilla(player));
        menu.setTitle(Component.Serializer.fromJson(title.serialize()));
        return menu;
    }

    @Override
    public Inventory create(InventoryHolder owner, int size, String title) {
        return createInventory(owner, size, title);
    }

    @Override
    public Inventory create(net.cocoonmc.core.inventory.Container container) {
        return createInventory(new ProxyContainer(container));
    }


    public static class ProxySlot extends Slot implements SlotImpl {

        private final net.cocoonmc.core.inventory.Slot impl;

        public ProxySlot(net.cocoonmc.core.inventory.Slot impl, Container container, int slot, int x, int y) {
            super(container, slot, x, y);
            this.impl = impl;
        }

        @Override
        public void setChanged() {
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
        public ItemStack remove(int i) {
            return convertToVanilla(impl.removeItem(i));
        }

        @Override
        public boolean mayPlace(ItemStack itemStack) {
            return impl.mayPlace(convertToCocoon(itemStack));
        }

        @Override
        public boolean mayPickup(Player player) {
            return impl.mayPickup(convertToCocoon(player));
        }

        @Override
        public void super$setChanged() {
            super.setChanged();
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
            return convertToCocoon(super.remove(i));
        }

        @Override
        public boolean super$mayPlace(net.cocoonmc.core.item.ItemStack itemStack) {
            return super.mayPlace(convertToVanilla(itemStack));
        }

        @Override
        public boolean super$mayPickup(org.bukkit.entity.Player player) {
            return super.mayPickup(convertToVanilla(player));
        }
    }

    public static class ProxyMenu extends AbstractContainerMenu implements MenuImpl {

        private final net.cocoonmc.core.inventory.Menu impl;

        private final ServerPlayer player;

        private Inventory inventory;
        private InventoryView inventoryView;

        protected ProxyMenu(net.cocoonmc.core.inventory.Menu impl, ServerPlayer player) {
            super(MenuType.GENERIC_9x6, player.nextContainerCounter());
            this.impl = impl;
            this.player = player;
        }

        @Override
        public InventoryView getBukkitView() {
            if (inventoryView == null) {
                inventoryView = createInventoryView(player.getBukkitEntity(), impl.getInventory(), this);
            }
            return inventoryView;
        }

        @Override
        public boolean stillValid(Player player) {
            return impl.stillValid(convertToCocoon(player));
        }

        @Override
        public void removed(Player player) {
            impl.removed(convertToCocoon(player));
        }

        @Override
        public void broadcastChanges() {
            impl.broadcastChanges();
        }

        @Override
        public ItemStack quickMoveStack(Player player, int i) {
            return convertToVanilla(impl.quickMoveStack(convertToCocoon(player), i));
        }

        @Override
        public void super$removed(org.bukkit.entity.Player player) {
            super.removed(convertToVanilla(player));
        }

        @Override
        public void super$broadcastChanges() {
            super.broadcastChanges();
        }

        @Override
        public boolean super$moveItemStackTo(net.cocoonmc.core.item.ItemStack itemStack, int i, int j, boolean bl) {
            return super.moveItemStackTo(convertToVanilla(itemStack), i, j, bl);
        }

        @Override
        public void super$addSlot(SlotImpl slot) {
            super.addSlot((ProxySlot) slot);
        }

        @Override
        public void super$openContainer() {
            impl.handleOpenWindowPacket(containerId);
            player.containerMenu = this;
            player.initMenu(this);
        }

        @Override
        public void super$closeContainer() {
            CraftEventFactory.handleInventoryCloseEvent(player);
            player.containerMenu = player.inventoryMenu;
            impl.handleCloseWindowPacket(containerId);
        }

        @Override
        public void super$sendOpenWindowPacket(int windowId) {
            player.connection.send(new ClientboundOpenScreenPacket(windowId, getType(), getTitle()));
        }

        @Override
        public void super$sendCloseWindowPacket(int windowId) {
            player.connection.send(new ClientboundContainerClosePacket(windowId));
        }
    }

    public static class ProxyContainer implements Container {

        private final ArrayList<HumanEntity> viewers = new ArrayList<>();
        private final net.cocoonmc.core.inventory.Container impl;

        public ProxyContainer(net.cocoonmc.core.inventory.Container impl) {
            this.impl = impl;
        }

        @Override
        public int getContainerSize() {
            return impl.getContainerSize();
        }

        @Override
        public boolean isEmpty() {
            return impl.isEmpty();
        }

        @Override
        public ItemStack getItem(int i) {
            return convertToVanilla(impl.getItem(i));
        }

        @Override
        public ItemStack removeItem(int i, int i1) {
            return convertToVanilla(impl.removeItem(i, i1));
        }

        @Override
        public ItemStack removeItemNoUpdate(int i) {
            return convertToVanilla(impl.removeItemNoUpdate(i));
        }

        @Override
        public void setItem(int i, ItemStack itemStack) {
            impl.setItem(i, convertToCocoon(itemStack));
        }

        @Override
        public int getMaxStackSize() {
            return impl.getMaxStackSize();
        }

        @Override
        public void setMaxStackSize(int size) {
        }

        @Override
        public void setChanged() {
            impl.setChanged();
        }

        @Override
        public boolean stillValid(Player player) {
            return impl.stillValid(convertToCocoon(player));
        }

        @Override
        public List<ItemStack> getContents() {
            return impl.getContents().stream().map(TransformFactory::convertToVanilla).collect(Collectors.toList());
        }

        @Override
        public void onOpen(CraftHumanEntity player) {
            viewers.add(player);
        }

        @Override
        public void onClose(CraftHumanEntity player) {
            viewers.remove(player);
        }

        @Override
        public boolean canPlaceItem(int i, ItemStack itemstack) {
            return impl.canPlaceItem(i, convertToCocoon(itemstack));
        }

        @Override
        public List<HumanEntity> getViewers() {
            return viewers;
        }

        @Override
        public InventoryHolder getOwner() {
            return null;
        }

        @Override
        public Location getLocation() {
            return null;
        }

        @Override
        public void clearContent() {
            impl.clearContent();
        }
    }
}

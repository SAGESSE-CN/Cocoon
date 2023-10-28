package net.cocoonmc.runtime.v1_20_R1;

import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.network.Component;
import net.cocoonmc.runtime.impl.ItemStackWrapper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

public class TransformFactory {

    private static final org.bukkit.inventory.ItemStack EMPTY = new org.bukkit.inventory.ItemStack(Material.AIR, 0);

    public static Inventory createInventory(InventoryHolder owner, int size, Component title) {
        return new CraftInventoryCustom(owner, size, "");
    }

    public static InventoryView createInventoryView(HumanEntity player, Inventory viewing, AbstractContainerMenu container) {
        return new CraftInventoryView(player, viewing, container);
    }


    public static org.bukkit.inventory.ItemStack convertToBukkit(net.cocoonmc.core.item.ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return EMPTY;
        }
        if (itemStack instanceof ItemStackWrapper.Entry<?, ?>) {
            return itemStack.asBukkit();
        }
        return convertToBukkit(copyToVanilla(itemStack));
    }

    public static org.bukkit.inventory.ItemStack convertToBukkit(net.minecraft.world.item.ItemStack itemStack) {
        return CraftItemStack.asCraftMirror(itemStack);
    }


    public static net.cocoonmc.core.item.ItemStack convertToCocoon(org.bukkit.inventory.ItemStack itemStack) {
        if (itemStack == null || itemStack == EMPTY || itemStack.getType() == Material.AIR || itemStack.getAmount() <= 0) {
            return net.cocoonmc.core.item.ItemStack.EMPTY;
        }
        return ItemFactory.createItemWrapper(itemStack, null).newCocoonStack();
    }

    public static net.cocoonmc.core.item.ItemStack convertToCocoon(net.minecraft.world.item.ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return net.cocoonmc.core.item.ItemStack.EMPTY;
        }
        return ItemFactory.createItemWrapper(convertToBukkit(itemStack), itemStack).newCocoonStack();
    }

    public static org.bukkit.entity.Player convertToCocoon(net.minecraft.world.entity.player.Player player) {
        return (org.bukkit.entity.Player) player.getBukkitEntity();
    }

    @SuppressWarnings("unchecked")
    public static net.minecraft.world.item.ItemStack convertToVanilla(net.cocoonmc.core.item.ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }
        if (itemStack instanceof ItemStackWrapper.Entry<?, ?>) {
            return ((ItemStackWrapper.Entry<?, net.minecraft.world.item.ItemStack>) itemStack).asVanilla();
        }
        return copyToVanilla(itemStack);
    }

    public static net.minecraft.server.level.ServerPlayer convertToVanilla(org.bukkit.entity.Player player) {
        if (player != null) {
            return ((CraftPlayer) player).getHandle();
        }
        return null;
    }


    public static net.minecraft.server.level.ServerLevel convertToVanilla(org.bukkit.World world) {
        if (world instanceof CraftWorld) {
            return ((CraftWorld) world).getHandle();
        }
        return null;
    }

    public static net.minecraft.world.Container convertToVanilla(org.bukkit.inventory.Inventory inventory) {
        return ((CraftInventory) inventory).getInventory();
    }

    public static net.minecraft.world.item.ItemStack copyToVanilla(net.cocoonmc.core.item.ItemStack itemStack) {
        CompoundTag tag = CompoundTag.newInstance();
        itemStack.save(tag);
        return net.minecraft.world.item.ItemStack.of(TagFactory.unwrap(tag));
    }

    public static net.minecraft.world.item.ItemStack copyToVanilla(org.bukkit.inventory.ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    //

    public static void handleInventoryCloseEvent(net.minecraft.world.entity.player.Player player) {
        CraftEventFactory.handleInventoryCloseEvent(player);
    }
}

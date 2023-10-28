package net.cocoonmc.runtime.v1_16_R3;

import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.network.Component;
import net.cocoonmc.runtime.impl.ItemStackWrapper;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

public class TransformFactory {

    private static final org.bukkit.inventory.ItemStack EMPTY = new org.bukkit.inventory.ItemStack(Material.AIR, 0);

    public static Inventory createInventory(InventoryHolder owner, int size, Component title) {
        return new CraftInventoryCustom(owner, size, "");
    }

    public static InventoryView createInventoryView(HumanEntity player, Inventory viewing, net.minecraft.server.v1_16_R3.Container container) {
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

    public static org.bukkit.inventory.ItemStack convertToBukkit(net.minecraft.server.v1_16_R3.ItemStack itemStack) {
        return CraftItemStack.asCraftMirror(itemStack);
    }


    public static net.cocoonmc.core.item.ItemStack convertToCocoon(org.bukkit.inventory.ItemStack itemStack) {
        if (itemStack == null || itemStack == EMPTY || itemStack.getType() == Material.AIR || itemStack.getAmount() <= 0) {
            return net.cocoonmc.core.item.ItemStack.EMPTY;
        }
        return ItemFactory.createItemWrapper(itemStack, null).newCocoonStack();
    }

    public static net.cocoonmc.core.item.ItemStack convertToCocoon(net.minecraft.server.v1_16_R3.ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return net.cocoonmc.core.item.ItemStack.EMPTY;
        }
        return ItemFactory.createItemWrapper(convertToBukkit(itemStack), itemStack).newCocoonStack();
    }

    public static org.bukkit.entity.Player convertToCocoon(net.minecraft.server.v1_16_R3.EntityHuman player) {
        return (org.bukkit.entity.Player) player.getBukkitEntity();
    }

    @SuppressWarnings("unchecked")
    public static net.minecraft.server.v1_16_R3.ItemStack convertToVanilla(net.cocoonmc.core.item.ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return net.minecraft.server.v1_16_R3.ItemStack.b;
        }
        if (itemStack instanceof ItemStackWrapper.Entry<?, ?>) {
            return ((ItemStackWrapper.Entry<?, net.minecraft.server.v1_16_R3.ItemStack>) itemStack).asVanilla();
        }
        return copyToVanilla(itemStack);
    }

    public static net.minecraft.server.v1_16_R3.EntityPlayer convertToVanilla(org.bukkit.entity.Player player) {
        if (player != null) {
            return ((CraftPlayer) player).getHandle();
        }
        return null;
    }


    public static net.minecraft.server.v1_16_R3.WorldServer convertToVanilla(org.bukkit.World world) {
        if (world instanceof CraftWorld) {
            return ((CraftWorld) world).getHandle();
        }
        return null;
    }

    public static net.minecraft.server.v1_16_R3.IInventory convertToVanilla(org.bukkit.inventory.Inventory inventory) {
        return ((CraftInventory) inventory).getInventory();
    }

    public static net.minecraft.server.v1_16_R3.ItemStack copyToVanilla(net.cocoonmc.core.item.ItemStack itemStack) {
        CompoundTag tag = CompoundTag.newInstance();
        itemStack.save(tag);
        return net.minecraft.server.v1_16_R3.ItemStack.a((NBTTagCompound)TagFactory.unwrap(tag));
    }

    public static net.minecraft.server.v1_16_R3.ItemStack copyToVanilla(org.bukkit.inventory.ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    //

    public static void handleInventoryCloseEvent(net.minecraft.server.v1_16_R3.EntityPlayer player) {
        CraftEventFactory.handleInventoryCloseEvent(player);
    }
}

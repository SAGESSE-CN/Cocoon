package net.cocoonmc.runtime.v1_16_R3;

import net.minecraft.server.v1_16_R3.Container;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IInventory;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

public class TransformFactory {

    public static Inventory createInventory(InventoryHolder owner, int size, String title) {
        return new CraftInventoryCustom(owner, size, title);
    }

    public static Inventory createInventory(IInventory container) {
        return new CraftInventory(container);
    }

    public static InventoryView createInventoryView(HumanEntity player, Inventory viewing, Container container) {
        return new CraftInventoryView(player, viewing, container);
    }


    public static org.bukkit.inventory.ItemStack convertToBukkit(net.cocoonmc.core.item.ItemStack itemStack) {
        return ItemFactory.ITEM_TRANSFORMER.convertToBukkit(itemStack);
    }

    public static org.bukkit.inventory.ItemStack convertToBukkit(ItemStack itemStack) {
        return ItemFactory.ITEM_TRANSFORMER.convertToBukkit(itemStack);
    }

    public static net.cocoonmc.core.item.ItemStack convertToCocoon(org.bukkit.inventory.ItemStack itemStack) {
        return ItemFactory.ITEM_TRANSFORMER.convertToCocoon(itemStack);
    }

    public static net.cocoonmc.core.item.ItemStack convertToCocoon(ItemStack itemStack) {
        return ItemFactory.ITEM_TRANSFORMER.convertToCocoon(itemStack);
    }


    public static ItemStack convertToVanilla(net.cocoonmc.core.item.ItemStack itemStack) {
        return ItemFactory.ITEM_TRANSFORMER.convertToVanilla(itemStack);
    }


    public static org.bukkit.entity.Player convertToCocoon(EntityHuman player) {
        return (org.bukkit.entity.Player) player.getBukkitEntity();
    }

    public static EntityPlayer convertToVanilla(org.bukkit.entity.Player player) {
        if (player != null) {
            return ((CraftPlayer) player).getHandle();
        }
        return null;
    }


    public static WorldServer convertToVanilla(org.bukkit.World world) {
        if (world instanceof CraftWorld) {
            return ((CraftWorld) world).getHandle();
        }
        return null;
    }

    public static IInventory convertToVanilla(org.bukkit.inventory.Inventory inventory) {
        return ((CraftInventory) inventory).getInventory();
    }
}

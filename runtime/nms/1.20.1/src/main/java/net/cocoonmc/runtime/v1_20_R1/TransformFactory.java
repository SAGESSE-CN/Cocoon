package net.cocoonmc.runtime.v1_20_R1;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

public class TransformFactory {

    public static Inventory createInventory(InventoryHolder owner, int size, String title) {
        return new CraftInventoryCustom(owner, size, title);
    }

    public static Inventory createInventory(Container container) {
        return new CraftInventory(container);
    }

    public static InventoryView createInventoryView(HumanEntity player, Inventory viewing, AbstractContainerMenu container) {
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


    public static org.bukkit.entity.Player convertToCocoon(net.minecraft.world.entity.player.Player player) {
        return (org.bukkit.entity.Player) player.getBukkitEntity();
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
}

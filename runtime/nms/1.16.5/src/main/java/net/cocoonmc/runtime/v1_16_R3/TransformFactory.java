package net.cocoonmc.runtime.v1_16_R3;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IInventory;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;

public class TransformFactory {

    public static net.cocoonmc.core.item.ItemStack convertToCocoon(ItemStack itemStack) {
        return ItemFactory.ITEM_TRANSFORMER.convertToCocoon(itemStack);
    }

    public static ItemStack convertToVanilla(net.cocoonmc.core.item.ItemStack itemStack) {
        return ItemFactory.ITEM_TRANSFORMER.convertToVanilla(itemStack);
    }


    public static net.cocoonmc.core.world.entity.Player convertToCocoon(EntityHuman player) {
        return net.cocoonmc.core.world.entity.Player.of((org.bukkit.entity.Player) player.getBukkitEntity());
    }

    public static EntityPlayer convertToVanilla(net.cocoonmc.core.world.entity.Player player) {
        if (player != null) {
            return ((CraftPlayer) player.asBukkit()).getHandle();
        }
        return null;
    }


    public static WorldServer convertToVanilla(net.cocoonmc.core.world.Level level) {
        if (level != null) {
            return ((CraftWorld) level.asBukkit()).getHandle();
        }
        return null;
    }

    public static IInventory convertToVanilla(org.bukkit.inventory.Inventory inventory) {
        return ((CraftInventory) inventory).getInventory();
    }

    public static BlockPosition convertToVanilla(net.cocoonmc.core.BlockPos pos) {
        return new BlockPosition(pos.getX(), pos.getY(), pos.getZ());
    }
}

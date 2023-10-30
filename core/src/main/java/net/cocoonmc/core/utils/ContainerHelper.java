package net.cocoonmc.core.utils;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.ListTag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class ContainerHelper {

    public static ItemStack removeItem(List<ItemStack> list, int i, int j) {
        if (i < 0 || i >= list.size() || list.get(i).isEmpty() || j <= 0) {
            return ItemStack.EMPTY;
        }
        return list.get(i).split(j);
    }

    public static ItemStack takeItem(List<ItemStack> list, int i) {
        if (i < 0 || i >= list.size()) {
            return ItemStack.EMPTY;
        }
        return list.set(i, ItemStack.EMPTY);
    }

    public static CompoundTag saveAllItems(CompoundTag tag, NonNullList<ItemStack> itemStacks) {
        return saveAllItems(tag, itemStacks, true);
    }

    public static CompoundTag saveAllItems(CompoundTag tag, NonNullList<ItemStack> itemStacks, boolean var2) {
        ListTag var3 = ListTag.newInstance();

        for (int i = 0; i < itemStacks.size(); ++i) {
            ItemStack itemStack = itemStacks.get(i);
            if (!itemStack.isEmpty()) {
                CompoundTag itemTag = CompoundTag.newInstance();
                itemTag.putByte("Slot", (byte) i);
                itemStack.save(itemTag);
                var3.add(itemTag);
            }
        }

        if (!var3.isEmpty() || var2) {
            tag.put("Items", var3);
        }

        return tag;
    }

    public static void loadAllItems(CompoundTag tag, NonNullList<ItemStack> itemStacks) {
        ListTag var2 = tag.getList("Items", 10);
        for (int i = 0; i < var2.size(); ++i) {
            CompoundTag itemTag = var2.getCompound(i);
            int slot = itemTag.getByte("Slot") & 0xFF;
            if (slot < itemStacks.size()) {
                itemStacks.set(slot, ItemStack.of(itemTag));
            }
        }
    }


    public static void drop(ItemStack itemStack, Player player, boolean bl2) {
        drop(itemStack.asBukkit(), player, bl2);
    }

    public static void drop(org.bukkit.inventory.ItemStack itemStack, Player player, boolean bl2) {
        Bukkit.getScheduler().runTask(Cocoon.getInstance().getPlugin(), () -> {
            player.getWorld().dropItem(player.getLocation(), itemStack);
//            if (bl2) {
//                itemEntity.setThrower(player.getUUID());
//            }
        });
    }
}

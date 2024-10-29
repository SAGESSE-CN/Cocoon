package net.cocoonmc.runtime;

import net.cocoonmc.core.component.DataComponentMap;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.context.UseOnContext;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.entity.Player;
import net.cocoonmc.runtime.impl.DataComponentMapImpl;

public interface IItemFactory {

    ItemStack convertTo(org.bukkit.inventory.ItemStack itemStack);

    org.bukkit.inventory.ItemStack convertTo(ItemStack itemStack);

    InteractionResult useOn(Player player, ItemStack itemStack, UseOnContext context);

    default DataComponentMap createComponents() {
        return new DataComponentMapImpl(null);
    }

    default DataComponentMap createComponents(CompoundTag tag) {
        return new DataComponentMapImpl(tag);
    }
}

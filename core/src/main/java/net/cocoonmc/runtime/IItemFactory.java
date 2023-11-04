package net.cocoonmc.runtime;

import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.context.UseOnContext;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.entity.Player;

public interface IItemFactory {

    ItemStack convertTo(org.bukkit.inventory.ItemStack itemStack);

    org.bukkit.inventory.ItemStack convertTo(ItemStack itemStack);

    InteractionResult useOn(Player player, ItemStack itemStack, UseOnContext context);
}

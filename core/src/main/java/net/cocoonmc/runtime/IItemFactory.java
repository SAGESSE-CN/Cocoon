package net.cocoonmc.runtime;

import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.context.UseOnContext;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.runtime.impl.Constants;

public interface IItemFactory {

    ItemStack convertTo(org.bukkit.inventory.ItemStack itemStack);

    org.bukkit.inventory.ItemStack convertTo(ItemStack itemStack);

    InteractionResult useOn(ItemStack itemStack, UseOnContext context);
}

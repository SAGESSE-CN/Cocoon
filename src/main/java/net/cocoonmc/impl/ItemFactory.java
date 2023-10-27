package net.cocoonmc.impl;

import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.context.UseOnContext;
import net.cocoonmc.core.world.InteractionResult;

public interface ItemFactory {

    ItemStack wrap(org.bukkit.inventory.ItemStack itemStack);

    org.bukkit.inventory.ItemStack unwrap(ItemStack itemStack);

    InteractionResult useOn(ItemStack itemStack, UseOnContext context);
}

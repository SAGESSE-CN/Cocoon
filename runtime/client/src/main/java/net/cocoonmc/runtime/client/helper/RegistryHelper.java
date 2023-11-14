package net.cocoonmc.runtime.client.helper;

import net.cocoonmc.runtime.client.api.Available;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

@Available("[1.20, )")
public class RegistryHelper {

    public static final Registry<Item> ITEMS = BuiltInRegistries.ITEM;
    public static final Registry<Block> BLOCKS = BuiltInRegistries.BLOCK;

    public static final Registry<EntityType<?>> ENTITY_TYPES = BuiltInRegistries.ENTITY_TYPE;
    public static final Registry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = BuiltInRegistries.BLOCK_ENTITY_TYPE;

    public static final Registry<MenuType<?>> MENU_TYPES = BuiltInRegistries.MENU;
}

package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.core.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public class ItemStackWrapper<BukkitItemStack extends org.bukkit.inventory.ItemStack, VanillaItemStack extends ItemStackAccessor> extends ItemStack {

    protected BukkitItemStack bukkitStack;
    protected VanillaItemStack vanillaStack;

    public ItemStackWrapper(BukkitItemStack bukkitStack, VanillaItemStack vanillaStack) {
        this(bukkitStack, vanillaStack, vanillaStack.getTag());
    }

    protected ItemStackWrapper(BukkitItemStack bukkitStack, VanillaItemStack vanillaStack, CompoundTag tag) {
        super(getRealItem(bukkitStack.getType().getKey().toString(), tag), bukkitStack.getAmount(), tag);
        this.bukkitStack = bukkitStack;
        this.vanillaStack = vanillaStack;
    }

    @Override
    public void setCount(int count) {
        super.setCount(count);
        bukkitStack.setAmount(count);
        vanillaStack.setCount(count);
    }

    @Override
    public void setTag(CompoundTag tag) {
        super.setTag(tag);
        vanillaStack.setTag(tag);
    }

    @Override
    public int getMaxStackSize() {
        return bukkitStack.getMaxStackSize();
    }

    @Override
    public BukkitItemStack asBukkit() {
        return bukkitStack;
    }


    public static Item getRealItem(String id, CompoundTag tag) {
        Item item = Items.byId(getReadId(id, tag));
        if (item != null) {
            return item;
        }
        return Items.AIR;
    }

    @Nullable
    public static String getReadId(String wrapperId, @Nullable CompoundTag tag) {
        if (tag != null && tag.contains(Constants.ITEM_REDIRECTED_KEY, 8)) {
            String sourceId = _splitId(tag.getString(Constants.ITEM_REDIRECTED_KEY), 0);
            if (sourceId != null && !sourceId.isEmpty()) {
                return sourceId;
            }
        }
        return wrapperId;
    }

    public static String getWrapperId(String sourceId, @Nullable CompoundTag tag, int maxStackSize) {
        if (sourceId.startsWith("minecraft:")) {
            return sourceId;
        }
        if (tag != null && tag.contains(Constants.ITEM_REDIRECTED_KEY, 8)) {
            String wrapperId = _splitId(tag.getString(Constants.ITEM_REDIRECTED_KEY), 1);
            if (wrapperId != null && !wrapperId.isEmpty()) {
                return wrapperId;
            }
        }
        return getWrapperIdBySize(maxStackSize);
    }

    public static String getWrapperIdBySize(int maxStackSize) {
        switch (maxStackSize) {
            case 1: {
                return Items.FLOWER_BANNER_PATTERN.getRegistryName().toString();
            }
            case 16: {
                return Items.WHITE_BANNER.getRegistryName().toString();
            }
            default: {
                return Items.PAPER.getRegistryName().toString();
            }
        }
    }

    private static String _splitId(String id, int index) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        String[] ids = id.split("/");
        if (index < ids.length) {
            return ids[index];
        }
        return null;
    }
}

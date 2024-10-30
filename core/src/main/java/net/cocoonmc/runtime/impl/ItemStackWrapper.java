package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.component.DataComponentMap;
import net.cocoonmc.core.component.DataComponents;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.core.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public class ItemStackWrapper<BukkitItemStack extends org.bukkit.inventory.ItemStack, VanillaItemStack extends ItemStackAccessor> extends ItemStack {

    protected BukkitItemStack bukkitStack;
    protected VanillaItemStack vanillaStack;

    public ItemStackWrapper(BukkitItemStack bukkitStack, VanillaItemStack vanillaStack) {
        this(bukkitStack, vanillaStack, vanillaStack.getComponents());
    }

    protected ItemStackWrapper(BukkitItemStack bukkitStack, VanillaItemStack vanillaStack, DataComponentMap components) {
        super(getRealItem(bukkitStack.getType().getKey().toString(), components), bukkitStack.getAmount(), getProxyComponents(components, vanillaStack.getComponents()));
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
    public int getMaxStackSize() {
        return bukkitStack.getMaxStackSize();
    }

    @Override
    public BukkitItemStack asBukkit() {
        return bukkitStack;
    }


    public static CompoundTag unsafeSerialize(ItemStack itemStack, CompoundTag outputTag) {
        DataComponentMap components = itemStack.getComponents();
        String sourceId = itemStack.getItem().getRegistryName().toString();
        String wrapperId = ItemStackWrapper.getWrapperId(sourceId, components, itemStack.getMaxStackSize());
        outputTag.putString("id", wrapperId);
        outputTag.putByte("Count", (byte) itemStack.getCount());
        CompoundTag itemTag = ((TagComponentMap) components).getTag();
        if (itemTag != null) {
            itemTag = itemTag.copy();
        }
        if (!wrapperId.equals(sourceId)) {
            if (itemTag == null) {
                itemTag = CompoundTag.newInstance();
            }
            itemTag.putString(Constants.ITEM_REDIRECTED_KEY, sourceId + "/" + wrapperId);
        }
        if (itemTag != null) {
            outputTag.put("tag", itemTag);
        }
        return outputTag;
    }

    public static ItemStack unsafeDeserialize(CompoundTag tag) {
        CompoundTag itemTag = null;
        if (tag.contains("tag", 10)) {
            itemTag = tag.getCompound("tag");
        }
        DataComponentMap components = new TagComponentMap(itemTag);
        String wrapperId = tag.getString("id");
        String sourceId = ItemStackWrapper.getReadId(wrapperId, components);
        Item item = Items.byId(sourceId);
        int count = tag.getByte("Count");
        return new ItemStack(item, count, components);
    }

    public static Item getRealItem(String id, DataComponentMap components) {
        Item item = Items.byId(getReadId(id, components));
        if (item != null) {
            return item;
        }
        return Items.AIR;
    }

    @Nullable
    public static String getReadId(String wrapperId, DataComponentMap components) {
        String sourceId = _splitId(components.get(DataComponents.REDIRECTED_ITEM_ID), 0);
        if (sourceId != null && !sourceId.isEmpty()) {
            return sourceId;
        }
        return wrapperId;
    }

    public static String getWrapperId(String sourceId, DataComponentMap components, int maxStackSize) {
        if (sourceId.startsWith("minecraft:")) {
            return sourceId;
        }
        String wrapperId = _splitId(components.get(DataComponents.REDIRECTED_ITEM_ID), 1);
        if (wrapperId != null && !wrapperId.isEmpty()) {
            return wrapperId;
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

    private static DataComponentMap getProxyComponents(DataComponentMap first, DataComponentMap second) {
        if (first != second) {
            return new ProxyComponentMap(first, second);
        }
        return first;
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

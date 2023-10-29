package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.BukkitUtils;
import org.bukkit.Material;

public class ItemStackWrapper<T1 extends org.bukkit.inventory.ItemStack, T2 extends VanillaStackAccessor> extends ItemStack {

    protected T1 bukkitStack;
    protected T2 vanillaStack;

    public ItemStackWrapper(T1 bukkitStack, T2 vanillaStack) {
        this(bukkitStack, vanillaStack, vanillaStack.getTag());
    }

    protected ItemStackWrapper(T1 bukkitStack, T2 vanillaStack, CompoundTag tag) {
        super(loadItem(bukkitStack.getType(), tag), bukkitStack.getAmount(), tag);
        this.bukkitStack = bukkitStack;
        this.vanillaStack = vanillaStack;
    }

    protected static Item loadItem(Material type, CompoundTag tag) {
        String id = type.getKey().toString();
        Item item = Items.byId(BukkitUtils.getReadId(id, tag));
        if (item != null) {
            return item;
        }
        return Items.AIR;
    }

    @Override
    public void setCount(int count) {
        super.setCount(count);
        bukkitStack.setAmount(count);
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
    public T1 asBukkit() {
        return bukkitStack;
    }
}

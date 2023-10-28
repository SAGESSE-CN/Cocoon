package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.BukkitUtils;
import org.jetbrains.annotations.Nullable;


//public static class ItemStackContext {
//}
//

public abstract class ItemStackWrapper<T1 extends org.bukkit.inventory.ItemStack, T2> {

    // 0x01 load tag, 0x02 load vanilla stack, 0x08 load item
    protected int flags = 0;

    protected Item item;
    protected CompoundTag tag;

    protected T1 bukkitStack;
    protected T2 vanillaStack;


    public ItemStackWrapper(T1 bukkitStack, T2 vanillaStack) {
        this.bukkitStack = bukkitStack;
        if (vanillaStack != null) {
            this.setVanillaStack(vanillaStack);
        }
    }

    public void setItem(Item item) {
        this.flags |= 0x08;
        this.item = item;
    }

    public Item getItem() {
        if ((flags & 0x08) == 0) {
            flags |= 0x08;
            String id = bukkitStack.getType().getKey().toString();
            item = Items.byId(BukkitUtils.getReadId(id, getTag()));
        }
        if (item != null) {
            return item;
        }
        return Items.AIR;
    }

    public void setTag(CompoundTag tag) {
        this.flags |= 0x01;
        this.tag = tag;
        T2 itemStack = getVanillaStack();
        if (itemStack != null) {
            saveVanillaTag(itemStack, tag);
        }
    }

    public CompoundTag getTag() {
        if ((flags & 0x01) == 0) {
            flags |= 0x01;
            T2 itemStack = getVanillaStack();
            if (itemStack != null) {
                tag = loadVanillaTag(itemStack);
            }
        }
        return tag;
    }

    public void setVanillaStack(T2 itemStack) {
        flags |= 0x02;
        vanillaStack = itemStack;
    }

    public T2 getVanillaStack() {
        if ((flags & 0x02) == 0) {
            flags |= 0x02;
            vanillaStack = loadVanillaStack(bukkitStack);
        }
        return vanillaStack;
    }

    public boolean isBreakChanges() {
        // we changed the tag, but we can't read the vanilla stack.
        return tag != null && vanillaStack == null;
    }

    public T1 newBukkitStack() {
        T2 itemStack = copyTo(bukkitStack);
        if (tag != null) {
            saveVanillaTag(itemStack, tag.copy());
        }
        return convertTo(itemStack);
    }

    public ItemStack newCocoonStack() {
        return new Entry<>(this);
    }

    protected abstract T2 copyTo(T1 itemStack);

    protected abstract T1 convertTo(T2 itemStack);

    @Nullable
    protected abstract T2 loadVanillaStack(T1 bukkitStack);

    @Nullable
    protected abstract CompoundTag loadVanillaTag(T2 itemStack);

    protected abstract void saveVanillaTag(T2 itemStack, CompoundTag tag);
//            if (tag != null) {
//                stack.setTag(TagFactory.unwrap(newValue));
//            } else {
//                stack.setTag(null);
//            }


//
//        private CompoundTag loadVanillaTag() {
//            if (!bukkitStack.hasItemMeta()) {
//                return null;
//            }
//            ItemStack stack = getVanillaItem();
//            if (stack != null && stack.getTag() != null) {
//                return TagFactory.wrap(stack.getTag());
//            }
//            return null;
//        }
//
//        private ItemStack loadVanillaItem() {
//            return ReflectUtils.getMemberField(bukkitStack.getClass(), "handle").get(bukkitStack);
//        }

    public static class Entry<T1 extends org.bukkit.inventory.ItemStack, T2> extends ItemStack {

        private final ItemStackWrapper<T1, T2> context;

        public Entry(ItemStackWrapper<T1, T2> context) {
            super(context.getItem(), context.bukkitStack.getAmount());
            this.tag = context.getTag();
            this.context = context;
        }

        @Override
        public void setCount(int count) {
            super.setCount(count);
            context.bukkitStack.setAmount(count);
        }

        @Override
        public void setTag(CompoundTag tag) {
            super.setTag(tag);
            context.setTag(tag);
        }

        @Override
        public int getMaxStackSize() {
            return context.bukkitStack.getMaxStackSize();
        }

        public T2 asVanilla() {
            T2 itemStack = context.getVanillaStack();
            if (itemStack != null) {
                return itemStack;
            }
            return context.copyTo(asBukkit());
        }

        @Override
        public T1 asBukkit() {
            if (context.isBreakChanges()) {
                return context.newBukkitStack();
            }
            return context.bukkitStack;
        }

        @Override
        public String toString() {
            return context.bukkitStack.toString();
        }
    }
}

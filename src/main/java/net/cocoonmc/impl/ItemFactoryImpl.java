package net.cocoonmc.impl;

import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.utils.BukkitUtils;
import net.cocoonmc.utils.ReflectUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ItemFactoryImpl extends NMSUtils implements ItemFactory {

    @Override
    public net.cocoonmc.core.item.ItemStack wrap(org.bukkit.inventory.ItemStack itemStack) {
        return convertToCocoon(itemStack);
    }

    @Override
    public org.bukkit.inventory.ItemStack unwrap(net.cocoonmc.core.item.ItemStack itemStack) {
        return convertToBukkit(itemStack);
    }

    @Override
    public net.cocoonmc.core.world.InteractionResult useOn(net.cocoonmc.core.item.ItemStack itemStackIn, net.cocoonmc.core.item.context.UseOnContext context) {
        InteractionHand useItemHand = _unwrap(context.getHand());
        ItemStack itemStack = copyToVanilla(unwrap(itemStackIn));
        return _wrap(itemStack.useOn(_unwrap(context, itemStack, useItemHand), useItemHand));
    }

    private static net.cocoonmc.core.world.InteractionResult _wrap(InteractionResult result) {
        return net.cocoonmc.core.world.InteractionResult.values()[result.ordinal()];
    }

    private static Vec3 _unwrap(net.cocoonmc.core.math.Vector3f vector) {
        return new Vec3(vector.getX(), vector.getY(), vector.getZ());
    }

    private static Direction _unwrap(net.cocoonmc.core.Direction dir) {
        return Direction.values()[dir.ordinal()];
    }

    private static InteractionHand _unwrap(net.cocoonmc.core.world.InteractionHand hand) {
        return InteractionHand.values()[hand.ordinal()];
    }

    private static BlockPos _unwrap(net.cocoonmc.core.BlockPos pos) {
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }


    private static UseOnContext _unwrap(net.cocoonmc.core.item.context.UseOnContext context, ItemStack itemStack, InteractionHand useItemHand) {
        ServerLevel level = convertToVanilla(context.getWorld());
        ServerPlayer player = convertToVanilla(context.getPlayer());
        return new UseOnContext(level, player, useItemHand, itemStack, _unwrap(context.getHitResult()));
    }

    private static BlockHitResult _unwrap(net.cocoonmc.core.item.context.BlockHitResult hitResult) {
        Vec3 loc = _unwrap(hitResult.getLocation());
        BlockPos pos = _unwrap(hitResult.getBlockPos());
        Direction dir = _unwrap(hitResult.getDirection());
        switch (hitResult.getType()) {
            case MISS:
                return BlockHitResult.miss(loc, dir, pos);

            case ENTITY:
            case BLOCK:
                return new BlockHitResult(loc, dir, pos, hitResult.isInside());

            default:
                return null;
        }
    }

    public static class ItemStackWrapper extends net.cocoonmc.core.item.ItemStack {

        private final ItemStackContext context;

        public ItemStackWrapper(ItemStackContext context) {
            super(context.getItem(), context.getCount());
            this.tag = context.getTag();
            this.context = context;
        }

        @Override
        public void setCount(int count) {
            super.setCount(count);
            context.setCount(count);
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

        @Override
        public net.cocoonmc.core.item.ItemStack copy() {
            if (context.isBreakChanges()) {
                return Versions.ITEM.wrap(context.newBukkitStack());
            }
            return Versions.ITEM.wrap(context.bukkitStack.clone());
        }

        public net.minecraft.world.item.ItemStack asVanilla() {
            ItemStack itemStack = context.getVanillaItem();
            if (itemStack != null) {
                return itemStack;
            }
            return copyToVanilla(asBukkit());
        }

        @Override
        public org.bukkit.inventory.ItemStack asBukkit() {
            if (context.isBreakChanges()) {
                return context.newBukkitStack();
            }
            return context.bukkitStack;
        }

    }

    public static class ItemStackContext {

        org.bukkit.inventory.ItemStack bukkitStack;
        net.minecraft.world.item.ItemStack vanillaItem;

        CompoundTag tag;
        Item item;

        int flags = 0; // 0x01 load tag, 0x02 load vanilla stack, 0x08 load item

        ItemStackContext(org.bukkit.inventory.ItemStack bukkitStack) {
            this.bukkitStack = bukkitStack;
        }

        boolean isBreakChanges() {
            // we can't write it, but new tag is required.
            return vanillaItem == null && tag != null;
        }

        void setCount(int count) {
            bukkitStack.setAmount(count);
        }

        int getCount() {
            return bukkitStack.getAmount();
        }

        CompoundTag getTag() {
            if ((flags & 0x01) == 0) {
                flags |= 0x01;
                tag = loadVanillaTag();
            }
            return tag;
        }

        void setTag(CompoundTag newValue) {
            flags |= 0x01;
            tag = newValue;
            ItemStack stack = getVanillaItem();
            if (stack == null) {
                return;
            }
            if (newValue != null) {
                stack.setTag(TagFactoryImpl.unwrap(newValue));
            } else {
                stack.setTag(null);
            }
        }

        Item getItem() {
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

        void setVanillaItem(net.minecraft.world.item.ItemStack itemStack) {
            flags |= 0x02;
            vanillaItem = itemStack;
        }

        ItemStack getVanillaItem() {
            if ((flags & 0x02) == 0) {
                flags |= 0x02;
                vanillaItem = loadVanillaItem();
            }
            return vanillaItem;
        }

        org.bukkit.inventory.ItemStack newBukkitStack() {
            ItemStack stack = copyToVanilla(bukkitStack);
            if (tag != null) {
                stack.setTag(TagFactoryImpl.unwrap(tag.copy()));
            }
            return convertToBukkit(stack);
        }

        public net.cocoonmc.core.item.ItemStack newCocoonStack() {
            return new ItemStackWrapper(this);
        }

        private CompoundTag loadVanillaTag() {
            if (!bukkitStack.hasItemMeta()) {
                return null;
            }
            ItemStack stack = getVanillaItem();
            if (stack != null && stack.getTag() != null) {
                return TagFactoryImpl.wrap(stack.getTag());
            }
            return null;
        }

        private ItemStack loadVanillaItem() {
            return ReflectUtils.getMemberField(bukkitStack.getClass(), "handle").get(bukkitStack);
        }

    }
}

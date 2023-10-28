package net.cocoonmc.runtime.v1_19_R1;

import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.ReflectUtils;
import net.cocoonmc.runtime.IItemFactory;
import net.cocoonmc.runtime.impl.ItemStackWrapper;
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
import org.jetbrains.annotations.Nullable;

public class ItemFactory extends TransformFactory implements IItemFactory {

    @Override
    public net.cocoonmc.core.item.ItemStack convertTo(org.bukkit.inventory.ItemStack itemStack) {
        return convertToCocoon(itemStack);
    }

    @Override
    public org.bukkit.inventory.ItemStack convertTo(net.cocoonmc.core.item.ItemStack itemStack) {
        return convertToBukkit(itemStack);
    }

    @Override
    public net.cocoonmc.core.world.InteractionResult useOn(net.cocoonmc.core.item.ItemStack itemStackIn, net.cocoonmc.core.item.context.UseOnContext context) {
        InteractionHand useItemHand = _unwrap(context.getHand());
        ItemStack itemStack = copyToVanilla(convertToBukkit(itemStackIn));
        return _wrap(itemStack.useOn(_unwrap(context, itemStack, useItemHand), useItemHand));
    }

    public static ItemStackWrapper<org.bukkit.inventory.ItemStack, ItemStack> createItemWrapper(org.bukkit.inventory.ItemStack bukkitStack, net.minecraft.world.item.ItemStack vanillaStack) {
        return new ItemStackWrapper<org.bukkit.inventory.ItemStack, ItemStack>(bukkitStack, vanillaStack) {

            @Override
            protected ItemStack copyTo(org.bukkit.inventory.ItemStack itemStack) {
                return TransformFactory.copyToVanilla(itemStack);
            }

            @Override
            protected org.bukkit.inventory.ItemStack convertTo(ItemStack itemStack) {
                return TransformFactory.convertToBukkit(itemStack);
            }

            @Nullable
            @Override
            protected ItemStack loadVanillaStack(org.bukkit.inventory.ItemStack bukkitStack) {
                return ReflectUtils.getMemberField(bukkitStack.getClass(), "handle").get(bukkitStack);
            }

            @Nullable
            @Override
            protected CompoundTag loadVanillaTag(ItemStack itemStack) {
                if (itemStack.getTag() != null) {
                    return TagFactory.wrap(itemStack.getTag());
                }
                return null;
            }

            @Override
            protected void saveVanillaTag(ItemStack itemStack, CompoundTag tag) {
                if (tag != null) {
                    itemStack.setTag(TagFactory.unwrap(tag));
                } else {
                    itemStack.setTag(null);
                }
            }
        };
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
            case ENTITY:
            case BLOCK:
                return new BlockHitResult(loc, dir, pos, hitResult.isInside());

            default:
                return BlockHitResult.miss(loc, dir, pos);
        }
    }
}

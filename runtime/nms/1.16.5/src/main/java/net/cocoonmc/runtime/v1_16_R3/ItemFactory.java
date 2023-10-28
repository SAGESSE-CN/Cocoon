package net.cocoonmc.runtime.v1_16_R3;

import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.ReflectUtils;
import net.cocoonmc.runtime.IItemFactory;
import net.cocoonmc.runtime.impl.ItemStackWrapper;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumDirection;
import net.minecraft.server.v1_16_R3.EnumHand;
import net.minecraft.server.v1_16_R3.EnumInteractionResult;
import net.minecraft.server.v1_16_R3.ItemActionContext;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.MovingObjectPositionBlock;
import net.minecraft.server.v1_16_R3.Vec3D;
import net.minecraft.server.v1_16_R3.WorldServer;
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
        EnumHand useItemHand = _unwrap(context.getHand());
        ItemStack itemStack = copyToVanilla(convertToBukkit(itemStackIn));
        return _wrap(itemStack.placeItem(_unwrap(context, itemStack, useItemHand), useItemHand));
    }

    public static ItemStackWrapper<org.bukkit.inventory.ItemStack, ItemStack> createItemWrapper(org.bukkit.inventory.ItemStack bukkitStack, net.minecraft.server.v1_16_R3.ItemStack vanillaStack) {
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

    private static net.cocoonmc.core.world.InteractionResult _wrap(EnumInteractionResult result) {
        return net.cocoonmc.core.world.InteractionResult.values()[result.ordinal()];
    }

    private static Vec3D _unwrap(net.cocoonmc.core.math.Vector3f vector) {
        return new Vec3D(vector.getX(), vector.getY(), vector.getZ());
    }

    private static EnumDirection _unwrap(net.cocoonmc.core.Direction dir) {
        return EnumDirection.values()[dir.ordinal()];
    }

    private static EnumHand _unwrap(net.cocoonmc.core.world.InteractionHand hand) {
        return EnumHand.values()[hand.ordinal()];
    }

    private static BlockPosition _unwrap(net.cocoonmc.core.BlockPos pos) {
        return new BlockPosition(pos.getX(), pos.getY(), pos.getZ());
    }


    private static ItemActionContext _unwrap(net.cocoonmc.core.item.context.UseOnContext context, ItemStack itemStack, EnumHand useItemHand) {
        WorldServer level = convertToVanilla(context.getWorld());
        EntityPlayer player = convertToVanilla(context.getPlayer());
        return new ItemActionContext(level, player, useItemHand, itemStack, _unwrap(context.getHitResult()));
    }

    private static MovingObjectPositionBlock _unwrap(net.cocoonmc.core.item.context.BlockHitResult hitResult) {
        Vec3D loc = _unwrap(hitResult.getLocation());
        BlockPosition pos = _unwrap(hitResult.getBlockPos());
        EnumDirection dir = _unwrap(hitResult.getDirection());
        switch (hitResult.getType()) {
            case ENTITY:
            case BLOCK:
                return new MovingObjectPositionBlock(loc, dir, pos, hitResult.isInside());

            default:
                return MovingObjectPositionBlock.a(loc, dir, pos);
        }
    }
}

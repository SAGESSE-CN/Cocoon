package net.cocoonmc.runtime.v1_18_R2;

import net.cocoonmc.core.utils.ReflectHelper;
import net.cocoonmc.runtime.IItemFactory;
import net.cocoonmc.runtime.impl.ItemStackAccessor;
import net.cocoonmc.runtime.impl.ItemStackTransformer;
import net.cocoonmc.runtime.impl.ItemStackWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemFactory extends TransformFactory implements IItemFactory {

    public static final org.bukkit.inventory.ItemStack EMPTY = new org.bukkit.inventory.ItemStack(Material.AIR, 0);

    public static final ItemStackTransformer<net.cocoonmc.core.item.ItemStack, org.bukkit.inventory.ItemStack, ItemStack> ITEM_TRANSFORMER = new ItemStackTransformer<>(_createCocoonLayer(), _createBukkitLayer(), _createVanillaLayer());

    @Override
    public net.cocoonmc.core.item.ItemStack convertTo(org.bukkit.inventory.ItemStack itemStack) {
        return ITEM_TRANSFORMER.convertToCocoon(itemStack);
    }

    @Override
    public org.bukkit.inventory.ItemStack convertTo(net.cocoonmc.core.item.ItemStack itemStack) {
        return ITEM_TRANSFORMER.convertToBukkit(itemStack);
    }

    @Override
    public net.cocoonmc.core.world.InteractionResult useOn(net.cocoonmc.core.item.ItemStack itemStackIn, net.cocoonmc.core.item.context.UseOnContext context) {
        InteractionHand useItemHand = _unwrap(context.getHand());
        ItemStack itemStack = ITEM_TRANSFORMER.convertToVanilla(itemStackIn);
        InteractionResult result = itemStack.useOn(_unwrap(context, itemStack, useItemHand), useItemHand);
        itemStackIn.setCount(itemStack.getCount());
        return _wrap(result);
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

    private static UseOnContext _unwrap(net.cocoonmc.core.item.context.UseOnContext context, ItemStack itemStack, InteractionHand useItemHand) {
        ServerLevel level = convertToVanilla(context.getLevel());
        ServerPlayer player = convertToVanilla(context.getPlayer());
        return new UseOnContext(level, player, useItemHand, itemStack, _unwrap(context.getHitResult()));
    }

    private static BlockHitResult _unwrap(net.cocoonmc.core.item.context.BlockHitResult hitResult) {
        Vec3 loc = _unwrap(hitResult.getLocation());
        BlockPos pos = convertToVanilla(hitResult.getBlockPos());
        Direction dir = _unwrap(hitResult.getDirection());
        switch (hitResult.getType()) {
            case ENTITY:
            case BLOCK:
                return new BlockHitResult(loc, dir, pos, hitResult.isInside());

            default:
                return BlockHitResult.miss(loc, dir, pos);
        }
    }

    private static ItemStackTransformer.Layer<net.cocoonmc.core.item.ItemStack> _createCocoonLayer() {
        return new ItemStackTransformer.Layer<net.cocoonmc.core.item.ItemStack>() {
            @Override
            public net.cocoonmc.core.item.ItemStack empty() {
                return net.cocoonmc.core.item.ItemStack.EMPTY;
            }

            @Override
            public boolean isEmpty(net.cocoonmc.core.item.ItemStack itemStack) {
                return itemStack.isEmpty();
            }

            @Override
            public net.cocoonmc.core.nbt.CompoundTag serialize(net.cocoonmc.core.item.ItemStack itemStack) {
                return itemStack.save(net.cocoonmc.core.nbt.CompoundTag.newInstance());
            }

            @Override
            public net.cocoonmc.core.item.ItemStack deserialize(net.cocoonmc.core.nbt.CompoundTag tag) {
                return net.cocoonmc.core.item.ItemStack.of(tag);
            }

            @Override
            public net.cocoonmc.core.item.ItemStack mirror(Object... itemStack) {
                org.bukkit.inventory.ItemStack bukkitStack = (org.bukkit.inventory.ItemStack) itemStack[0];
                ItemStack vanillaStack = (ItemStack) itemStack[1];
                return new ItemStackWrapper<>(bukkitStack, new ItemStackAccessor() {

                    @Override
                    public void setCount(int count) {
                        vanillaStack.setCount(count);
                    }

                    @Override
                    public net.cocoonmc.core.nbt.CompoundTag getTag() {
                        if (vanillaStack.getTag() != null) {
                            return TagFactory.wrap(vanillaStack.getTag());
                        }
                        return null;
                    }

                    @Override
                    public void setTag(net.cocoonmc.core.nbt.CompoundTag tag) {
                        if (tag != null) {
                            vanillaStack.setTag(TagFactory.unwrap(tag));
                        } else {
                            vanillaStack.setTag(null);
                        }
                    }
                });
            }

            @Override
            public net.cocoonmc.core.item.ItemStack copy(net.cocoonmc.core.item.ItemStack itemStack) {
                return itemStack.copy();
            }

            @Nullable
            @Override
            public net.cocoonmc.core.item.ItemStack get(Object itemStack) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static ItemStackTransformer.Layer<org.bukkit.inventory.ItemStack> _createBukkitLayer() {
        return new ItemStackTransformer.Layer<org.bukkit.inventory.ItemStack>() {
            @Override
            public org.bukkit.inventory.ItemStack empty() {
                return EMPTY;
            }

            @Override
            public boolean isEmpty(org.bukkit.inventory.ItemStack itemStack) {
                return itemStack == null || itemStack == EMPTY || itemStack.getType() == Material.AIR || itemStack.getAmount() <= 0;
            }

            @Override
            public net.cocoonmc.core.nbt.CompoundTag serialize(org.bukkit.inventory.ItemStack itemStack) {
                throw new UnsupportedOperationException();
            }

            @Override
            public org.bukkit.inventory.ItemStack deserialize(net.cocoonmc.core.nbt.CompoundTag tag) {
                throw new UnsupportedOperationException();
            }

            @Override
            public org.bukkit.inventory.ItemStack mirror(Object... itemStack) {
                // we only support vanilla stack.
                return CraftItemStack.asCraftMirror((ItemStack) itemStack[0]);
            }

            @Override
            public org.bukkit.inventory.ItemStack copy(org.bukkit.inventory.ItemStack itemStack) {
                return itemStack.clone();
            }

            @Nullable
            @Override
            public org.bukkit.inventory.ItemStack get(Object itemStack) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static ItemStackTransformer.Layer<ItemStack> _createVanillaLayer() {
        return new ItemStackTransformer.Layer<ItemStack>() {
            @Override
            public ItemStack empty() {
                return ItemStack.EMPTY;
            }

            @Override
            public boolean isEmpty(ItemStack itemStack) {
                return itemStack.isEmpty();
            }

            @Override
            public net.cocoonmc.core.nbt.CompoundTag serialize(ItemStack itemStack) {
                return TagFactory.wrap(itemStack.save(new CompoundTag()));
            }

            @Override
            public ItemStack deserialize(net.cocoonmc.core.nbt.CompoundTag tag) {
                return ItemStack.of(TagFactory.unwrap(tag));
            }

            @Override
            public ItemStack mirror(Object... itemStack) {
                // we only support bukkit stack.
                return CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) itemStack[0]);
            }

            @Override
            public ItemStack copy(ItemStack itemStack) {
                return itemStack.copy();
            }

            @Nullable
            @Override
            public ItemStack get(Object itemStack) {
                // try to get the vanilla stack via reflection,
                if (itemStack instanceof CraftItemStack) {
                    return ReflectHelper.getMemberField(itemStack.getClass(), "handle").get(itemStack);
                }
                return null;
            }
        };
    }
}

package net.cocoonmc.runtime.v1_16_R3;

import net.cocoonmc.core.utils.ReflectHelper;
import net.cocoonmc.runtime.IItemFactory;
import net.cocoonmc.runtime.impl.ItemStackAccessor;
import net.cocoonmc.runtime.impl.ItemStackTransformer;
import net.cocoonmc.runtime.impl.ItemStackWrapper;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumDirection;
import net.minecraft.server.v1_16_R3.EnumHand;
import net.minecraft.server.v1_16_R3.EnumInteractionResult;
import net.minecraft.server.v1_16_R3.ItemActionContext;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.MovingObjectPositionBlock;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.Vec3D;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
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
        EnumHand useItemHand = _unwrap(context.getHand());
        ItemStack itemStack = ITEM_TRANSFORMER.convertToVanilla(itemStackIn);
        return _wrap(itemStack.placeItem(_unwrap(context, itemStack, useItemHand), useItemHand));
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

    private static ItemActionContext _unwrap(net.cocoonmc.core.item.context.UseOnContext context, ItemStack itemStack, EnumHand useItemHand) {
        WorldServer level = convertToVanilla(context.getLevel());
        EntityPlayer player = convertToVanilla(context.getPlayer());
        return new ItemActionContext(level, player, useItemHand, itemStack, _unwrap(context.getHitResult()));
    }

    private static MovingObjectPositionBlock _unwrap(net.cocoonmc.core.item.context.BlockHitResult hitResult) {
        Vec3D loc = _unwrap(hitResult.getLocation());
        BlockPosition pos = convertToVanilla(hitResult.getBlockPos());
        EnumDirection dir = _unwrap(hitResult.getDirection());
        switch (hitResult.getType()) {
            case ENTITY:
            case BLOCK:
                return new MovingObjectPositionBlock(loc, dir, pos, hitResult.isInside());

            default:
                return MovingObjectPositionBlock.a(loc, dir, pos);
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
                return ItemStack.b;
            }

            @Override
            public boolean isEmpty(ItemStack itemStack) {
                return itemStack.isEmpty();
            }

            @Override
            public net.cocoonmc.core.nbt.CompoundTag serialize(ItemStack itemStack) {
                return TagFactory.wrap(itemStack.save(new NBTTagCompound()));
            }

            @Override
            public ItemStack deserialize(net.cocoonmc.core.nbt.CompoundTag tag) {
                return ItemStack.a((NBTTagCompound) TagFactory.unwrap(tag));
            }

            @Override
            public ItemStack mirror(Object... itemStack) {
                // we only support bukkit stack.
                return CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) itemStack[0]);
            }

            @Override
            public ItemStack copy(ItemStack itemStack) {
                return itemStack.cloneItemStack();
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

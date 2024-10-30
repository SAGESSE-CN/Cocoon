package net.cocoonmc.runtime.v1_21_R1;

import net.cocoonmc.core.utils.ReflectHelper;
import net.cocoonmc.runtime.IItemFactory;
import net.cocoonmc.runtime.impl.ItemStackAccessor;
import net.cocoonmc.runtime.impl.ItemStackTransformer;
import net.cocoonmc.runtime.impl.ItemStackWrapper;
import net.cocoonmc.runtime.impl.TagComponentMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.BlockHitResult;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemFactory extends TransformFactory implements IItemFactory {

    public static final org.bukkit.inventory.ItemStack EMPTY = new org.bukkit.inventory.ItemStack(Material.AIR, 0);


    public static final ItemStackTransformer<net.cocoonmc.core.item.ItemStack, org.bukkit.inventory.ItemStack, ItemStack> ITEM_TRANSFORMER = new ItemStackTransformer<>(_createCocoonLayer(), _createBukkitLayer(), _createVanillaLayer());

    @Override
    public net.cocoonmc.core.component.DataComponentMap createDataComponents(net.cocoonmc.core.item.Item item) {
        Item vanillaItem = convertToVanilla(item);
        return new WrappedDataComponentMap(new PatchedDataComponentMap(vanillaItem.components()));
    }

    @Override
    public void registerDataComponentType(net.cocoonmc.core.component.DataComponentType<?> type) {
    }

    @Override
    public net.cocoonmc.core.item.ItemStack convertTo(org.bukkit.inventory.ItemStack itemStack) {
        return ITEM_TRANSFORMER.convertToCocoon(itemStack);
    }

    @Override
    public org.bukkit.inventory.ItemStack convertTo(net.cocoonmc.core.item.ItemStack itemStack) {
        return ITEM_TRANSFORMER.convertToBukkit(itemStack);
    }

    @Override
    public net.cocoonmc.core.world.InteractionResult useOn(net.cocoonmc.core.world.entity.Player player, net.cocoonmc.core.item.ItemStack itemStackIn, net.cocoonmc.core.item.context.UseOnContext context) {
        ServerLevel serverLevel = convertToVanilla(context.getLevel());
        ServerPlayer serverPlayer = convertToVanilla(player);
        InteractionHand useItemHand = convertToVanilla(context.getHand());
        ItemStack itemStack = convertToVanilla(itemStackIn);
        BlockHitResult hitResult = convertToVanilla(context.getHitResult());
        return _wrap(serverPlayer.gameMode.useItemOn(serverPlayer, serverLevel, itemStack, useItemHand, hitResult));
    }

    private static net.cocoonmc.core.world.InteractionResult _wrap(InteractionResult result) {
        return net.cocoonmc.core.world.InteractionResult.values()[result.ordinal()];
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
                return ItemStackWrapper.unsafeSerialize(itemStack, net.cocoonmc.core.nbt.CompoundTag.newInstance());
            }

            @Override
            public net.cocoonmc.core.item.ItemStack deserialize(net.cocoonmc.core.nbt.CompoundTag tag) {
                return ItemStackWrapper.unsafeDeserialize(tag);
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
                    public net.cocoonmc.core.component.DataComponentMap getComponents() {
                        return new WrappedDataComponentMap(vanillaStack.getComponents());
                    }

                    @Override
                    public void setComponents(net.cocoonmc.core.component.DataComponentMap components) {
                        if (components instanceof WrappedDataComponentMap components1) {
                            components1.applyTo(vanillaStack.getComponents());
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
                HolderLookup.Provider provider = getCurrentServer().registryAccess();
                return TagFactory.wrap((CompoundTag) itemStack.save(provider));
            }

            @Override
            public ItemStack deserialize(net.cocoonmc.core.nbt.CompoundTag tag) {
                HolderLookup.Provider provider = getCurrentServer().registryAccess();
                return ItemStack.parseOptional(provider, TagFactory.unwrap(tag));
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
                    return ReflectHelper.getMember(itemStack, "handle");
                }
                return null;
            }
        };
    }

    private static class WrappedDataComponentMap extends TagComponentMap {

        private final DataComponentMap components;

        public WrappedDataComponentMap(DataComponentMap components) {
            super(null);
            this.components = components;
        }

        public void applyTo(DataComponentMap otherComponents) {
            // ignore when same object.
            if (otherComponents != components && otherComponents instanceof PatchedDataComponentMap otherComponents1) {
                otherComponents1.setAll(components);
            }
        }

        @Override
        public net.cocoonmc.core.component.DataComponentMap copy() {
            return new WrappedDataComponentMap(new PatchedDataComponentMap(components));
        }

        @Override
        protected net.cocoonmc.core.nbt.CompoundTag getTag() {
            if (tag == null && components.has(DataComponents.CUSTOM_DATA)) {
                tag = TagFactory.wrap(components.get(DataComponents.CUSTOM_DATA).getUnsafe());
            }
            return tag;
        }

        @Override
        protected net.cocoonmc.core.nbt.CompoundTag getOrCreateTag() {
            net.cocoonmc.core.nbt.CompoundTag tag1 = getTag();
            if (tag1 != null) {
                return tag1;
            }
            CustomData customData = CustomData.of(new CompoundTag());
            if (components instanceof PatchedDataComponentMap components1) {
                components1.set(DataComponents.CUSTOM_DATA, customData);
            }
            tag = TagFactory.wrap(customData.getUnsafe());
            tagDidChange();
            return tag;
        }
    }
}

package net.cocoonmc.runtime.impl;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.runtime.IAssociatedContainerKey;
import org.jetbrains.annotations.Nullable;

public class ItemStackTransformer<CocoonItemStack extends net.cocoonmc.core.item.ItemStack, BukkitItemStack extends org.bukkit.inventory.ItemStack, VanillaItemStack> {

    protected final Layer<CocoonItemStack> cocoonLayer;
    protected final Layer<BukkitItemStack> bukkitLayer;
    protected final Layer<VanillaItemStack> vanillaLayer;

    protected final IAssociatedContainerKey<CocoonItemStack> cocoonCacheKey;
    protected final IAssociatedContainerKey<BukkitItemStack> bukkitCacheKey;
    protected final IAssociatedContainerKey<VanillaItemStack> vanillaCacheKey;

    public ItemStackTransformer(Layer<CocoonItemStack> cocoonLayer, Layer<BukkitItemStack> bukkitLayer, Layer<VanillaItemStack> vanillaLayer) {
        this.cocoonLayer = cocoonLayer;
        this.cocoonCacheKey = cocoonLayer.getCacheKey();
        this.bukkitLayer = bukkitLayer;
        this.bukkitCacheKey = bukkitLayer.getCacheKey();
        this.vanillaLayer = vanillaLayer;
        this.vanillaCacheKey = vanillaLayer.getCacheKey();
    }


    public BukkitItemStack convertToBukkit(CocoonItemStack itemStack) {
        if (cocoonLayer.isEmpty(itemStack)) {
            return bukkitLayer.empty();
        }
        return Cocoon.API.CACHE.getOrElse(itemStack, bukkitCacheKey, cocoonStack -> {
            CompoundTag tag = cocoonLayer.serialize(cocoonStack);
            VanillaItemStack newVanillaStack = vanillaLayer.deserialize(tag);
            BukkitItemStack newBukkitStack = bukkitLayer.mirror(newVanillaStack);
            setRelation(cocoonStack, newVanillaStack, newBukkitStack);
            setDelegate(cocoonStack, newVanillaStack, newBukkitStack);
            return newBukkitStack;
        });
    }

    public BukkitItemStack convertToBukkit(VanillaItemStack itemStack) {
        if (vanillaLayer.isEmpty(itemStack)) {
            return bukkitLayer.empty();
        }
        return Cocoon.API.CACHE.getOrElse(itemStack, bukkitLayer.getCacheKey(), vanillaStack -> {
            BukkitItemStack bukkitStack = bukkitLayer.mirror(vanillaStack);
            CocoonItemStack cocoonStack = cocoonLayer.mirror(bukkitStack, vanillaStack);
            setRelation(cocoonStack, vanillaStack, bukkitStack);
            return bukkitStack;
        });
    }


    public CocoonItemStack convertToCocoon(BukkitItemStack itemStack) {
        if (bukkitLayer.isEmpty(itemStack)) {
            return cocoonLayer.empty();
        }
        return Cocoon.API.CACHE.getOrElse(itemStack, cocoonCacheKey, bukkitStack -> {
            // we will try to get the vanilla stack from the bukkit stack,
            VanillaItemStack vanillaStack = vanillaLayer.get(bukkitStack);
            if (vanillaStack != null) {
                CocoonItemStack cocoonStack = cocoonLayer.mirror(bukkitStack, vanillaStack);
                setRelation(cocoonStack, vanillaStack, bukkitStack);
                return cocoonStack;
            }
            // if get is fails, it means this a new bukkit stack by the user.
            BukkitItemStack newBukkitStack = bukkitLayer.copy(bukkitStack);
            VanillaItemStack newVanillaStack = vanillaLayer.mirror(newBukkitStack);
            CocoonItemStack newCocoonStack = cocoonLayer.mirror(newBukkitStack, newVanillaStack);
            setRelation(newCocoonStack, newVanillaStack, newBukkitStack);
            return newCocoonStack;
        });
    }

    public CocoonItemStack convertToCocoon(VanillaItemStack itemStack) {
        if (vanillaLayer.isEmpty(itemStack)) {
            return cocoonLayer.empty();
        }
        return Cocoon.API.CACHE.getOrElse(itemStack, cocoonCacheKey, vanillaStack -> {
            BukkitItemStack bukkitStack = bukkitLayer.mirror(vanillaStack);
            CocoonItemStack cocoonStack = cocoonLayer.mirror(bukkitStack, vanillaStack);
            setRelation(cocoonStack, vanillaStack, bukkitStack);
            return cocoonStack;
        });
    }


    public VanillaItemStack convertToVanilla(CocoonItemStack itemStack) {
        if (cocoonLayer.isEmpty(itemStack)) {
            return vanillaLayer.empty();
        }
        return Cocoon.API.CACHE.getOrElse(itemStack, vanillaCacheKey, cocoonStack -> {
            CompoundTag tag = cocoonLayer.serialize(cocoonStack);
            VanillaItemStack newVanillaStack = vanillaLayer.deserialize(tag);
            BukkitItemStack newBukkitStack = bukkitLayer.mirror(newVanillaStack);
            setRelation(cocoonStack, newVanillaStack, newBukkitStack);
            setDelegate(cocoonStack, newVanillaStack, newBukkitStack);
            return newVanillaStack;
        });
    }

    public VanillaItemStack convertToVanilla(BukkitItemStack itemStack) {
        if (bukkitLayer.isEmpty(itemStack)) {
            return vanillaLayer.empty();
        }
        return Cocoon.API.CACHE.getOrElse(itemStack, vanillaCacheKey, bukkitStack -> {
            // we will try to get the vanilla stack from the bukkit stack.
            VanillaItemStack vanillaStack = vanillaLayer.get(bukkitStack);
            if (vanillaStack != null) {
                CocoonItemStack cocoonStack = cocoonLayer.mirror(bukkitStack, vanillaStack);
                setRelation(cocoonStack, vanillaStack, bukkitStack);
                return vanillaStack;
            }
            // if get is fails, it means this a new bukkit stack by the user.
            BukkitItemStack newBukkitStack = bukkitLayer.copy(bukkitStack);
            VanillaItemStack newVanillaStack = vanillaLayer.mirror(newBukkitStack);
            CocoonItemStack newCocoonStack = cocoonLayer.mirror(newBukkitStack, newVanillaStack);
            setRelation(newCocoonStack, newVanillaStack, newBukkitStack);
            return newVanillaStack;
        });
    }


    public Layer<CocoonItemStack> getCocoonLayer() {
        return cocoonLayer;
    }

    public IAssociatedContainerKey<CocoonItemStack> getCocoonCacheKey() {
        return cocoonCacheKey;
    }

    public Layer<BukkitItemStack> getBukkitLayer() {
        return bukkitLayer;
    }

    public IAssociatedContainerKey<BukkitItemStack> getBukkitCacheKey() {
        return bukkitCacheKey;
    }

    public Layer<VanillaItemStack> getVanillaLayer() {
        return vanillaLayer;
    }

    public IAssociatedContainerKey<VanillaItemStack> getVanillaCacheKey() {
        return vanillaCacheKey;
    }

    private void setRelation(CocoonItemStack cocoonStack, VanillaItemStack vanillaStack, BukkitItemStack bukkitStack) {
        Cocoon.API.CACHE.set(cocoonStack, bukkitCacheKey, bukkitStack);
        Cocoon.API.CACHE.set(cocoonStack, vanillaCacheKey, vanillaStack);
        Cocoon.API.CACHE.set(bukkitStack, cocoonCacheKey, cocoonStack);
        Cocoon.API.CACHE.set(bukkitStack, vanillaCacheKey, vanillaStack);
        Cocoon.API.CACHE.set(vanillaStack, cocoonCacheKey, cocoonStack);
        Cocoon.API.CACHE.set(vanillaStack, bukkitCacheKey, bukkitStack);
    }

    private void setDelegate(CocoonItemStack cocoonStack, VanillaItemStack vanillaStack, BukkitItemStack bukkitStack) {
        // create a special mirror stack, original stack all changes will apply to it.
        CocoonItemStack newCocoonStack = cocoonLayer.mirror(bukkitStack, vanillaStack);
        Cocoon.API.CACHE.set(newCocoonStack, bukkitCacheKey, bukkitStack);
        Cocoon.API.CACHE.set(newCocoonStack, vanillaCacheKey, vanillaStack);
        cocoonStack.setDelegate(newCocoonStack);
    }

    public interface Layer<T> {

        T empty();

        boolean isEmpty(T itemStack);

        CompoundTag serialize(T itemStack);

        T deserialize(CompoundTag tag);

        T mirror(Object... itemStack);

        T copy(T itemStack);

        @Nullable
        T get(Object itemStack);

        IAssociatedContainerKey<T> getCacheKey();
    }
}

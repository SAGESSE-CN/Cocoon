package net.cocoonmc.runtime.impl;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.SimpleAssociatedKey;
import org.jetbrains.annotations.Nullable;

public class ItemStackTransformer<CocoonItemStack extends net.cocoonmc.core.item.ItemStack, BukkitItemStack extends org.bukkit.inventory.ItemStack, VanillaItemStack> {

    protected boolean needsTransmitChanges = true;
    
    protected final Layer<CocoonItemStack> cocoonLayer;
    protected final Layer<BukkitItemStack> bukkitLayer;
    protected final Layer<VanillaItemStack> vanillaLayer;

    protected final SimpleAssociatedKey<Relation> relationCacheKey;

    public ItemStackTransformer(Layer<CocoonItemStack> cocoonLayer, Layer<BukkitItemStack> bukkitLayer, Layer<VanillaItemStack> vanillaLayer) {
        this.cocoonLayer = cocoonLayer;
        this.bukkitLayer = bukkitLayer;
        this.vanillaLayer = vanillaLayer;
        this.relationCacheKey = SimpleAssociatedKey.of("Relation", getRelationClass());
    }

    public BukkitItemStack convertToBukkit(CocoonItemStack itemStack) {
        if (cocoonLayer.isEmpty(itemStack)) {
            return bukkitLayer.empty();
        }
        return getRelation(itemStack).getBukkitStack();
    }

    public BukkitItemStack convertToBukkit(VanillaItemStack itemStack) {
        if (vanillaLayer.isEmpty(itemStack)) {
            return bukkitLayer.empty();
        }
        return getRelation(itemStack).getBukkitStack();
    }

    public CocoonItemStack convertToCocoon(BukkitItemStack itemStack) {
        if (bukkitLayer.isEmpty(itemStack)) {
            return cocoonLayer.empty();
        }
        return getRelation(itemStack).getCocoonStack();
    }

    public CocoonItemStack convertToCocoon(VanillaItemStack itemStack) {
        if (vanillaLayer.isEmpty(itemStack)) {
            return cocoonLayer.empty();
        }
        return getRelation(itemStack).getCocoonStack();
    }


    public VanillaItemStack convertToVanilla(CocoonItemStack itemStack) {
        if (cocoonLayer.isEmpty(itemStack)) {
            return vanillaLayer.empty();
        }
        return getRelation(itemStack).getVanillaStack();
    }

    public VanillaItemStack convertToVanilla(BukkitItemStack itemStack) {
        if (bukkitLayer.isEmpty(itemStack)) {
            return vanillaLayer.empty();
        }
        return getRelation(itemStack).getVanillaStack();
    }


    public Layer<CocoonItemStack> getCocoonLayer() {
        return cocoonLayer;
    }

    public Layer<BukkitItemStack> getBukkitLayer() {
        return bukkitLayer;
    }

    public Layer<VanillaItemStack> getVanillaLayer() {
        return vanillaLayer;
    }


    private Relation createRelation(CocoonItemStack cocoonStack, VanillaItemStack vanillaStack, BukkitItemStack bukkitStack) {
        Relation relation = new Relation();
        relation.setCocoonStack(cocoonStack);
        relation.setVanillaStack(vanillaStack);
        relation.setBukkitStack(bukkitStack);
        Cocoon.API.CACHE.set(cocoonStack, relationCacheKey, relation);
        Cocoon.API.CACHE.set(vanillaStack, relationCacheKey, relation);
        Cocoon.API.CACHE.set(bukkitStack, relationCacheKey, relation);
        return relation;
    }

    private Relation getRelation(CocoonItemStack itemStack) {
        return Cocoon.API.CACHE.getOrElse(itemStack, relationCacheKey, cocoonStack -> {
            CompoundTag tag = cocoonLayer.serialize(cocoonStack);
            VanillaItemStack newVanillaStack = vanillaLayer.deserialize(tag);
            BukkitItemStack newBukkitStack = bukkitLayer.mirror(newVanillaStack);
            if (needsTransmitChanges) {
                // create a special mirror stack, original stack all changes will apply to it.
                CocoonItemStack newCocoonStack = cocoonLayer.mirror(newBukkitStack, newVanillaStack);
                Relation relation = createRelation(newCocoonStack, newVanillaStack, newBukkitStack);
                Cocoon.API.CACHE.set(newCocoonStack, relationCacheKey, relation);
                cocoonStack.setDelegate(newCocoonStack);
            }
            return createRelation(cocoonStack, newVanillaStack, newBukkitStack);
        });
    }

    private Relation getRelation(BukkitItemStack itemStack) {
        // bukkit will always create a new mirror stack, this is very detrimental to caching,
        // but luck the vanilla stack is the same, so we give priority to using vanilla stack.
        VanillaItemStack vanillaStack = vanillaLayer.get(itemStack);
        if (vanillaStack != null) {
            return getRelation(vanillaStack);
        }
        // if get is fails, it means this a new bukkit stack by the user.
        return Cocoon.API.CACHE.getOrElse(itemStack, relationCacheKey, bukkitStack -> {
            BukkitItemStack newBukkitStack = bukkitLayer.copy(bukkitStack);
            VanillaItemStack newVanillaStack = vanillaLayer.mirror(newBukkitStack);
            CocoonItemStack newCocoonStack = cocoonLayer.mirror(newBukkitStack, newVanillaStack);
            return createRelation(newCocoonStack, newVanillaStack, newBukkitStack);
        });
    }

    private Relation getRelation(VanillaItemStack itemStack) {
        return Cocoon.API.CACHE.getOrElse(itemStack, relationCacheKey, vanillaStack -> {
            BukkitItemStack bukkitStack = bukkitLayer.mirror(vanillaStack);
            CocoonItemStack cocoonStack = cocoonLayer.mirror(bukkitStack, vanillaStack);
            return createRelation(cocoonStack, vanillaStack, bukkitStack);
        });
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Relation> getRelationClass() {
        // we can't direct to use `Relation.class`, it will return the wrong class.
        Relation relation = new Relation();
        return (Class<? extends Relation>) relation.getClass();
    }


    public class Relation {

        private CocoonItemStack cocoonStack;
        private BukkitItemStack bukkitStack;
        private VanillaItemStack vanillaStack;

        public void setCocoonStack(CocoonItemStack cocoonStack) {
            this.cocoonStack = cocoonStack;
        }

        public CocoonItemStack getCocoonStack() {
            return cocoonStack;
        }

        public void setBukkitStack(BukkitItemStack bukkitStack) {
            this.bukkitStack = bukkitStack;
        }

        public BukkitItemStack getBukkitStack() {
            return bukkitStack;
        }

        public void setVanillaStack(VanillaItemStack vanillaStack) {
            this.vanillaStack = vanillaStack;
        }

        public VanillaItemStack getVanillaStack() {
            return vanillaStack;
        }
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
    }
}

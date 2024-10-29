package net.cocoonmc.core.item;

import com.mojang.serialization.Codec;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.component.DataComponentMap;
import net.cocoonmc.core.component.DataComponentType;
import net.cocoonmc.core.utils.ObjectHelper;
import net.cocoonmc.core.utils.SimpleAssociatedStorage;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;
import net.cocoonmc.runtime.IAssociatedContainer;
import net.cocoonmc.runtime.IAssociatedContainerProvider;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class ItemStack implements IAssociatedContainerProvider {

    public static final Codec<ItemStack> CODEC = Cocoon.API.CODEC.getItemStack();

    public static final ItemStack EMPTY = new ItemStack(Items.AIR, 0);

    protected int count;

    protected final Item item;
    protected final DataComponentMap components;

    private ItemStack delegate;
    private final SimpleAssociatedStorage storage = new SimpleAssociatedStorage();

    public ItemStack(Item item) {
        this(item, 1, Cocoon.API.ITEM.createComponents());
    }

    public ItemStack(Item item, int count) {
        this(item, count, Cocoon.API.ITEM.createComponents());
    }

    public ItemStack(Item item, int count, DataComponentMap components) {
        this.item = item;
        this.count = count;
        this.components = components;
    }


    public static ItemStack of(org.bukkit.inventory.ItemStack itemStack) {
        return Cocoon.API.ITEM.convertTo(itemStack);
    }


    public Item getItem() {
        if (isEmpty()) {
            return Items.AIR;
        }
        return item;
    }

    public boolean has(DataComponentType<?> componentType) {
        return components.has(componentType);
    }

    @Nullable
    public <T> T get(DataComponentType<? extends T> componentType) {
        return components.get(componentType);
    }

    public <T> T getOrDefault(DataComponentType<? extends T> componentType, T object) {
        return components.getOrDefault(componentType, object);
    }

    public <T> void set(DataComponentType<? super T> componentType, @Nullable T object) {
        components.set(componentType, object);
    }

    public void remove(DataComponentType<?> componentType) {
        components.remove(componentType);
    }

    public boolean isEmpty() {
        return this == EMPTY || item == Items.AIR || count <= 0;
    }

    public void applyComponents(DataComponentMap components) {
    }

    public DataComponentMap getComponents() {
        return components;
    }

    public ItemStack copy() {
        if (isEmpty()) {
            return EMPTY;
        }
        return new ItemStack(getItem(), getCount(), getComponents().copy());
    }

    public ItemStack copyWithCount(int i) {
        if (isEmpty()) {
            return EMPTY;
        }
        ItemStack itemstack = copy();
        itemstack.setCount(i);
        return itemstack;
    }

    public ItemStack split(int i) {
        int j = Math.min(i, getCount());
        ItemStack itemStack = copy();
        itemStack.setCount(j);
        shrink(j);
        return itemStack;
    }


    public void setCount(int count) {
        this.count = count;
        if (this.delegate != null) {
            this.delegate.setCount(count);
        }
    }

    public int getCount() {
        return count;
    }

    public void grow(int i) {
        this.setCount(count + i);
    }

    public void shrink(int i) {
        this.grow(-i);
    }

    public int getMaxStackSize() {
        return item.getMaxStackSize();
    }

    public boolean isStackable() {
        return getMaxStackSize() > 1;
    }

    public boolean isDamageableItem() {
        return false;
    }

    public void setDelegate(ItemStack delegate) {
        this.delegate = delegate;
    }

    public ItemStack getDelegate() {
        return delegate;
    }

    @Override
    public IAssociatedContainer getAssociatedContainer() {
        return storage;
    }


    /**
     * Should this item, when held, allow sneak-clicks to pass through to the
     * underlying block?
     *
     * @param player The Player that is wielding the item
     * @param level  The world
     * @param pos    Block position in level
     */
    public boolean doesSneakBypassUse(Player player, Level level, BlockPos pos) {
        return isEmpty() || getItem().doesSneakBypassUse(this, player, level, pos);
    }


    public static boolean tagMatches(ItemStack itemStack, ItemStack itemStack2) {
        if (itemStack.isEmpty() && itemStack2.isEmpty()) {
            return true;
        }
        if (!itemStack.isEmpty() && !itemStack2.isEmpty()) {
            return itemStack.components.equals(itemStack2.components);
        }
        return false;
    }

    public boolean is(Item item) {
        return getItem().equals(item);
    }

    //    public static boolean matches(ItemStack itemStack, ItemStack itemStack2) {
//        if (itemStack.isEmpty() && itemStack2.isEmpty()) {
//            return true;
//        } else {
//            return !itemStack.isEmpty() && !itemStack2.isEmpty() ? itemStack.matches(itemStack2) : false;
//        }
//    }
//
//    private boolean matches(ItemStack itemStack) {
//        if (this.count != itemStack.count) {
//            return false;
//        } else if (!this.is(itemStack.getItem())) {
//            return false;
//        } else if (this.tag == null && itemStack.tag != null) {
//            return false;
//        } else {
//            return this.tag == null || this.tag.equals(itemStack.tag);
//        }
//    }
//
    public static boolean isSame(ItemStack itemStack, ItemStack itemStack2) {
        if (itemStack == itemStack2) {
            return true;
        } else {
            return !itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.sameItem(itemStack2);
        }
    }

//    public static boolean isSameIgnoreDurability(ItemStack itemStack, ItemStack itemStack2) {
//        if (itemStack == itemStack2) {
//            return true;
//        } else {
//            return !itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.sameItemStackIgnoreDurability(itemStack2);
//        }
//    }

    public boolean sameItem(ItemStack itemStack) {
        return !itemStack.isEmpty() && item.equals(itemStack.item);
    }

    //    public boolean sameItemStackIgnoreDurability(ItemStack itemStack) {
//        if (!this.isDamageableItem()) {
//            return this.sameItem(itemStack);
//        } else {
//            return !itemStack.isEmpty() && id.equals(itemStack.id);
//        }
//    }
//
    public static boolean isSameItemSameTags(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack.item.equals(itemStack2.item) && tagMatches(itemStack, itemStack2);
    }

//    public boolean overrideStackedOnOther(Slot slot, ClickAction clickAction, Player player) {
//        return false;
//    }
//
//    public boolean overrideOtherStackedOnMe(ItemStack itemStack2, Slot slot, ClickAction clickAction, Player player, Object slotAccess) {
//        return false;
//    }

    public org.bukkit.inventory.ItemStack asBukkit() {
        return Cocoon.API.ITEM.convertTo(this);
    }

    @Override
    public String toString() {
        return ObjectHelper.makeDescription(this, "id", item.getRegistryName(), "count", count);
    }
}

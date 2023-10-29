package net.cocoonmc.core.item;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.Tag;
import net.cocoonmc.core.utils.BukkitUtils;
import net.cocoonmc.core.utils.SimpleAssociatedStorage;
import net.cocoonmc.runtime.IAssociatedContainer;
import net.cocoonmc.runtime.IAssociatedContainerProvider;
import net.cocoonmc.runtime.impl.Constants;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class ItemStack implements IAssociatedContainerProvider {

    public static final ItemStack EMPTY = new ItemStack(Items.AIR, 0);

    protected int count;

    @Nullable
    protected CompoundTag tag;

    protected final Item item;

    private ItemStack delegate;
    private final SimpleAssociatedStorage storage = new SimpleAssociatedStorage();

    public ItemStack(Item item) {
        this(item, 1, null);
    }

    public ItemStack(Item item, int count) {
        this(item, count, null);
    }

    public ItemStack(Item item, int count, @Nullable CompoundTag tag) {
        this.item = item;
        this.count = count;
        this.tag = tag;
    }

    public ItemStack(CompoundTag tag) {
        CompoundTag itemTag = null;
        if (tag.contains("tag", 10)) {
            itemTag = tag.getCompound("tag");
        }
        String wrapperId = tag.getString("id");
        String sourceId = BukkitUtils.getReadId(wrapperId, itemTag);
        this.item = Items.byId(sourceId);
        this.count = tag.getByte("Count");
        this.tag = itemTag;
    }

    public static ItemStack of(CompoundTag tag) {
        return new ItemStack(tag);
    }

    public static ItemStack of(org.bukkit.inventory.ItemStack itemStack) {
        return Cocoon.API.ITEM.convertTo(itemStack);
    }

    public CompoundTag save(CompoundTag outputTag) {
        String sourceId = item.getKey().toString();
        String wrapperId = BukkitUtils.getWrapperId(sourceId, tag, getMaxStackSize());
        outputTag.putString("id", wrapperId);
        outputTag.putByte("Count", (byte) count);
        CompoundTag itemTag = null;
        if (tag != null) {
            itemTag = tag.copy();
        }
        if (!wrapperId.equals(sourceId)) {
            if (itemTag == null) {
                itemTag = CompoundTag.newInstance();
            }
            itemTag.putString(Constants.ITEM_REDIRECTED_KEY, sourceId + "/" + wrapperId);
        }
        if (itemTag != null) {
            outputTag.put("tag", itemTag);
        }
        return outputTag;
    }

    public Item getItem() {
        if (isEmpty()) {
            return Items.AIR;
        }
        return item;
    }

    public boolean hasTag() {
        return tag != null && !tag.isEmpty();
    }

    public boolean hasTagElement(String key) {
        return tag != null && tag.contains(key);
    }

    public void setTag(@Nullable CompoundTag tag) {
        this.tag = tag;
        if (this.delegate != null) {
            this.delegate.setTag(tag);
        }
    }

    @Nullable
    public CompoundTag getTag() {
        return tag;
    }

    public CompoundTag getOrCreateTag() {
        if (tag != null) {
            return tag;
        }
        CompoundTag tag = CompoundTag.newInstance();
        setTag(tag);
        return tag;
    }

    public CompoundTag getOrCreateTagElement(String key) {
        if (tag != null && tag.contains(key, 10)) {
            return tag.getCompound(key);
        }
        CompoundTag tag = CompoundTag.newInstance();
        addTagElement(key, tag);
        return tag;
    }

    @Nullable
    public CompoundTag getTagElement(String string) {
        if (tag != null && tag.contains(string, 10)) {
            return tag.getCompound(string);
        }
        return null;
    }

    public void addTagElement(String key, Tag value) {
        getOrCreateTag().put(key, value);
    }

    public void removeTagKey(String string) {
        if (tag != null && tag.contains(string)) {
            tag.remove(string);
            if (tag.isEmpty()) {
                setTag(null);
            }
        }
    }

    public boolean isEmpty() {
        return this == EMPTY || item == Items.AIR || count <= 0;
    }


    public ItemStack copy() {
        if (isEmpty()) {
            return EMPTY;
        }
        ItemStack itemStack = new ItemStack(getItem(), getCount());
        CompoundTag tag = getTag();
        if (tag != null) {
            itemStack.tag = tag.copy();
        }
        return itemStack;
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
     * @param world  The world
     * @param pos    Block position in level
     */
    public boolean doesSneakBypassUse(Player player, World world, BlockPos pos) {
        return isEmpty() || getItem().doesSneakBypassUse(this, player, world, pos);
    }


    public static boolean tagMatches(ItemStack itemStack, ItemStack itemStack2) {
        if (itemStack.isEmpty() && itemStack2.isEmpty()) {
            return true;
        } else if (!itemStack.isEmpty() && !itemStack2.isEmpty()) {
            if (itemStack.getTag() == null && itemStack2.getTag() != null) {
                return false;
            } else {
                return itemStack.getTag() == null || itemStack.getTag().equals(itemStack2.getTag());
            }
        } else {
            return false;
        }
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
        return "" + count + " " + item.getKey();
    }
}

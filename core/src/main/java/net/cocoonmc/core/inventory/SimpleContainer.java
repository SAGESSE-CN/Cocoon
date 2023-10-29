package net.cocoonmc.core.inventory;

import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.utils.ContainerHelper;
import net.cocoonmc.core.utils.NonNullList;
import net.cocoonmc.core.utils.SimpleAssociatedStorage;
import net.cocoonmc.runtime.IAssociatedContainer;
import net.cocoonmc.runtime.IAssociatedContainerProvider;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SimpleContainer implements Container, IAssociatedContainerProvider {

    private final int size;
    private final NonNullList<ItemStack> items;
    private final SimpleAssociatedStorage storage = new SimpleAssociatedStorage();

    private ArrayList<ContainerListener> listeners;

    public SimpleContainer(int size) {
        this.size = size;
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public SimpleContainer(ItemStack... itemStacks) {
        this.size = itemStacks.length;
        this.items = NonNullList.of(ItemStack.EMPTY, itemStacks);
    }

    public void addListener(ContainerListener containerListener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList<>();
        }
        this.listeners.add(containerListener);
    }

    public void removeListener(ContainerListener containerListener) {
        if (this.listeners != null) {
            this.listeners.remove(containerListener);
        }
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= this.items.size()) {
            return ItemStack.EMPTY;
        }
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack itemStack = ContainerHelper.removeItem(this.items, i, j);
        if (!itemStack.isEmpty()) {
            this.setChanged();
        }
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack itemStack = this.items.get(i);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.items.set(i, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.items.set(i, itemStack);
        if (!itemStack.isEmpty() && itemStack.getCount() > getMaxStackSize()) {
            itemStack.setCount(getMaxStackSize());
        }
        this.setChanged();
    }

    @Override
    public int getContainerSize() {
        return this.size;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.items) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setChanged() {
        if (this.listeners != null) {
            for (ContainerListener containerListener : this.listeners) {
                containerListener.onContainerChanges(this);
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public List<ItemStack> getContents() {
        return items;
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.setChanged();
    }

//    @Override
//    public void fillStackedContents(StackedContents stackedContents) {
//        for (ItemStack itemStack : this.items) {
//            stackedContents.accountStack(itemStack);
//        }
//    }


    @Override
    public IAssociatedContainer getAssociatedContainer() {
        return storage;
    }

    @Override
    public String toString() {
        return this.items.stream().filter(itemStack -> !itemStack.isEmpty()).toList().toString();
    }

//    public void fromTag(ListTag listTag) {
//        this.clearContent();
//        for (int i = 0; i < listTag.size(); ++i) {
//            ItemStack itemStack = ItemStack.of(listTag.getCompound(i));
//            if (itemStack.isEmpty()) continue;
//            this.addItem(itemStack);
//        }
//    }
//
//    public ListTag createTag() {
//        ListTag listTag = new ListTag();
//        for (int i = 0; i < this.getContainerSize(); ++i) {
//            ItemStack itemStack = this.getItem(i);
//            if (itemStack.isEmpty()) continue;
//            listTag.add(itemStack.save(new CompoundTag()));
//        }
//        return listTag;
//    }
}

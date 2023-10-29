package net.cocoonmc.core.utils;

import net.cocoonmc.core.item.ItemStack;

import java.util.List;

public class ContainerHelper {

    public static ItemStack removeItem(List<ItemStack> list, int i, int j) {
        if (i < 0 || i >= list.size() || list.get(i).isEmpty() || j <= 0) {
            return ItemStack.EMPTY;
        }
        return list.get(i).split(j);
    }

}

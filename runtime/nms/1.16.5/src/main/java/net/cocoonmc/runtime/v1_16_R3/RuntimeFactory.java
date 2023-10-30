package net.cocoonmc.runtime.v1_16_R3;

import net.cocoonmc.runtime.IBlockFactory;
import net.cocoonmc.runtime.IItemFactory;
import net.cocoonmc.runtime.IMenuFactory;
import net.cocoonmc.runtime.IRuntime;
import net.cocoonmc.runtime.ITagFactory;

@SuppressWarnings("unused")
public class RuntimeFactory implements IRuntime {

    public static final TagFactory TAG = new TagFactory();
    public static final ItemFactory ITEM = new ItemFactory();
    public static final BlockFactory BLOCK = new BlockFactory();
    public static final MenuFactory MENU = new MenuFactory();

    @Override
    public ITagFactory getTag() {
        return TAG;
    }

    @Override
    public IItemFactory getItem() {
        return ITEM;
    }

    @Override
    public IBlockFactory getBlock() {
        return BLOCK;
    }

    @Override
    public IMenuFactory getMenu() {
        return MENU;
    }
}

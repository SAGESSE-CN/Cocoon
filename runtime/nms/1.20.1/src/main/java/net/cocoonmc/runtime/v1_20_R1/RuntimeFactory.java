package net.cocoonmc.runtime.v1_20_R1;

import net.cocoonmc.runtime.IItemFactory;
import net.cocoonmc.runtime.IMenuFactory;
import net.cocoonmc.runtime.INetworkFactory;
import net.cocoonmc.runtime.IRuntime;
import net.cocoonmc.runtime.ITagFactory;

@SuppressWarnings("unused")
public class RuntimeFactory implements IRuntime {

    public static final TagFactory TAG = new TagFactory();
    public static final ItemFactory ITEM = new ItemFactory();
    public static final MenuFactory MENU = new MenuFactory();
    public static final NetworkFactory NETWORK = new NetworkFactory();

    @Override
    public ITagFactory getTag() {
        return TAG;
    }

    @Override
    public IItemFactory getItem() {
        return ITEM;
    }

    @Override
    public IMenuFactory getMenu() {
        return MENU;
    }

    @Override
    public INetworkFactory getNetwork() {
        return NETWORK;
    }
}

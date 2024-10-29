package net.cocoonmc.runtime.v1_18_R2;

import net.cocoonmc.runtime.IBlockFactory;
import net.cocoonmc.runtime.ICodecFactory;
import net.cocoonmc.runtime.IItemFactory;
import net.cocoonmc.runtime.IMenuFactory;
import net.cocoonmc.runtime.INetworkFactory;
import net.cocoonmc.runtime.IRuntime;
import net.cocoonmc.runtime.ITagFactory;

@SuppressWarnings("unused")
public class RuntimeFactory implements IRuntime {

    public static final TagFactory TAG = new TagFactory();
    public static final ItemFactory ITEM = new ItemFactory();
    public static final BlockFactory BLOCK = new BlockFactory();
    public static final MenuFactory MENU = new MenuFactory();
    public static final NetworkFactory NETWORK = new NetworkFactory();
    public static final CodecFactory CODEC = new CodecFactory();

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

    @Override
    public INetworkFactory getNetwork() {
        return NETWORK;
    }

    @Override
    public ICodecFactory getCodec() {
        return CODEC;
    }
}

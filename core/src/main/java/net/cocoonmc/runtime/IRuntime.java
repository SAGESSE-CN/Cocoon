package net.cocoonmc.runtime;

public interface IRuntime {

    ITagFactory getTag();

    IItemFactory getItem();

    IBlockFactory getBlock();

    IMenuFactory getMenu();
}

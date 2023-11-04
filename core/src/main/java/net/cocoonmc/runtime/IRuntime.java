package net.cocoonmc.runtime;

public interface IRuntime {

    ITagFactory getTag();

    IItemFactory getItem();

    IMenuFactory getMenu();

    INetworkFactory getNetwork();
}

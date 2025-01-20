package net.cocoonmc.runtime;

public interface IRuntime {

    ITagFactory getTag();

    IItemFactory getItem();

    IBlockFactory getBlock();

    IMenuFactory getMenu();

    INetworkFactory getNetwork();

    ICodecFactory getCodec();
}

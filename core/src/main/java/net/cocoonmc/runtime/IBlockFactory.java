package net.cocoonmc.runtime;

import net.cocoonmc.core.item.context.BlockPlaceContext;
import net.cocoonmc.runtime.impl.BlockPlaceContextAccessor;

public interface IBlockFactory {

    BlockPlaceContextAccessor convertTo(BlockPlaceContext context);
}

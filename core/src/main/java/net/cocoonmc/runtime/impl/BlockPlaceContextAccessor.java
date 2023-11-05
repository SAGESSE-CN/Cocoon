package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.world.Level;

public interface BlockPlaceContextAccessor {

    boolean canBeReplaced(Level level, BlockPos pos);
}

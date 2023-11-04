package net.cocoonmc.core.block;

import net.cocoonmc.core.BlockPos;

import javax.annotation.Nullable;

public interface BlockEntitySupplier {

    @Nullable
    BlockEntity newBlockEntity(BlockPos pos, BlockState state);
}

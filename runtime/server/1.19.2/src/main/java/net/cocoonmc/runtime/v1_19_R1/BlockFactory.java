package net.cocoonmc.runtime.v1_19_R1;

import net.cocoonmc.runtime.IBlockFactory;
import net.cocoonmc.runtime.impl.BlockPlaceContextAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;

public class BlockFactory extends TransformFactory implements IBlockFactory {

    @Override
    public BlockPlaceContextAccessor convertTo(net.cocoonmc.core.item.context.BlockPlaceContext context) {
        BlockPlaceContext context1 = new BlockPlaceContext(convertToVanilla(context));
        return new BlockPlaceContextAccessor() {
            @Override
            public boolean canBeReplaced(net.cocoonmc.core.world.Level level, net.cocoonmc.core.BlockPos pos) {
                ServerLevel level1 = convertToVanilla(level);
                BlockPos pos1 = convertToVanilla(pos);
                return level1.getBlockState(pos1).canBeReplaced((context1));
            }
        };
    }
}

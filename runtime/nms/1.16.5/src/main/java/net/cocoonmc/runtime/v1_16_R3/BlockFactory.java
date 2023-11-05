package net.cocoonmc.runtime.v1_16_R3;

import net.cocoonmc.runtime.IBlockFactory;
import net.cocoonmc.runtime.impl.BlockPlaceContextAccessor;
import net.minecraft.server.v1_16_R3.BlockActionContext;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.WorldServer;

public class BlockFactory extends TransformFactory implements IBlockFactory {

    @Override
    public BlockPlaceContextAccessor convertTo(net.cocoonmc.core.item.context.BlockPlaceContext context) {
        BlockActionContext context1 = new BlockActionContext(convertToVanilla(context));
        return new BlockPlaceContextAccessor() {
            @Override
            public boolean canBeReplaced(net.cocoonmc.core.world.Level level, net.cocoonmc.core.BlockPos pos) {
                WorldServer level1 = convertToVanilla(level);
                BlockPosition pos1 = convertToVanilla(pos);
                return level1.getType(pos1).a((context1));
            }
        };
    }
}

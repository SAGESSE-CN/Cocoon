package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.world.Level;
import org.jetbrains.annotations.Nullable;

public class NeighborUpdater {

    public static final Direction[] UPDATE_ORDER = new Direction[]{Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH};

    public void updateNeighborsAtExceptFromFacing(Level level, BlockPos sourcePos, Block sourceBlock, @Nullable Direction arg3) {
        for (Direction direction : UPDATE_ORDER) {
            if (direction == arg3) {
                continue;
            }
            this.neighborChanged(level, sourcePos.relative(direction), sourcePos, sourceBlock);
        }
//       org.bukkit.block.Block test = level.asBukkit().getBlockAt(sourcePos.asBukkit());
//        Logs.debug("{}", test);
        //level.hasNeighborSignal();
    }

    public void neighborChanged(Level level, BlockPos pos, BlockPos sourcePos, Block sourceBlock) {
        BlockState state = level.getBlockState(pos);
        if (state != null) {
            state.onNeighborChanged(level, pos, sourcePos, sourceBlock, false);
            //Logs.debug("{} ~> {}", pos, state);
        }
    }
}

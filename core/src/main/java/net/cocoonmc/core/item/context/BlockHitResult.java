package net.cocoonmc.core.item.context;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.math.Vector3f;

public class BlockHitResult extends HitResult {

    private final Direction direction;
    private final BlockPos blockPos;
    private final boolean miss;
    private final boolean inside;

    public static BlockHitResult miss(Vector3f location, Direction dir, BlockPos pos) {
        return new BlockHitResult(true, location, dir, pos, false);
    }

    public static BlockHitResult hitting(Vector3f location, Direction dir, BlockPos pos, boolean inside) {
        return new BlockHitResult(false, location, dir, pos, inside);
    }

    public BlockHitResult(boolean miss, Vector3f location, Direction dir, BlockPos pos, boolean inside) {
        super(location);
        this.miss = miss;
        this.direction = dir;
        this.blockPos = pos;
        this.inside = inside;
    }

    public BlockHitResult withDirection(Direction dir) {
        return new BlockHitResult(miss, location, dir, blockPos, inside);
    }

    public BlockHitResult withPosition(BlockPos pos) {
        return new BlockHitResult(miss, location, direction, pos, inside);
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public Type getType() {
        return miss ? Type.MISS : Type.BLOCK;
    }

    public boolean isInside() {
        return inside;
    }
}

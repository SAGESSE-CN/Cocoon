package net.cocoonmc.core.math;

import net.cocoonmc.core.BlockPos;

public class CollissionBox {

    private final BlockPos pos;
    private final VoxelShape shape;
    private final VoxelShape delegate;

    public CollissionBox(BlockPos pos, VoxelShape shape) {
        this.pos = pos;
        this.shape = shape;
        this.delegate = new VoxelShape(pos.getX(), pos.getY(), pos.getZ(), 1, 1, 1);
    }

    public boolean contains(double x, double y, double z) {
        return delegate.contains(x, y, z);
    }

    public BlockPos getPos() {
        return pos;
    }

    public VoxelShape getShape() {
        return shape;
    }

    public VoxelShape getDelegate() {
        return delegate;
    }
}

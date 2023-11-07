package net.cocoonmc.core.math;

import net.cocoonmc.core.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Cursor3D implements Iterable<BlockPos> {

    private final int originX;
    private final int originY;
    private final int originZ;
    private final int width;
    private final int height;
    private final int depth;
    private final int end;

    public Cursor3D(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.originX = minX;
        this.originY = minY;
        this.originZ = minZ;
        this.width = maxX - minX + 1;
        this.height = maxY - minY + 1;
        this.depth = maxZ - minZ + 1;
        this.end = this.width * this.height * this.depth;
    }

    @NotNull
    @Override
    public Iterator<BlockPos> iterator() {
        return new Iterator<BlockPos>() {
            int index = 0;
            int x = 0;
            int y = 0;
            int z = 0;
            BlockPos.Mutable pos = new BlockPos.Mutable(0, 0, 0);

            @Override
            public boolean hasNext() {
                return index < end;
            }

            @Override
            public BlockPos next() {
                x = index % width;
                int i = index / width;
                y = i % height;
                z = i / height;
                ++index;
                pos.set(originX + x, originY + y, originZ + z);
                return pos;
            }
        };
    }
}


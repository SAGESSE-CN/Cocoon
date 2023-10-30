package net.cocoonmc.core.item.context;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;
import org.jetbrains.annotations.Nullable;

public class BlockPlaceContext extends UseOnContext {

    protected final BlockPos relativePos;
    protected boolean replaceClicked;

    public BlockPlaceContext(UseOnContext context) {
        this(context.getLevel(), context.getPlayer(), context.getHand(), context.getItemInHand(), context.getHitResult());
    }

    public BlockPlaceContext(Level level, @Nullable Player player, InteractionHand hand, ItemStack itemStack, BlockHitResult hitResult) {
        super(level, player, hand, itemStack, hitResult);
        this.replaceClicked = true;
        this.relativePos = hitResult.getBlockPos().relative(hitResult.getDirection());
        this.replaceClicked = canBeReplaced(hitResult.getBlockPos());
    }

    @Override
    public BlockPos getClickedPos() {
        if (replaceClicked) {
            return super.getClickedPos();
        }
        return relativePos;
    }

    public boolean canPlace() {
        return replaceClicked || canBeReplaced(relativePos);
    }

    public boolean replacingClickedOnBlock() {
        return replaceClicked;
    }

    public Direction getNearestLookingDirection() {
        return Direction.orderedByNearest(player)[0];
    }

    public Direction getNearestLookingVerticalDirection() {
        return Direction.getFacingAxis(this.getPlayer(), Direction.Axis.Y);
    }

    public Direction[] getNearestLookingDirections() {
        Direction[] directions = Direction.orderedByNearest(player);
        if (replaceClicked) {
            return directions;
        }
        int i = 0;
        Direction direction = getClickedFace();
        for (i = 0; i < directions.length && directions[i] != direction.getOpposite(); ++i) {
        }
        if (i > 0) {
            System.arraycopy(directions, 0, directions, 1, i);
            directions[0] = direction.getOpposite();
        }
        return directions;
    }

    private boolean canBeReplaced(BlockPos pos) {
        return level.asBukkit().getBlockAt(pos.getX(), pos.getY(), pos.getZ()).isEmpty();
    }
}

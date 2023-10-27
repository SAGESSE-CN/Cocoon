package net.cocoonmc.core.item.context;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.world.InteractionHand;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class UseOnContext {

    @Nullable
    protected final Player player;
    protected final InteractionHand hand;
    protected final BlockHitResult hitResult;
    protected final World world;
    protected final ItemStack itemStack;

    public UseOnContext(World world, @Nullable Player player, InteractionHand hand, ItemStack itemStack, BlockHitResult hitResult) {
        this.player = player;
        this.hand = hand;
        this.hitResult = hitResult;
        this.itemStack = itemStack;
        this.world = world;
    }

    public BlockHitResult getHitResult() {
        return hitResult;
    }

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public ItemStack getItemInHand() {
        return itemStack;
    }

    public InteractionHand getHand() {
        return hand;
    }

    public BlockPos getClickedPos() {
        return hitResult.getBlockPos();
    }

    public Direction getClickedFace() {
        return hitResult.getDirection();
    }

    public Vector3f getClickLocation() {
        return hitResult.getLocation();
    }

    public boolean isInside() {
        return hitResult.isInside();
    }

    public Direction getHorizontalDirection() {
        if (player != null) {
            return Direction.of(player.getFacing());
        }
        return Direction.NORTH;
    }

//    public boolean isSecondaryUseActive() {
//        return this.player != null && this.player.isSecondaryUseActive();
//    }
}


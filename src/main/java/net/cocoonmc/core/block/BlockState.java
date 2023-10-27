package net.cocoonmc.core.block;

import com.google.common.collect.ImmutableMap;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.state.StateHolder;
import net.cocoonmc.core.block.state.properties.Property;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BlockState extends StateHolder<Block, BlockState> {

    public BlockState(Block block, ImmutableMap<Property<?>, Comparable<?>> values) {
        super(block, values);
    }

    public InteractionResult use(World world, BlockPos blockPos, Player player, InteractionHand hand) {
        return getBlock().use(this, world, blockPos, player, hand);
    }

    public InteractionResult attack(World world, BlockPos blockPos, Player player) {
        return getBlock().attack(this, world, blockPos, player);
    }

    public boolean canSurvive(World world, BlockPos blockPos) {
        return getBlock().canSurvive(this, world, blockPos);
    }


    public Block getBlock() {
        return owner;
    }

}

package net.cocoonmc.core.block;

import com.google.common.collect.ImmutableMap;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.state.StateHolder;
import net.cocoonmc.core.block.state.properties.Property;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;

public class BlockState extends StateHolder<Block, BlockState> {

    public BlockState(Block block, ImmutableMap<Property<?>, Comparable<?>> values) {
        super(block, values);
    }

    public InteractionResult use(Level level, BlockPos blockPos, Player player, InteractionHand hand) {
        return getBlock().use(this, level, blockPos, player, hand);
    }

    public InteractionResult attack(Level level, BlockPos blockPos, Player player) {
        return getBlock().attack(this, level, blockPos, player);
    }

    public boolean canSurvive(Level level, BlockPos blockPos) {
        return getBlock().canSurvive(this, level, blockPos);
    }


    public Block getBlock() {
        return owner;
    }

}

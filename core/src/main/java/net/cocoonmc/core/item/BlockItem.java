package net.cocoonmc.core.item;

import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.item.context.BlockPlaceContext;
import net.cocoonmc.core.item.context.UseOnContext;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.world.InteractionResult;
import org.jetbrains.annotations.Nullable;

public class BlockItem extends Item {

    private final Block block;

    public BlockItem(Block block, Properties properties) {
        super(properties);
        this.block = block;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return place(new BlockPlaceContext(context));
    }

    public InteractionResult place(BlockPlaceContext context) {
        if (!context.canPlace()) {
            return InteractionResult.FAIL;
        }
        BlockPlaceContext blockPlaceContext = this.updatePlacementContext(context);
        if (blockPlaceContext == null) {
            return InteractionResult.FAIL;
        }
        BlockState blockState = getPlacementState(blockPlaceContext);
        if (blockState == null) {
            return InteractionResult.FAIL;
        }
        return BukkitHelper.placeBlock(block.getDelegate(), blockState, null, blockPlaceContext);
    }

    protected BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        return context;
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext arg) {
        return getBlock().getStateForPlacement(arg);
    }

    public Block getBlock() {
        return block;
    }

//    public static ItemStack getHeadStack(Block block, @Nullable BlockState state, @Nullable CompoundTag tag) {
//        ItemStack itemStack = new ItemStack(Items.PLAYER_HEAD);
//        CompoundTag owner = CompoundTag.newInstance();
//        CompoundTag properties = CompoundTag.newInstance();
//        ListTag textures = ListTag.newInstance();
//        CompoundTag texture = CompoundTag.newInstance();
//        texture.putString("Value", BukkitHelper.getBlockDataTexture(block, state, tag));
//        textures.add(texture);
//        properties.put("textures", textures);
//        owner.put("Properties", properties);
//        owner.putUUID("Id", UUID.randomUUID());
//        itemStack.addTagElement("SkullOwner", owner);
//        return itemStack;
//    }
}

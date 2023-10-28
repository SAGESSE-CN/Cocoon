package net.cocoonmc.core.item;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.item.context.BlockPlaceContext;
import net.cocoonmc.core.item.context.UseOnContext;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.ListTag;
import net.cocoonmc.core.nbt.NbtIO;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.runtime.impl.Constants;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;
import java.util.UUID;

public class BlockItem extends Item {

    private final Block block;

    public BlockItem(Block block, Properties properties) {
        super(properties);
        this.block = block;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
//        InteractionResult interactionresult = this.place(new BlockPlaceContext(arg));
//        if (!interactionresult.consumesAction() && this.isEdible()) {
//            InteractionResult interactionresult1 = this.use(arg.getLevel(), arg.getPlayer(), arg.getHand()).getResult();
//            return interactionresult1 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionresult1;
//        }
//        return interactionresult;
        return place(new BlockPlaceContext(context));
    }

    public InteractionResult place(BlockPlaceContext context) {
//        if (!this.getBlock().isEnabled(arg.getLevel().enabledFeatures())) {
//            return InteractionResult.FAIL;
//        }
//        if (!context.canPlace()) {
//            return InteractionResult.FAIL;
//        }
        BlockPlaceContext blockPlaceContext = this.updatePlacementContext(context);
        if (blockPlaceContext == null) {
            return InteractionResult.FAIL;
        }
        BlockState blockState = getPlacementState(context);
        if (blockState == null) {
            return InteractionResult.FAIL;
        }
        ItemStack itemStack = getHeadStack(block, blockState, null);
        return Cocoon.API.ITEM.useOn(itemStack, context);

//        if (!this.placeBlock(blockplacecontext, blockstate)) {
//            return InteractionResult.FAIL;
//        }

//        BlockPos blockpos = blockplacecontext.getClickedPos();
//        Level level = blockplacecontext.getLevel();
//        Player player = blockplacecontext.getPlayer();
//        ItemStack itemstack = blockplacecontext.getItemInHand();
//        BlockState blockstate1 = level.getBlockState(blockpos);
//        if (blockstate1.is(blockstate.getBlock())) {
//            blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
//            this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
//            blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
//            if (player instanceof ServerPlayer) {
//                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
//            }
//        }
//        SoundType soundtype = blockstate1.getSoundType(level, blockpos, arg.getPlayer());
//        level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, arg.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
//        level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
//        if (player == null || !player.getAbilities().instabuild) {
//            itemstack.shrink(1);
//        }
//        return InteractionResult.sidedSuccess(level.isClientSide);


//        BukkitUtils.setBlockAndUpdate(context, block.getKey().toString(), blockState, null);

//        Block target = context.getClickedBlock().getRelative(context.getClickedBlockFace());
//        if (!target.getType().isAir()) {
//            // target already exists a block.
//            return InteractionResult.FAIL;
//        }
//        BukkitUtils.setBlockAndUpdate(target, blockId, state, null);


//        CompoundTag bs = new CompoundTag();
//        bs.putString("lit", "true");
//        bs.putString("facing", "south");
//        bs.putString("face", "floor");
//
//        ListTag<CompoundTag> items = new ListTag<>(CompoundTag.class);
//        CompoundTag item = new SkinDescriptor("db:QueQGz91xn", SkinTypes.OUTFIT).asItemStack().save(new CompoundTag());
//        item.putByte("Slot", (byte) 0);
//        items.add(item);
//
//        CompoundTag bt = new CompoundTag();
//        bt.putByte("Powered", (byte) 1);
//        bt.put("Items", items);
//
//        BukkitUtils.setRedirectedBlockAndUpdate(context.getClickedBlock(), "armourers_workshop:hologram-projector", bs, bt);
    }

    protected BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        return context;
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext arg) {
        BlockState blockState = getBlock().getStateForPlacement(arg);
//        return blockState != null && this.canPlace(arg, blockState) ? blockState : null;
        return blockState;
    }

    public Block getBlock() {
        return block;
    }


    public static ItemStack getHeadStack(Block block, @Nullable BlockState state, @Nullable CompoundTag tag) {
        ItemStack itemStack = new ItemStack(Items.PLAYER_HEAD);
        CompoundTag owner = CompoundTag.newInstance();
        CompoundTag properties = CompoundTag.newInstance();
        ListTag textures = ListTag.newInstance();
        CompoundTag texture = CompoundTag.newInstance();
        texture.putString("Value", getHeadTexture(block, state, tag));
        textures.add(texture);
        properties.put("textures", textures);
        owner.put("Properties", properties);
        owner.putUUID("Id", UUID.randomUUID());
        itemStack.addTagElement("SkullOwner", owner);
        return itemStack;
    }

    public static String getHeadTexture(Block block, @Nullable BlockState state, @Nullable CompoundTag tag) {
        CompoundTag blockTag = CompoundTag.newInstance();
        blockTag.putString(Constants.BLOCK_REDIRECTED_ID_KEY, block.getKey().toString());
        if (state != null) {
            CompoundTag stateTag = state.serialize();
            if (stateTag.size() != 0) {
                blockTag.put(Constants.BLOCK_REDIRECTED_STATE_KEY, stateTag);
            }
        }
        if (tag != null) {
            blockTag.put(Constants.BLOCK_REDIRECTED_TAG_KEY, tag);
        }
        try {
            String value = NbtIO.toString(blockTag);
            return new String(Base64.getEncoder().encode(String.format(Constants.BLOCK_REDIRECTED_DATA_FMT, value).getBytes()));
        } catch (Exception e) {
            return "";
        }
    }
}

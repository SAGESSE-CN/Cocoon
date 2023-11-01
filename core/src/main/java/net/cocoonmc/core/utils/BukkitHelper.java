package net.cocoonmc.core.utils;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.Blocks;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.NbtIO;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.runtime.impl.BlockData;
import net.cocoonmc.runtime.impl.Constants;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;
import java.util.List;

public class BukkitHelper {

    private static final Direction[] FACE_TO_DIRECTION = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN};
    private static final BlockFace[] DIRECTION_TO_FACE = {BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};


//    public static void updateBlockData(org.bukkit.block.Block block, Block newBlock, @Nullable BlockState newState, @Nullable CompoundTag newTag) {
//        Material type = block.getType();
//        if (type != Material.PLAYER_HEAD && type != Material.PLAYER_WALL_HEAD) {
//            return;
//        }
//        Skull skull = (Skull) block.getState();
//        String texture = BlockItem.getHeadTexture(newBlock, newState, newTag);
//        Cocoon.API.BLOCK.setSkullTexture(skull, texture);
//        // did changes.
//        block.getState().update();
//    }

//    @Nullable
//    public static BlockData getBlockData(org.bukkit.block.Block block) {
//        Level level = Level.of(block.getWorld());
//        BlockPos blockPos = BlockPos.of(block.getLocation());
//        Material type = block.getType();
//        if (type != Material.PLAYER_HEAD && type != Material.PLAYER_WALL_HEAD) {
//            return null;
//        }
//        // maybe is dynamic type.
//        Skull skull = (Skull) block.getState();
//        if (skull.getOwningPlayer() == null || skull.getOwningPlayer().getName() != null) {
//            return null;
//        }
//        return Cocoon.API.CACHE.computeIfAbsent(Cocoon.API.BLOCK.convertTo(skull), CacheKeys.BLOCK_DATA_KEY, it -> {
//            Level level = Level.of(block);
//            String texture = Cocoon.API.BLOCK.getSkullTexture(skull);
//            return getBlockDataFromTexture(level, BlockPos.of(block.getLocation()), texture);
//        });
//        return Cocoon.API.BLOCK.getBlockData(level, blockPos);
//    }

    @Nullable
    public static BlockData getBlockDataFromTexture(Level level, BlockPos pos, String texture) {
        // fast check
        if (texture == null || texture.isEmpty() || !texture.startsWith(Constants.BLOCK_REDIRECTED_DATA_PREFIX)) {
            return null;
        }
        try {
            String[] parts = Constants.BLOCK_REDIRECTED_DATA_FMT.split("%s");
            String tag = new String(Base64.getDecoder().decode(texture));
            if (tag.startsWith(parts[0]) && tag.endsWith(parts[1])) {
                String tag2 = tag.substring(parts[0].length(), tag.length() - parts[1].length());
                return getBlockDataFromTag(level, pos, NbtIO.fromString(tag2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static BlockData getBlockDataFromTag(Level level, BlockPos pos, CompoundTag tag) {
        Block block = Blocks.byId(tag.getString(Constants.BLOCK_REDIRECTED_ID_KEY));
        if (block == null || block.asMaterial() != null) {
            return null;
        }
        CompoundTag blockTag = null;
        BlockState blockState = block.defaultBlockState().deserialize(tag.getCompound(Constants.BLOCK_REDIRECTED_STATE_KEY));
        if (tag.contains(Constants.BLOCK_REDIRECTED_TAG_KEY, 10)) {
            blockTag = tag.getCompound(Constants.BLOCK_REDIRECTED_TAG_KEY);
        }
        BlockEntity blockEntity = block.createBlockEntity(level, pos, blockState);
        BlockData blockData = new BlockData(level, pos, block, blockState, blockEntity);
        blockData.setBlockTag(blockTag);
        return blockData;
    }

    public static String getBlockDataTexture(Block block, @Nullable BlockState state, @Nullable CompoundTag tag) {
        CompoundTag blockTag = CompoundTag.newInstance();
        blockTag.putString(Constants.BLOCK_REDIRECTED_ID_KEY, block.getRegistryName().toString());
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
            System.out.printf("%s", value);
            return new String(Base64.getEncoder().encode(String.format(Constants.BLOCK_REDIRECTED_DATA_FMT, value).getBytes()));
        } catch (Exception e) {
            return "";
        }
    }

    public static void replaceDrops(List<ItemStack> dropItems, Block block) {
        Item item = block.asItem();
        for (int i = 0; i < dropItems.size(); ++i) {
            ItemStack itemStack = dropItems.get(i);
            if (!itemStack.is(Items.PLAYER_HEAD)) {
                continue;
            }
            if (item == Items.AIR) {
                dropItems.remove(i);
                i -= 1;
                continue;
            }
            dropItems.set(i, new ItemStack(item, itemStack.getCount()));
        }
    }

    public static Direction convertToCocoon(org.bukkit.block.BlockFace face) {
        return FACE_TO_DIRECTION[face.ordinal()];
    }

    public static org.bukkit.block.BlockFace convertToBukkit(Direction direction) {
        return DIRECTION_TO_FACE[direction.ordinal()];
    }


    public static boolean isRedirectedBlock(org.bukkit.block.Block block) {
        Level level = Level.of(block.getWorld());
        BlockPos blockPos = BlockPos.of(block.getLocation());
        return Cocoon.API.BLOCK.getBlockData(level, blockPos) != null;
    }

    public static boolean isRedirectedItem(org.bukkit.inventory.ItemStack itemStack) {
        if (itemStack != null && itemStack.hasItemMeta()) {
            return Cocoon.API.ITEM.convertTo(itemStack).getItem().asMaterial() == null;
        }
        return false;
    }
}

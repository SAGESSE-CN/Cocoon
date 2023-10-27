package net.cocoonmc.utils;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.Blocks;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.NbtIO;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.impl.BlockFactoryImpl;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Base64;
import java.util.WeakHashMap;

public class BukkitUtils {

    private static final WeakHashMap<Object, BlockEntity> STATE_TO_BLOCK_ENTITY = new WeakHashMap<>();

    public static final String ITEM_REDIRECTED_KEY = "__redirected_id__";

    public static final Direction[] FACE_TO_DIRECTION = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN};
    public static final BlockFace[] DIRECTION_TO_FACE = {BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};


    public static ItemStack getItemInHand(Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return ItemStack.of(player.getInventory().getItemInMainHand());
        } else {
            return ItemStack.of(player.getInventory().getItemInOffHand());
        }
    }

    public static void setItemInHand(Player player, InteractionHand hand, ItemStack itemStack) {
        if (hand == InteractionHand.MAIN_HAND) {
            player.getInventory().setItemInMainHand(itemStack.asBukkit());
        } else {
            player.getInventory().setItemInOffHand(itemStack.asBukkit());
        }
    }


    public static BlockEntity getBlockEntity(World world, BlockPos pos) {
        org.bukkit.block.Block block = world.getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        Material type = block.getType();
        if (type != Material.PLAYER_HEAD && type != Material.PLAYER_WALL_HEAD) {
            return null;
        }
        // maybe is dynamic type.
        Skull skull = (Skull) block.getState();
        if (skull.getOwningPlayer() == null || skull.getOwningPlayer().getName() != null) {
            return null;
        }
        return STATE_TO_BLOCK_ENTITY.computeIfAbsent(skull, it -> {
            String texture = BlockFactoryImpl.getSkullTexture(skull);
            return getBlockEntityFromTexture(world, pos, texture);
        });
    }

    @Nullable
    public static BlockEntity getBlockEntityFromTexture(World world, BlockPos pos, String texture) {
        // base64("{textures:{SKIN:{\"url":\"\",__redirected_block_")
        if (texture == null || texture.isEmpty() || !texture.startsWith("e3RleHR1cmVzOntTS0lOOnsidXJsIjoiIixfX3JlZGlyZWN0ZWRfYmxvY2tfX")) {
            return null;
        }
        try {
            String header = "{textures:{SKIN:{\"url\":\"\",__redirected_block__:";
            String footer = ",__block_redirected__:\"\"}}}";
            String tag = new String(Base64.getDecoder().decode(texture));
            if (tag.startsWith(header) && tag.endsWith(footer)) {
                String tag2 = tag.substring(header.length(), tag.length() - footer.length());
                return getBlockEntityFromTag(world, pos, NbtIO.fromString(tag2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static BlockEntity getBlockEntityFromTag(World world, BlockPos pos, CompoundTag blockTag) {
        Block block = Blocks.byId(blockTag.getString("id"));
        if (block.asMaterial() != null) {
            return null;
        }
        BlockState blockState = block.defaultBlockState().deserialize(blockTag.getCompound("state"));
        CompoundTag tag = null;
        if (blockTag.contains("tag", 10)) {
            tag = blockTag.getCompound("tag");
        }
        return block.createBlockEntity(world, pos, blockState, tag);
    }


    public static CompoundTag setItemId(@Nullable CompoundTag tag, String sourceId, String wrapperId) {
        if (tag == null) {
            tag = CompoundTag.newInstance();
        }
        tag.putString(ITEM_REDIRECTED_KEY, sourceId + "/" + wrapperId);
        return tag;
    }

    @Nullable
    public static String getReadId(String wrapperId, @Nullable CompoundTag tag) {
        if (tag != null && tag.contains(ITEM_REDIRECTED_KEY, 8)) {
            String sourceId = _splitId(tag.getString(ITEM_REDIRECTED_KEY), 0);
            if (sourceId != null && !sourceId.isEmpty()) {
                return sourceId;
            }
        }
        return wrapperId;
    }

    public static String getWrapperId(String sourceId, @Nullable CompoundTag tag, int maxStackSize) {
        if (sourceId.startsWith("minecraft:")) {
            return sourceId;
        }
        if (tag != null && tag.contains(ITEM_REDIRECTED_KEY, 8)) {
            String wrapperId = _splitId(tag.getString(ITEM_REDIRECTED_KEY), 1);
            if (wrapperId != null && !wrapperId.isEmpty()) {
                return wrapperId;
            }
        }
        return getWrapperIdBySize(maxStackSize);
    }

    public static String getWrapperIdBySize(int maxStackSize) {
        switch (maxStackSize) {
            case 1: {
                return Items.FLOWER_BANNER_PATTERN.getKey().toString();
            }
            case 16: {
                return Items.WHITE_BANNER.getKey().toString();
            }
            default: {
                return Items.PAPER.getKey().toString();
            }
        }
    }

    private static String _splitId(String id, int index) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        String[] ids = id.split("/");
        if (index < ids.length) {
            return ids[index];
        }
        return null;
    }


    public static ItemStack getHead(String skinURL) {
        ItemStack itemStack = new ItemStack(Items.PLAYER_HEAD);
        CompoundTag owner = CompoundTag.newInstance();

//        owner.putIntArray("id", {0, 0, 0});

//        .Properties.textures[0].Value set value "e3RleHR1cmVzOntTS0lOOnsidXJsIjoiIixfX3JlZGlyZWN0ZWRfYmxvY2tfXzp7aWQ6Im1pbmVjcmFmdDpmdXJuYWNlIixzdGF0ZTp7bGl0OiJ0cnVlIn19LF9fYmxvY2tfcmVkaXJlY3RlZF9fOiIifX19

        itemStack.addTagElement("SkullOwner", owner);
        return itemStack;
    }

//    public static void setSkullTextureWithURL(Skull skull, String skinURL) {
//        String texture = new String(Base64.getEncoder().encode(String.format("{textures:{SKIN:{\"url\":\"%s\"}}}", skinURL).getBytes()));
//        setSkullTexture(skull, texture);
//    }

    private static String _getSkinURL(String id, @Nullable BlockState state, @Nullable CompoundTag tag) throws IOException {
        CompoundTag blockTag = CompoundTag.newInstance();
        blockTag.putString("id", id);
        if (state != null) {
            CompoundTag stateTag = state.serialize();
            if (stateTag.size() != 0) {
                blockTag.put("state", stateTag);
            }
        }
        if (tag != null) {
            blockTag.put("tag", tag);
        }
        String value = NbtIO.toString(blockTag);
        return "\",__redirected_block__:" + value + ",__block_redirected__:\"";
    }
}

package net.cocoonmc.core.utils;

import net.cocoonmc.core.Direction;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.runtime.impl.Constants;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Nullable;

public class BukkitUtils {


    public static final Direction[] FACE_TO_DIRECTION = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN};
    public static final BlockFace[] DIRECTION_TO_FACE = {BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};


//    public static CompoundTag setItemId(@Nullable CompoundTag tag, String sourceId, String wrapperId) {
//        if (tag == null) {
//            tag = CompoundTag.newInstance();
//        }
//        tag.putString(ITEM_REDIRECTED_KEY, sourceId + "/" + wrapperId);
//        return tag;
//    }

    @Nullable
    public static String getReadId(String wrapperId, @Nullable CompoundTag tag) {
        if (tag != null && tag.contains(Constants.ITEM_REDIRECTED_KEY, 8)) {
            String sourceId = _splitId(tag.getString(Constants.ITEM_REDIRECTED_KEY), 0);
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
        if (tag != null && tag.contains(Constants.ITEM_REDIRECTED_KEY, 8)) {
            String wrapperId = _splitId(tag.getString(Constants.ITEM_REDIRECTED_KEY), 1);
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


//    public static ItemStack getHead(String skinURL) {
//        ItemStack itemStack = new ItemStack(Items.PLAYER_HEAD);
//        CompoundTag owner = CompoundTag.newInstance();
//
////        owner.putIntArray("id", {0, 0, 0});
//
////        .Properties.textures[0].Value set value "e3RleHR1cmVzOntTS0lOOnsidXJsIjoiIixfX3JlZGlyZWN0ZWRfYmxvY2tfXzp7aWQ6Im1pbmVjcmFmdDpmdXJuYWNlIixzdGF0ZTp7bGl0OiJ0cnVlIn19LF9fYmxvY2tfcmVkaXJlY3RlZF9fOiIifX19
//
//        itemStack.addTagElement("SkullOwner", owner);
//        return itemStack;
//    }
//
//    public static void setSkullTextureWithURL(Skull skull, String skinURL) {
//        String texture = new String(Base64.getEncoder().encode(String.format("{textures:{SKIN:{\"url\":\"%s\"}}}", skinURL).getBytes()));
//        setSkullTexture(skull, texture);
//    }
//
//    private static String _getSkinURL(String id, @Nullable BlockState state, @Nullable CompoundTag tag) throws IOException {
//        CompoundTag blockTag = CompoundTag.newInstance();
//        blockTag.putString("id", id);
//        if (state != null) {
//            CompoundTag stateTag = state.serialize();
//            if (stateTag.size() != 0) {
//                blockTag.put("state", stateTag);
//            }
//        }
//        if (tag != null) {
//            blockTag.put("tag", tag);
//        }
//        String value = NbtIO.toString(blockTag);
//        return "\",__redirected_block__:" + value + ",__block_redirected__:\"";
//    }
}

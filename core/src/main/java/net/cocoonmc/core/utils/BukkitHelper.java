package net.cocoonmc.core.utils;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.core.item.context.BlockPlaceContext;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.EntityType;
import net.cocoonmc.runtime.impl.BlockPlaceTask;
import net.cocoonmc.runtime.impl.ConstantKeys;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class BukkitHelper {

    public static final UUID INVALID_UUID = new UUID(0, 0);

    private static final ReflectHelper.Member<Vector> GET_CLICKED_LOCATION = ReflectHelper.getMemberField(PlayerInteractEvent.class, "clickedPosistion");

    private static final Direction[] FACE_TO_DIRECTION = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN};
    private static final BlockFace[] DIRECTION_TO_FACE = {BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};


    public static InteractionResult placeBlock(Block wrapper, BlockState blockState, @Nullable CompoundTag entityTag, BlockPlaceContext context) {
        ItemStack wrapperStack = new ItemStack(wrapper.asItem());
        BlockPlaceTask.push(wrapper, blockState, entityTag, context);
        InteractionResult result = Cocoon.API.ITEM.useOn(context.getPlayer(), wrapperStack, context);
        BlockPlaceTask.pop(wrapper);
        if (wrapperStack.isEmpty()) {
            context.getItemInHand().shrink(1);
        }
        return result;
    }


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

    public static NamespacedKey getKey(String namespace, String path) {
        return new NamespacedKey(namespace, path);
    }


    public static void runTask(Runnable task) {
        Bukkit.getScheduler().runTask(Cocoon.getPlugin(), task);
    }

//    @Nullable
//    public static BlockData getBlockDataFromTexture(Level level, BlockPos pos, String texture) {
//        // fast check
//        if (texture == null || texture.isEmpty() || !texture.startsWith(Constants.BLOCK_REDIRECTED_DATA_PREFIX)) {
//            return null;
//        }
//        try {
//            String[] parts = Constants.BLOCK_REDIRECTED_DATA_FMT.split("%s");
//            String tag = new String(Base64.getDecoder().decode(texture));
//            if (tag.startsWith(parts[0]) && tag.endsWith(parts[1])) {
//                String tag2 = tag.substring(parts[0].length(), tag.length() - parts[1].length());
//                return getBlockDataFromTag(level, pos, CompoundTag.parseTag(tag2));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Nullable
//    public static BlockData getBlockDataFromTag(Level level, BlockPos pos, CompoundTag tag) {
//        Block block = Blocks.byId(tag.getString(Constants.BLOCK_REDIRECTED_ID_KEY));
//        if (block == null || block.asBukkit() != null) {
//            return null;
//        }
//        CompoundTag blockTag = null;
//        BlockState blockState = block.defaultBlockState().deserialize(tag.getCompound(Constants.BLOCK_REDIRECTED_STATE_KEY));
//        if (tag.contains(Constants.BLOCK_REDIRECTED_TAG_KEY, 10)) {
//            blockTag = tag.getCompound(Constants.BLOCK_REDIRECTED_TAG_KEY);
//        }
////        BlockEntity blockEntity = block.createBlockEntity(level, pos, blockState);
////        BlockData blockData = new BlockData(level, pos, blockState, blockEntity);
////        blockData.setBlockTag(blockTag);
////        return blockData;
//        return null;
//    }
//
//    public static CompoundTag getBlockDataTag(Block block, @Nullable BlockState state, @Nullable CompoundTag entityTag) {
//        CompoundTag blockTag = CompoundTag.newInstance();
//        blockTag.putString(Constants.BLOCK_REDIRECTED_ID_KEY, block.getRegistryName().toString());
//        if (state != null) {
//            CompoundTag stateTag = state.serialize();
//            if (stateTag.size() != 0) {
//                blockTag.put(Constants.BLOCK_REDIRECTED_STATE_KEY, stateTag);
//            }
//        }
//        if (entityTag != null) {
//            blockTag.put(Constants.BLOCK_REDIRECTED_TAG_KEY, entityTag);
//        }
//        return blockTag;
//    }
//
//    public static String getBlockDataTexture(Block block, @Nullable BlockState state, @Nullable CompoundTag tag) {
//        CompoundTag blockTag = getBlockDataTag(block, state, tag);
//        try {
//            String value = blockTag.toString();
//            System.out.printf("%s", value);
//            return new String(Base64.getEncoder().encode(String.format(Constants.BLOCK_REDIRECTED_DATA_FMT, value).getBytes()));
//        } catch (Exception e) {
//            return "";
//        }
//    }

    public static boolean hasCustomEntityType(org.bukkit.entity.Entity entity) {
        return entity.getPersistentDataContainer().has(ConstantKeys.ENTITY_TYPE_KEY, PersistentDataType.STRING);
    }

    public static void setCustomEntityType(org.bukkit.entity.Entity entity, EntityType<?> entityType) {
        entity.getPersistentDataContainer().set(ConstantKeys.ENTITY_TYPE_KEY, PersistentDataType.STRING, entityType.getRegistryName().toString());
    }

    public static EntityType<?> getCustomEntityType(org.bukkit.entity.Entity entity) {
        String value = entity.getPersistentDataContainer().get(ConstantKeys.ENTITY_TYPE_KEY, PersistentDataType.STRING);
        if (value != null) {
            ResourceLocation rl = new ResourceLocation(value);
            return EntityType.byKey(rl);
        }
        return null;
    }

    public static void replaceDrops(List<ItemStack> dropItems, Block block) {
        Item oldItem = block.getDelegate().asItem();
        Item newItem = block.asItem();
        for (int i = 0; i < dropItems.size(); ++i) {
            ItemStack itemStack = dropItems.get(i);
            if (!itemStack.is(oldItem)) {
                continue;
            }
            if (newItem == Items.AIR) {
                dropItems.remove(i);
                i -= 1;
                continue;
            }
            dropItems.set(i, new ItemStack(newItem, itemStack.getCount()));
        }
    }

    public static void dropItems(List<ItemStack> itemStacks, org.bukkit.block.Block block) {
        dropItems(itemStacks, block.getWorld(), block.getLocation());
    }

    public static void dropItems(List<ItemStack> itemStacks, org.bukkit.World world, org.bukkit.Location location) {
        BukkitHelper.runTask(() -> itemStacks.forEach(it -> {
            if (!it.isEmpty()) {
                world.dropItemNaturally(location, it.asBukkit());
            }
        }));
    }

    public static Direction convertToCocoon(org.bukkit.block.BlockFace face) {
        return FACE_TO_DIRECTION[face.ordinal()];
    }

    public static org.bukkit.block.BlockFace convertToBukkit(Direction direction) {
        return DIRECTION_TO_FACE[direction.ordinal()];
    }


    public static org.bukkit.Location getClickedLocation(PlayerInteractEvent event) {
        org.bukkit.block.Block block = event.getClickedBlock();
        org.bukkit.Location loc = block.getLocation();
        Vector vector = GET_CLICKED_LOCATION.get(event);
        if (vector != null) {
            return loc.clone().add(vector.getX(), vector.getY(), vector.getZ());
        }
        return loc;
    }

    public static boolean isRedirectedBlock(org.bukkit.block.Block block) {
        Level level = Level.of(block.getWorld());
        BlockPos blockPos = BlockPos.of(block);
        return level.getBlockState(blockPos) != null;
    }

    public static boolean isRedirectedItem(org.bukkit.inventory.ItemStack itemStack) {
        if (itemStack != null && itemStack.hasItemMeta()) {
            return Cocoon.API.ITEM.convertTo(itemStack).getItem().asBukkit() == null;
        }
        return false;
    }
}

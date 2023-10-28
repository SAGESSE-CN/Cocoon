package net.cocoonmc.runtime;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.Blocks;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.NbtIO;
import net.cocoonmc.runtime.impl.Caches;
import net.cocoonmc.runtime.impl.Constants;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Skull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;

public interface IBlockFactory {

    void setSkullTexture(Skull skull, String texture);

    String getSkullTexture(Skull skull);


    default BlockEntity getBlockEntity(World world, BlockPos pos) {
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
        return Caches.STATE_TO_BLOCK_ENTITY.computeIfAbsent(skull, it -> {
            String texture = getSkullTexture(skull);
            return getBlockEntityFromTexture(world, pos, texture);
        });
    }

    @Nullable
    default BlockEntity getBlockEntityFromTexture(World world, BlockPos pos, String texture) {
        // fast check
        if (texture == null || texture.isEmpty() || !texture.startsWith(Constants.BLOCK_REDIRECTED_DATA_PREFIX)) {
            return null;
        }
        try {
            String[] parts = Constants.BLOCK_REDIRECTED_DATA_FMT.split("%s");
            String tag = new String(Base64.getDecoder().decode(texture));
            if (tag.startsWith(parts[0]) && tag.endsWith(parts[1])) {
                String tag2 = tag.substring(parts[0].length(), tag.length() - parts[1].length());
                return getBlockEntityFromTag(world, pos, NbtIO.fromString(tag2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    default BlockEntity getBlockEntityFromTag(World world, BlockPos pos, CompoundTag blockTag) {
        Block block = Blocks.byId(blockTag.getString(Constants.BLOCK_REDIRECTED_ID_KEY));
        if (block == null || block.asMaterial() != null) {
            return null;
        }
        CompoundTag tag = null;
        BlockState blockState = block.defaultBlockState().deserialize(blockTag.getCompound(Constants.BLOCK_REDIRECTED_STATE_KEY));
        if (blockTag.contains(Constants.BLOCK_REDIRECTED_TAG_KEY, 10)) {
            tag = blockTag.getCompound(Constants.BLOCK_REDIRECTED_TAG_KEY);
        }
        return block.createBlockEntity(world, pos, blockState, tag);
    }
}

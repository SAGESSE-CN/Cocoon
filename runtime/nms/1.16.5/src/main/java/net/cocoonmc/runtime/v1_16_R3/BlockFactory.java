package net.cocoonmc.runtime.v1_16_R3;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.runtime.IBlockFactory;
import net.cocoonmc.runtime.impl.BlockData;
import net.cocoonmc.runtime.impl.BlockEntityAccessor;
import net.cocoonmc.runtime.impl.CacheKeys;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Blocks;
import net.minecraft.server.v1_16_R3.TileEntity;
import net.minecraft.server.v1_16_R3.TileEntitySkull;
import net.minecraft.server.v1_16_R3.World;
import net.minecraft.server.v1_16_R3.WorldServer;

import java.util.UUID;

public class BlockFactory extends TransformFactory implements IBlockFactory {

    @Override
    public void setBlockData(net.cocoonmc.core.world.Level level, net.cocoonmc.core.BlockPos blockPos, BlockData blockData) {
        WorldServer serverLevel = convertToVanilla(level);
        BlockPosition blockPos1 = convertToVanilla(blockPos);
        TileEntity blockEntity = serverLevel.getTileEntity(blockPos1);
        if (!(blockEntity instanceof TileEntitySkull)) {
            serverLevel.a(blockPos1, Blocks.PLAYER_HEAD.getBlockData(), 2, 1042);
            blockEntity = serverLevel.getTileEntity(blockPos1);
        }

        if (blockEntity instanceof TileEntitySkull) {
            blockData.setBlockEntity(convertToCocoon(blockEntity));
            Cocoon.API.CACHE.set(blockData, CacheKeys.BLOCK_DATA_KEY, blockData);
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            Property property = new Property("textures", BukkitHelper.getBlockDataTexture(blockData.getBlock(), blockData.getBlockState(), blockData.getBlockTag()));
            profile.getProperties().put("textures", property);
            ((TileEntitySkull) blockEntity).setGameProfile(profile);
        }
    }

    @Override
    public BlockData getBlockData(net.cocoonmc.core.world.Level level, net.cocoonmc.core.BlockPos blockPos) {
        WorldServer serverLevel = convertToVanilla(level);
        BlockPosition blockPos1 = convertToVanilla(blockPos);
        TileEntity blockEntity = serverLevel.getTileEntity(blockPos1);
        if (!(blockEntity instanceof TileEntitySkull)) {
            return null;
        }
        GameProfile profile = ((TileEntitySkull) blockEntity).gameProfile;
        if (profile == null || profile.getName() != null) {
            return null;
        }
        return Cocoon.API.CACHE.computeIfAbsent(blockEntity, CacheKeys.BLOCK_DATA_KEY, it -> {
            String texture = profile.getProperties().get("textures").stream().findFirst().map(Property::getValue).orElse(null);
            BlockData blockData = BukkitHelper.getBlockDataFromTexture(level, blockPos, texture);
            if (blockData != null) {
                blockData.setBlockEntity(convertToCocoon(blockEntity));
            }
            return blockData;
        });
    }


    private static BlockEntityAccessor convertToCocoon(TileEntity blockEntity) {
        return new BlockEntityAccessor() {
            @Override
            public void setChanged() {
                blockEntity.update();
            }

            @Override
            public void sendBlockUpdated(net.cocoonmc.core.BlockPos pos, BlockState oldBlockState, BlockState newBlockState, int flags) {
                World level = blockEntity.getWorld();
                if (level != null) {
                    level.notify(blockEntity.getPosition(), blockEntity.getBlock(), blockEntity.getBlock(), flags);
                }
            }
        };
    }
}

package net.cocoonmc.runtime.v1_18_R2;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.runtime.IBlockFactory;
import net.cocoonmc.runtime.impl.BlockData;
import net.cocoonmc.runtime.impl.BlockEntityAccessor;
import net.cocoonmc.runtime.impl.CacheKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

import java.util.UUID;

public class BlockFactory extends TransformFactory implements IBlockFactory {

    @Override
    public void setBlockData(net.cocoonmc.core.world.Level level, net.cocoonmc.core.BlockPos blockPos, BlockData blockData) {
        ServerLevel serverLevel = convertToVanilla(level);
        BlockPos blockPos1 = convertToVanilla(blockPos);
        BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos1);
        if (!(blockEntity instanceof SkullBlockEntity)) {
            serverLevel.setBlock(blockPos1, Blocks.PLAYER_HEAD.defaultBlockState(), 2, 1042);
            blockEntity = serverLevel.getBlockEntity(blockPos1);
            if (blockEntity != null) {
                blockData.setBlockAccessor(convertToCocoon(blockEntity));
            }
        }
        if (blockEntity instanceof SkullBlockEntity) {
            Cocoon.API.CACHE.set(blockData, CacheKeys.BLOCK_DATA_KEY, blockData);
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            Property property = new Property("textures", BukkitHelper.getBlockDataTexture(blockData.getBlock(), blockData.getBlockState(), blockData.getBlockTag()));
            profile.getProperties().put("textures", property);
            ((SkullBlockEntity) blockEntity).setOwner(profile);
        }
    }

    @Override
    public BlockData getBlockData(net.cocoonmc.core.world.Level level, net.cocoonmc.core.BlockPos blockPos) {
        ServerLevel serverLevel = convertToVanilla(level);
        BlockPos blockPos1 = convertToVanilla(blockPos);
        BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos1);
        if (!(blockEntity instanceof SkullBlockEntity)) {
            return null;
        }
        GameProfile profile = ((SkullBlockEntity) blockEntity).getOwnerProfile();
        if (profile == null || profile.getName() != null) {
            return null;
        }
        return Cocoon.API.CACHE.computeIfAbsent(blockEntity, CacheKeys.BLOCK_DATA_KEY, it -> {
            String texture = profile.getProperties().get("textures").stream().findFirst().map(Property::getValue).orElse(null);
            BlockData blockData = BukkitHelper.getBlockDataFromTexture(level, blockPos, texture);
            if (blockData != null) {
                blockData.setBlockAccessor(convertToCocoon(blockEntity));
            }
            return blockData;
        });
    }


    private static BlockEntityAccessor convertToCocoon(BlockEntity blockEntity) {
        return new BlockEntityAccessor() {
            @Override
            public void setChanged() {
                blockEntity.setChanged();
            }

            @Override
            public void sendBlockUpdated(net.cocoonmc.core.BlockPos pos, BlockState oldBlockState, BlockState newBlockState, int flags) {
                Level level = blockEntity.getLevel();
                if (level != null) {
                    level.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), flags);
                }
            }
        };
    }
}

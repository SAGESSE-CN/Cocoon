package net.cocoonmc.runtime.client.helper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.HashMap;
import java.util.Optional;

public class BlockHelper {

    private static final HashMap<String, Block> ID_TO_BLOCKS = new HashMap<>();

    public static BlockPos getBlockPos(CompoundTag arg) {
        return new BlockPos(arg.getInt("x"), arg.getInt("y"), arg.getInt("z"));
    }

    public static Pair<BlockState, CompoundTag> getBlockFromTag(CompoundTag blockTag) {
        BlockState state = getBlockState(blockTag.getString("id"), blockTag.getCompound("state"));
        if (state == null) {
            return null;
        }
        CompoundTag entityTag = null;
        if (blockTag.contains("tag", 10)) {
            entityTag = blockTag.getCompound("tag");
        }
        return Pair.of(state, entityTag);
    }

    private static Block getBlock(String id) {
        return ID_TO_BLOCKS.computeIfAbsent(id, it -> RegistryHelper.BLOCKS.getOptional(new ResourceLocation(id)).orElse(null));
    }

    private static BlockState getBlockState(String id, CompoundTag prop) {
        Block block = getBlock(id);
        if (block == null) {
            return null;
        }
        StateDefinition<Block, BlockState> definition = block.getStateDefinition();
        BlockState state = block.defaultBlockState();
        for (String key : prop.getAllKeys()) {
            Property<?> property = definition.getProperty(key);
            if (property != null) {
                state = setBlockStateValue(state, property, prop.getString(key));
            }
        }
        return state;
    }

    private static <T extends Comparable<T>> BlockState setBlockStateValue(BlockState blockState, Property<T> arg, String string) {
        Optional<T> optional = arg.getValue(string);
        return optional.map(t -> blockState.setValue(arg, t)).orElse(blockState);
    }
}

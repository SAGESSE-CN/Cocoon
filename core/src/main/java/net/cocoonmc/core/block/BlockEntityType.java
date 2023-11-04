package net.cocoonmc.core.block;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.utils.ObjectHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class BlockEntityType<T extends BlockEntity> {

    private static final HashMap<ResourceLocation, BlockEntityType<?>> KEYED_BLOCK_ENTITY_TYPES = new HashMap<>();

    private ResourceLocation registryName;
    private final Set<Block> validBlocks;
    private final Factory<T> factory;

    public BlockEntityType(Set<Block> validBlocks, Factory<T> factory) {
        this.validBlocks = validBlocks;
        this.factory = factory;
    }

    @Nullable
    public T create(BlockPos blockPos, BlockState blockState) {
        return factory.create(this, blockPos, blockState);
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public boolean isValid(BlockState state) {
        return this.validBlocks.contains(state.getBlock());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockEntityType)) return false;
        BlockEntityType<?> that = (BlockEntityType<?>) o;
        return Objects.equals(registryName, that.registryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registryName);
    }

    @Override
    public String toString() {
        return ObjectHelper.makeDescription(this, "id", getRegistryName());
    }

    public static BlockEntityType<?> byKey(ResourceLocation registryName) {
        return KEYED_BLOCK_ENTITY_TYPES.get(registryName);
    }

    public static <T extends BlockEntity> BlockEntityType<T> register(ResourceLocation registryName, BlockEntityType<T> blockEntityType) {
        blockEntityType.registryName = registryName;
        KEYED_BLOCK_ENTITY_TYPES.put(registryName, blockEntityType);
        return blockEntityType;
    }

    public interface Factory<T extends BlockEntity> {

        T create(BlockEntityType<?> type, BlockPos var1, BlockState var2);
    }
}

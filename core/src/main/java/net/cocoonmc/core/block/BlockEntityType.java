package net.cocoonmc.core.block;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.world.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class BlockEntityType<T extends BlockEntity> {

    private static final HashMap<ResourceLocation, BlockEntityType<?>> KEYED_BLOCK_ENTITY_TYPES = new HashMap<>();

    private ResourceLocation registryName;
    private final Factory<T> factory;

    public BlockEntityType(Factory<T> factory) {
        this.factory = factory;
    }


    @Nullable
    public T create(Level level, BlockPos blockPos, BlockState blockState) {
        return factory.create(level, blockPos, blockState);
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public static BlockEntityType<?> byKey(ResourceLocation registryName) {
        return KEYED_BLOCK_ENTITY_TYPES.get(registryName);
    }

    public static <T extends BlockEntity> BlockEntityType<T> register(ResourceLocation registryName, BlockEntityType<T> blockEntityType) {
        blockEntityType.registryName = registryName;
        KEYED_BLOCK_ENTITY_TYPES.put(registryName, blockEntityType);
        return blockEntityType;
    }

    public interface Factory<T> {

        T create(Level level, BlockPos var1, BlockState var2);
    }
}

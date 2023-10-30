package net.cocoonmc.core.block;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.state.StateDefinition;
import net.cocoonmc.core.item.context.BlockPlaceContext;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.utils.SimpleAssociatedStorage;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;
import net.cocoonmc.runtime.IAssociatedContainer;
import net.cocoonmc.runtime.IAssociatedContainerProvider;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;

public class Block implements IAssociatedContainerProvider {

    private static final HashMap<ResourceLocation, Block> KEYED_BLOCKS = new HashMap<>();

    private ResourceLocation registryName;

    protected final StateDefinition<Block, BlockState> stateDefinition;
    private BlockState defaultBlockState;

    private final Properties properties;
    private final SimpleAssociatedStorage storage = new SimpleAssociatedStorage();

    public Block(Properties properties) {
        this.properties = properties;
        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(builder);
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(this.stateDefinition.any());
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    public InteractionResult attack(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return InteractionResult.PASS;
    }

    public boolean canSurvive(BlockState blockState, Level level, BlockPos blockPos) {
        return true;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    }

    @Nullable
    public BlockEntity createBlockEntity(Level level, BlockPos pos, BlockState blockState) {
        BlockEntityType<?> entityType = properties.blockEntityType;
        if (entityType != null) {
            return entityType.create(level, pos, blockState);
        }
        return null;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState();
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public void registerDefaultState(BlockState defaultBlockState) {
        this.defaultBlockState = defaultBlockState;
    }

    public BlockState defaultBlockState() {
        return defaultBlockState;
    }

    @Override
    public IAssociatedContainer getAssociatedContainer() {
        return storage;
    }

    @Nullable
    public Material asMaterial() {
        return properties.material;
    }

    public boolean isInteractable() {
        if (properties.material != null) {
            return properties.material.isInteractable();
        }
        return properties.isInteractable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block block = (Block) o;
        return Objects.equals(registryName, block.registryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registryName);
    }

    public static Block byKey(ResourceLocation key) {
        return KEYED_BLOCKS.get(key);
    }

    public static Block register(ResourceLocation registryName, Block block) {
        block.registryName = registryName;
        KEYED_BLOCKS.put(registryName, block);
        return block;
    }

    public static class Properties {

        Material material;
        BlockEntityType<?> blockEntityType;
        boolean isInteractable = false;
        boolean noDrops = false;
        boolean noOcclusion = false;
        boolean noCollission = false;

        public Properties noDrops() {
            this.noDrops = true;
            return this;
        }

        public Properties noOcclusion() {
            this.noOcclusion = true;
            return this;
        }

        public Properties noCollission() {
            this.noCollission = true;
            return this;
        }

        public Properties isInteractable() {
            this.isInteractable = true;
            return this;
        }

        public Properties entity(BlockEntityType<?> entityType) {
            this.blockEntityType = entityType;
            return this;
        }

        public Properties material(Material material) {
            this.material = material;
            return this;
        }
    }

}

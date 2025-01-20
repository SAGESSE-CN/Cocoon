package net.cocoonmc.core.block;

import com.google.common.collect.Lists;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.state.StateDefinition;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.context.BlockPlaceContext;
import net.cocoonmc.core.math.VoxelShape;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.utils.ObjectHelper;
import net.cocoonmc.core.utils.SimpleAssociatedStorage;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.LivingEntity;
import net.cocoonmc.core.world.entity.Player;
import net.cocoonmc.core.world.loot.LootContext;
import net.cocoonmc.runtime.IAssociatedContainer;
import net.cocoonmc.runtime.IAssociatedContainerProvider;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Block implements IAssociatedContainerProvider {

    private static final Map<ResourceLocation, Block> KEYED_BLOCKS = new ConcurrentHashMap<>();

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

    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, BlockPlaceContext context) {
    }

    public void onPlace(Level level, BlockPos blockPos, BlockState oldBlockState, BlockState newBlockState, boolean bl) {
    }

    public void onRemove(Level level, BlockPos blockPos, BlockState oldBlockState, BlockState newBlockState, boolean bl) {
    }

    public void onNeighborChanged(Level level, BlockPos pos, BlockState state, BlockPos sourcePos, Block sourceBlock, boolean bl) {
    }

    public boolean isBed(Level level, BlockPos pos, BlockState state, @Nullable Entity entity) {
        return false;
    }

    public boolean isLadder(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity) {
        return false;
    }

    public boolean canSurvive(BlockState blockState, Level level, BlockPos blockPos) {
        return true;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState();
    }

    public List<ItemStack> getDrops(BlockState blockState, LootContext context) {
        return Lists.newArrayList(asItem().getDefaultInstance());
    }

    public VoxelShape getCollisionShape(Level level, BlockPos blockPos, BlockState blockState) {
        return VoxelShape.BLOCK;
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

    public Block getDelegate() {
        if (properties.delegate != null) {
            return properties.delegate;
        }
        if (properties.material != null) {
            return this;
        }
        return Blocks.DIRT;
    }

    @Nullable
    public Material asBukkit() {
        return properties.material;
    }

    public Item asItem() {
        return Item.byBlock(this);
    }

    public boolean isInteractable() {
        if (properties.material != null) {
            return properties.material.isInteractable();
        }
        return false;
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

    @Override
    public String toString() {
        return ObjectHelper.makeDescription(this, "id", getRegistryName());
    }

    public static Collection<Block> values() {
        return KEYED_BLOCKS.values();
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

        Block delegate;
        Material material;
        boolean noDrops = false;
        boolean noOcclusion = false;
        boolean noCollission = false;
        boolean dynamicShape = false;

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

        public Properties dymanicShape() {
            this.dynamicShape = true;
            return this;
        }

        public Properties delegate(Block delegate) {
            this.delegate = delegate;
            return this;
        }

        public Properties material(Material material) {
            this.material = material;
            return this;
        }
    }

}

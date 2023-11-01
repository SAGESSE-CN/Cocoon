package net.cocoonmc.core.item;

import com.google.common.collect.Maps;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.item.context.UseOnContext;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.utils.SimpleAssociatedStorage;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.InteractionResultHolder;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.LivingEntity;
import net.cocoonmc.core.world.entity.Player;
import net.cocoonmc.runtime.IAssociatedContainer;
import net.cocoonmc.runtime.IAssociatedContainerProvider;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Item implements IAssociatedContainerProvider {

    private static final Map<Block, Item> BY_BLOCK = Maps.newHashMap();
    private static final HashMap<ResourceLocation, Item> KEYED_ITEMS = new HashMap<>();

    private ResourceLocation registryName;

    private final Properties properties;
    private final SimpleAssociatedStorage storage = new SimpleAssociatedStorage();

    public Item(Properties properties) {
        this.properties = properties;
    }

    public InteractionResultHolder<ItemStack> use(ItemStack itemStack, Player player, InteractionHand hand) {
        return InteractionResultHolder.pass(itemStack);
    }

    /**
     * This is called when the item is used, before the block is activated.
     *
     * @return Return PASS to allow vanilla handling, any other to skip normal code.
     */
    public InteractionResult useOnFirst(ItemStack stack, UseOnContext context) {
        return InteractionResult.PASS;
    }

    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    /**
     * Called when the player Left Clicks (attacks) an entity. Processed before
     * damage is done, if return value is true further processing is canceled and
     * the entity is not attacked.
     *
     * @param stack  The Item being used
     * @param player The player that is attacking
     * @param entity The entity being attacked
     * @return True to cancel the rest of the interaction.
     */
    public InteractionResult attackLivingEntity(ItemStack stack, Player player, Entity entity) {
        return InteractionResult.PASS;
    }

    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    /**
     * Should this item, when held, allow sneak-clicks to pass through to the
     * underlying block?
     *
     * @param stack  The Item being used
     * @param player The Player that is wielding the item
     * @param level  The world
     * @param pos    Block position in level
     */
    public boolean doesSneakBypassUse(ItemStack stack, Player player, Level level, BlockPos pos) {
        return false;
    }

    public int getMaxStackSize() {
        return properties.maxStackSize;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public IAssociatedContainer getAssociatedContainer() {
        return storage;
    }

    @Nullable
    public Material asMaterial() {
        return properties.material;
    }

    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(registryName, item.registryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registryName);
    }


    public static Item byKey(ResourceLocation registryName) {
        return KEYED_ITEMS.get(registryName);
    }

    public static Item byBlock(Block block) {
        return BY_BLOCK.getOrDefault(block, Items.AIR);
    }

    public static Item register(ResourceLocation registryName, Item item) {
        item.registryName = registryName;
        KEYED_ITEMS.put(registryName, item);
        if (item instanceof BlockItem) {
            BY_BLOCK.put(((BlockItem) item).getBlock(), item);
        }
        return item;
    }

    public static class Properties {

        int maxStackSize = 64;
        int maxDamage = 1;

        Material material;

        public Properties stacksTo(int i) {
            this.maxStackSize = i;
            return this;
        }

        public Properties durability(int i) {
            this.maxDamage = i;
            this.maxStackSize = 1;
            return this;
        }

        public Properties material(Material material) {
            this.material = material;
            if (material != null) {
                this.maxStackSize = material.getMaxStackSize();
                this.maxDamage = material.getMaxDurability();
            }
            return this;
        }
    }
}

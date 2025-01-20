package net.cocoonmc.core.world.entity;

import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.utils.BukkitHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class EntityTypes {

    private static final Map<org.bukkit.entity.EntityType, EntityType<?>> TYPE_TO_TYPES = new ConcurrentHashMap<>();

    public static final EntityType<Entity> DROPPED_ITEM = register(Entity::new, "minecraft:item");
    public static final EntityType<Entity> EXPERIENCE_ORB = register(Entity::new, "minecraft:experience_orb");
    public static final EntityType<Entity> AREA_EFFECT_CLOUD = register(Entity::new, "minecraft:area_effect_cloud");
    public static final EntityType<LivingEntity> ELDER_GUARDIAN = register(LivingEntity::new, "minecraft:elder_guardian");
    public static final EntityType<LivingEntity> WITHER_SKELETON = register(LivingEntity::new, "minecraft:wither_skeleton");
    public static final EntityType<LivingEntity> STRAY = register(LivingEntity::new, "minecraft:stray");
    public static final EntityType<Entity> EGG = register(Entity::new, "minecraft:egg");
    public static final EntityType<Entity> LEASH_HITCH = register(Entity::new, "minecraft:leash_knot");
    public static final EntityType<Entity> PAINTING = register(Entity::new, "minecraft:painting");
    public static final EntityType<Entity> ARROW = register(Entity::new, "minecraft:arrow");
    public static final EntityType<Entity> SNOWBALL = register(Entity::new, "minecraft:snowball");
    public static final EntityType<Entity> FIREBALL = register(Entity::new, "minecraft:fireball");
    public static final EntityType<Entity> SMALL_FIREBALL = register(Entity::new, "minecraft:small_fireball");
    public static final EntityType<Entity> ENDER_PEARL = register(Entity::new, "minecraft:ender_pearl");
    public static final EntityType<Entity> ENDER_SIGNAL = register(Entity::new, "minecraft:eye_of_ender");
    public static final EntityType<Entity> SPLASH_POTION = register(Entity::new, "minecraft:potion");
    public static final EntityType<Entity> THROWN_EXP_BOTTLE = register(Entity::new, "minecraft:experience_bottle");
    public static final EntityType<Entity> ITEM_FRAME = register(Entity::new, "minecraft:item_frame");
    public static final EntityType<Entity> WITHER_SKULL = register(Entity::new, "minecraft:wither_skull");
    public static final EntityType<Entity> PRIMED_TNT = register(Entity::new, "minecraft:tnt");
    public static final EntityType<Entity> FALLING_BLOCK = register(Entity::new, "minecraft:falling_block");
    public static final EntityType<Entity> FIREWORK = register(Entity::new, "minecraft:firework_rocket");
    public static final EntityType<LivingEntity> HUSK = register(LivingEntity::new, "minecraft:husk");
    public static final EntityType<Entity> SPECTRAL_ARROW = register(Entity::new, "minecraft:spectral_arrow");
    public static final EntityType<Entity> SHULKER_BULLET = register(Entity::new, "minecraft:shulker_bullet");
    public static final EntityType<Entity> DRAGON_FIREBALL = register(Entity::new, "minecraft:dragon_fireball");
    public static final EntityType<LivingEntity> ZOMBIE_VILLAGER = register(LivingEntity::new, "minecraft:zombie_villager");
    public static final EntityType<LivingEntity> SKELETON_HORSE = register(LivingEntity::new, "minecraft:skeleton_horse");
    public static final EntityType<LivingEntity> ZOMBIE_HORSE = register(LivingEntity::new, "minecraft:zombie_horse");
    public static final EntityType<LivingEntity> ARMOR_STAND = register(LivingEntity::new, "minecraft:armor_stand");
    public static final EntityType<LivingEntity> DONKEY = register(LivingEntity::new, "minecraft:donkey");
    public static final EntityType<LivingEntity> MULE = register(LivingEntity::new, "minecraft:mule");
    public static final EntityType<Entity> EVOKER_FANGS = register(Entity::new, "minecraft:evoker_fangs");
    public static final EntityType<LivingEntity> EVOKER = register(LivingEntity::new, "minecraft:evoker");
    public static final EntityType<LivingEntity> VEX = register(LivingEntity::new, "minecraft:vex");
    public static final EntityType<LivingEntity> VINDICATOR = register(LivingEntity::new, "minecraft:vindicator");
    public static final EntityType<LivingEntity> ILLUSIONER = register(LivingEntity::new, "minecraft:illusioner");
    public static final EntityType<Entity> MINECART_COMMAND = register(Entity::new, "minecraft:command_block_minecart");
    public static final EntityType<Entity> BOAT = register(LivingEntity::new, "minecraft:boat");
    public static final EntityType<Entity> MINECART = register(Entity::new, "minecraft:minecart");
    public static final EntityType<Entity> MINECART_CHEST = register(Entity::new, "minecraft:chest_minecart");
    public static final EntityType<Entity> MINECART_FURNACE = register(Entity::new, "minecraft:furnace_minecart");
    public static final EntityType<Entity> MINECART_TNT = register(Entity::new, "minecraft:tnt_minecart");
    public static final EntityType<Entity> MINECART_HOPPER = register(Entity::new, "minecraft:hopper_minecart");
    public static final EntityType<Entity> MINECART_MOB_SPAWNER = register(Entity::new, "minecraft:spawner_minecart");
    public static final EntityType<LivingEntity> CREEPER = register(LivingEntity::new, "minecraft:creeper");
    public static final EntityType<LivingEntity> SKELETON = register(LivingEntity::new, "minecraft:skeleton");
    public static final EntityType<LivingEntity> SPIDER = register(LivingEntity::new, "minecraft:spider");
    public static final EntityType<LivingEntity> GIANT = register(LivingEntity::new, "minecraft:giant");
    public static final EntityType<LivingEntity> ZOMBIE = register(LivingEntity::new, "minecraft:zombie");
    public static final EntityType<LivingEntity> SLIME = register(LivingEntity::new, "minecraft:slime");
    public static final EntityType<LivingEntity> GHAST = register(LivingEntity::new, "minecraft:ghast");
    public static final EntityType<LivingEntity> ZOMBIFIED_PIGLIN = register(LivingEntity::new, "minecraft:zombified_piglin");
    public static final EntityType<LivingEntity> ENDERMAN = register(LivingEntity::new, "minecraft:enderman");
    public static final EntityType<LivingEntity> CAVE_SPIDER = register(LivingEntity::new, "minecraft:cave_spider");
    public static final EntityType<LivingEntity> SILVERFISH = register(LivingEntity::new, "minecraft:silverfish");
    public static final EntityType<LivingEntity> BLAZE = register(LivingEntity::new, "minecraft:blaze");
    public static final EntityType<LivingEntity> MAGMA_CUBE = register(LivingEntity::new, "minecraft:magma_cube");
    public static final EntityType<LivingEntity> ENDER_DRAGON = register(LivingEntity::new, "minecraft:ender_dragon");
    public static final EntityType<LivingEntity> WITHER = register(LivingEntity::new, "minecraft:wither");
    public static final EntityType<LivingEntity> BAT = register(LivingEntity::new, "minecraft:bat");
    public static final EntityType<LivingEntity> WITCH = register(LivingEntity::new, "minecraft:witch");
    public static final EntityType<LivingEntity> ENDERMITE = register(LivingEntity::new, "minecraft:endermite");
    public static final EntityType<LivingEntity> GUARDIAN = register(LivingEntity::new, "minecraft:guardian");
    public static final EntityType<LivingEntity> SHULKER = register(LivingEntity::new, "minecraft:shulker");
    public static final EntityType<LivingEntity> PIG = register(LivingEntity::new, "minecraft:pig");
    public static final EntityType<LivingEntity> SHEEP = register(LivingEntity::new, "minecraft:sheep");
    public static final EntityType<LivingEntity> COW = register(LivingEntity::new, "minecraft:cow");
    public static final EntityType<LivingEntity> CHICKEN = register(LivingEntity::new, "minecraft:chicken");
    public static final EntityType<LivingEntity> SQUID = register(LivingEntity::new, "minecraft:squid");
    public static final EntityType<LivingEntity> WOLF = register(LivingEntity::new, "minecraft:wolf");
    public static final EntityType<LivingEntity> MUSHROOM_COW = register(LivingEntity::new, "minecraft:mooshroom");
    public static final EntityType<LivingEntity> SNOWMAN = register(LivingEntity::new, "minecraft:snow_golem");
    public static final EntityType<LivingEntity> OCELOT = register(LivingEntity::new, "minecraft:ocelot");
    public static final EntityType<LivingEntity> IRON_GOLEM = register(LivingEntity::new, "minecraft:iron_golem");
    public static final EntityType<LivingEntity> HORSE = register(LivingEntity::new, "minecraft:horse");
    public static final EntityType<LivingEntity> RABBIT = register(LivingEntity::new, "minecraft:rabbit");
    public static final EntityType<LivingEntity> POLAR_BEAR = register(LivingEntity::new, "minecraft:polar_bear");
    public static final EntityType<LivingEntity> LLAMA = register(LivingEntity::new, "minecraft:llama");
    public static final EntityType<Entity> LLAMA_SPIT = register(Entity::new, "minecraft:llama_spit");
    public static final EntityType<LivingEntity> PARROT = register(LivingEntity::new, "minecraft:parrot");
    public static final EntityType<LivingEntity> VILLAGER = register(LivingEntity::new, "minecraft:villager");
    public static final EntityType<Entity> ENDER_CRYSTAL = register(Entity::new, "minecraft:end_crystal");
    public static final EntityType<LivingEntity> TURTLE = register(LivingEntity::new, "minecraft:turtle");
    public static final EntityType<LivingEntity> PHANTOM = register(LivingEntity::new, "minecraft:phantom");
    public static final EntityType<Entity> TRIDENT = register(Entity::new, "minecraft:trident");
    public static final EntityType<LivingEntity> COD = register(LivingEntity::new, "minecraft:cod");
    public static final EntityType<LivingEntity> SALMON = register(LivingEntity::new, "minecraft:salmon");
    public static final EntityType<LivingEntity> PUFFERFISH = register(LivingEntity::new, "minecraft:pufferfish");
    public static final EntityType<LivingEntity> TROPICAL_FISH = register(LivingEntity::new, "minecraft:tropical_fish");
    public static final EntityType<LivingEntity> DROWNED = register(LivingEntity::new, "minecraft:drowned");
    public static final EntityType<LivingEntity> DOLPHIN = register(LivingEntity::new, "minecraft:dolphin");
    public static final EntityType<LivingEntity> CAT = register(LivingEntity::new, "minecraft:cat");
    public static final EntityType<LivingEntity> PANDA = register(LivingEntity::new, "minecraft:panda");
    public static final EntityType<LivingEntity> PILLAGER = register(LivingEntity::new, "minecraft:pillager");
    public static final EntityType<LivingEntity> RAVAGER = register(LivingEntity::new, "minecraft:ravager");
    public static final EntityType<LivingEntity> TRADER_LLAMA = register(LivingEntity::new, "minecraft:trader_llama");
    public static final EntityType<LivingEntity> WANDERING_TRADER = register(LivingEntity::new, "minecraft:wandering_trader");
    public static final EntityType<LivingEntity> FOX = register(LivingEntity::new, "minecraft:fox");
    public static final EntityType<LivingEntity> BEE = register(LivingEntity::new, "minecraft:bee");
    public static final EntityType<LivingEntity> HOGLIN = register(LivingEntity::new, "minecraft:hoglin");
    public static final EntityType<LivingEntity> PIGLIN = register(LivingEntity::new, "minecraft:piglin");
    public static final EntityType<LivingEntity> STRIDER = register(LivingEntity::new, "minecraft:strider");
    public static final EntityType<LivingEntity> ZOGLIN = register(LivingEntity::new, "minecraft:zoglin");
    public static final EntityType<LivingEntity> PIGLIN_BRUTE = register(LivingEntity::new, "minecraft:piglin_brute");
    public static final EntityType<LivingEntity> AXOLOTL = register(LivingEntity::new, "minecraft:axolotl");
    public static final EntityType<Entity> GLOW_ITEM_FRAME = register(Entity::new, "minecraft:glow_item_frame");
    public static final EntityType<LivingEntity> GLOW_SQUID = register(LivingEntity::new, "minecraft:glow_squid");
    public static final EntityType<LivingEntity> GOAT = register(LivingEntity::new, "minecraft:goat");
    public static final EntityType<Entity> MARKER = register(Entity::new, "minecraft:marker");
    public static final EntityType<LivingEntity> ALLAY = register(LivingEntity::new, "minecraft:allay");
    public static final EntityType<Entity> CHEST_BOAT = register(Entity::new, "minecraft:chest_boat");
    public static final EntityType<LivingEntity> FROG = register(LivingEntity::new, "minecraft:frog");
    public static final EntityType<LivingEntity> TADPOLE = register(LivingEntity::new, "minecraft:tadpole");
    public static final EntityType<LivingEntity> WARDEN = register(LivingEntity::new, "minecraft:warden");
    public static final EntityType<LivingEntity> CAMEL = register(LivingEntity::new, "minecraft:camel");
    public static final EntityType<Entity> BLOCK_DISPLAY = register(Entity::new, "minecraft:block_display");
    public static final EntityType<Entity> INTERACTION = register(Entity::new, "minecraft:interaction");
    public static final EntityType<Entity> ITEM_DISPLAY = register(Entity::new, "minecraft:item_display");
    public static final EntityType<LivingEntity> SNIFFER = register(LivingEntity::new, "minecraft:sniffer");
    public static final EntityType<Entity> TEXT_DISPLAY = register(Entity::new, "minecraft:text_display");
    public static final EntityType<Entity> FISHING_HOOK = register(Entity::new, "minecraft:fishing_bobber");
    public static final EntityType<Entity> LIGHTNING = register(Entity::new, "minecraft:lightning_bolt");
    public static final EntityType<Player> PLAYER = register(Player::new, "minecraft:player");

    private static final EntityType<Entity> UNKNOWN_ENTITY_TYPE = registerUnknown(Entity::new);
    private static final EntityType<LivingEntity> UNKNOWN_LIVING_ENTITY_TYPE = registerUnknown(LivingEntity::new);


    @SuppressWarnings("unchecked")
    public static EntityType<Entity> findEntityType(org.bukkit.entity.Entity entity) {
        EntityType<?> entityType = BukkitHelper.getCustomEntityType(entity);
        if (entityType != null) {
            return (EntityType<Entity>) entityType;
        }
        entityType = TYPE_TO_TYPES.get(entity.getType());
        if (entityType != null) {
            return (EntityType<Entity>) entityType;
        }
        return UNKNOWN_ENTITY_TYPE;
    }

    @SuppressWarnings("unchecked")
    public static EntityType<LivingEntity> findEntityType(org.bukkit.entity.LivingEntity entity) {
        EntityType<?> entityType = BukkitHelper.getCustomEntityType(entity);
        if (entityType != null) {
            return (EntityType<LivingEntity>) entityType;
        }
        // the custom entity always choose a vanilla entity type, but it won't always confirm class hierarchy.
        entityType = TYPE_TO_TYPES.get(entity.getType());
        if (entityType != null) {
            if (entityType.asBukkit() == null || !BukkitHelper.isTwistedEntity(entity)) {
                return (EntityType<LivingEntity>) entityType;
            }
        }
        return UNKNOWN_LIVING_ENTITY_TYPE;
    }

    public static <T extends Entity> EntityType<T> register(EntityType.Factory<T> factory, String id) {
        ResourceLocation key = new ResourceLocation(id);
        org.bukkit.entity.EntityType entityType = org.bukkit.entity.EntityType.fromName(key.getPath());
        if (entityType != null) {
            return register(factory, key, entityType);
        }
        return null;
    }

    public static <T extends Entity> EntityType<T> register(EntityType.Factory<T> factory, ResourceLocation key, org.bukkit.entity.EntityType entityType) {
        EntityType<T> newType = EntityType.register(key, new EntityType<>(factory) {
            @Override
            public org.bukkit.entity.EntityType asBukkit() {
                return entityType;
            }
        });
        TYPE_TO_TYPES.put(entityType, newType);
        return newType;
    }

    public static <T extends Entity> EntityType<T> registerUnknown(EntityType.Factory<T> factory) {
        return EntityType.register(new ResourceLocation("minecraft", "unknown"), new EntityType<>(factory));
    }
}

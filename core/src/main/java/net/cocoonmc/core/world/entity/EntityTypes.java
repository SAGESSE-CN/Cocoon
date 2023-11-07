package net.cocoonmc.core.world.entity;

import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.utils.BukkitHelper;

@SuppressWarnings("unused")
public class EntityTypes {

    public static final EntityType<Entity> DROPPED_ITEM = register(Entity::new, org.bukkit.entity.EntityType.DROPPED_ITEM);
    public static final EntityType<Entity> EXPERIENCE_ORB = register(Entity::new, org.bukkit.entity.EntityType.EXPERIENCE_ORB);
    public static final EntityType<Entity> AREA_EFFECT_CLOUD = register(Entity::new, org.bukkit.entity.EntityType.AREA_EFFECT_CLOUD);
    public static final EntityType<LivingEntity> ELDER_GUARDIAN = register(LivingEntity::new, org.bukkit.entity.EntityType.ELDER_GUARDIAN);
    public static final EntityType<LivingEntity> WITHER_SKELETON = register(LivingEntity::new, org.bukkit.entity.EntityType.WITHER_SKELETON);
    public static final EntityType<LivingEntity> STRAY = register(LivingEntity::new, org.bukkit.entity.EntityType.STRAY);
    public static final EntityType<Entity> EGG = register(Entity::new, org.bukkit.entity.EntityType.EGG);
    public static final EntityType<Entity> LEASH_HITCH = register(Entity::new, org.bukkit.entity.EntityType.LEASH_HITCH);
    public static final EntityType<Entity> PAINTING = register(Entity::new, org.bukkit.entity.EntityType.PAINTING);
    public static final EntityType<Entity> ARROW = register(Entity::new, org.bukkit.entity.EntityType.ARROW);
    public static final EntityType<Entity> SNOWBALL = register(Entity::new, org.bukkit.entity.EntityType.SNOWBALL);
    public static final EntityType<Entity> FIREBALL = register(Entity::new, org.bukkit.entity.EntityType.FIREBALL);
    public static final EntityType<Entity> SMALL_FIREBALL = register(Entity::new, org.bukkit.entity.EntityType.SMALL_FIREBALL);
    public static final EntityType<Entity> ENDER_PEARL = register(Entity::new, org.bukkit.entity.EntityType.ENDER_PEARL);
    public static final EntityType<Entity> ENDER_SIGNAL = register(Entity::new, org.bukkit.entity.EntityType.ENDER_SIGNAL);
    public static final EntityType<Entity> SPLASH_POTION = register(Entity::new, org.bukkit.entity.EntityType.SPLASH_POTION);
    public static final EntityType<Entity> THROWN_EXP_BOTTLE = register(Entity::new, org.bukkit.entity.EntityType.THROWN_EXP_BOTTLE);
    public static final EntityType<Entity> ITEM_FRAME = register(Entity::new, org.bukkit.entity.EntityType.ITEM_FRAME);
    public static final EntityType<Entity> WITHER_SKULL = register(Entity::new, org.bukkit.entity.EntityType.WITHER_SKULL);
    public static final EntityType<Entity> PRIMED_TNT = register(Entity::new, org.bukkit.entity.EntityType.PRIMED_TNT);
    public static final EntityType<Entity> FALLING_BLOCK = register(Entity::new, org.bukkit.entity.EntityType.FALLING_BLOCK);
    public static final EntityType<Entity> FIREWORK = register(Entity::new, org.bukkit.entity.EntityType.FIREWORK);
    public static final EntityType<LivingEntity> HUSK = register(LivingEntity::new, org.bukkit.entity.EntityType.HUSK);
    public static final EntityType<Entity> SPECTRAL_ARROW = register(Entity::new, org.bukkit.entity.EntityType.SPECTRAL_ARROW);
    public static final EntityType<Entity> SHULKER_BULLET = register(Entity::new, org.bukkit.entity.EntityType.SHULKER_BULLET);
    public static final EntityType<Entity> DRAGON_FIREBALL = register(Entity::new, org.bukkit.entity.EntityType.DRAGON_FIREBALL);
    public static final EntityType<LivingEntity> ZOMBIE_VILLAGER = register(LivingEntity::new, org.bukkit.entity.EntityType.ZOMBIE_VILLAGER);
    public static final EntityType<LivingEntity> SKELETON_HORSE = register(LivingEntity::new, org.bukkit.entity.EntityType.SKELETON_HORSE);
    public static final EntityType<LivingEntity> ZOMBIE_HORSE = register(LivingEntity::new, org.bukkit.entity.EntityType.ZOMBIE_HORSE);
    public static final EntityType<LivingEntity> ARMOR_STAND = register(LivingEntity::new, org.bukkit.entity.EntityType.ARMOR_STAND);
    public static final EntityType<LivingEntity> DONKEY = register(LivingEntity::new, org.bukkit.entity.EntityType.DONKEY);
    public static final EntityType<LivingEntity> MULE = register(LivingEntity::new, org.bukkit.entity.EntityType.MULE);
    public static final EntityType<Entity> EVOKER_FANGS = register(Entity::new, org.bukkit.entity.EntityType.EVOKER_FANGS);
    public static final EntityType<LivingEntity> EVOKER = register(LivingEntity::new, org.bukkit.entity.EntityType.EVOKER);
    public static final EntityType<LivingEntity> VEX = register(LivingEntity::new, org.bukkit.entity.EntityType.VEX);
    public static final EntityType<LivingEntity> VINDICATOR = register(LivingEntity::new, org.bukkit.entity.EntityType.VINDICATOR);
    public static final EntityType<LivingEntity> ILLUSIONER = register(LivingEntity::new, org.bukkit.entity.EntityType.ILLUSIONER);
    public static final EntityType<Entity> MINECART_COMMAND = register(Entity::new, org.bukkit.entity.EntityType.MINECART_COMMAND);
    public static final EntityType<Entity> BOAT = register(Entity::new, org.bukkit.entity.EntityType.BOAT);
    public static final EntityType<Entity> MINECART = register(Entity::new, org.bukkit.entity.EntityType.MINECART);
    public static final EntityType<Entity> MINECART_CHEST = register(Entity::new, org.bukkit.entity.EntityType.MINECART_CHEST);
    public static final EntityType<Entity> MINECART_FURNACE = register(Entity::new, org.bukkit.entity.EntityType.MINECART_FURNACE);
    public static final EntityType<Entity> MINECART_TNT = register(Entity::new, org.bukkit.entity.EntityType.MINECART_TNT);
    public static final EntityType<Entity> MINECART_HOPPER = register(Entity::new, org.bukkit.entity.EntityType.MINECART_HOPPER);
    public static final EntityType<Entity> MINECART_MOB_SPAWNER = register(Entity::new, org.bukkit.entity.EntityType.MINECART_MOB_SPAWNER);
    public static final EntityType<LivingEntity> CREEPER = register(LivingEntity::new, org.bukkit.entity.EntityType.CREEPER);
    public static final EntityType<LivingEntity> SKELETON = register(LivingEntity::new, org.bukkit.entity.EntityType.SKELETON);
    public static final EntityType<LivingEntity> SPIDER = register(LivingEntity::new, org.bukkit.entity.EntityType.SPIDER);
    public static final EntityType<LivingEntity> GIANT = register(LivingEntity::new, org.bukkit.entity.EntityType.GIANT);
    public static final EntityType<LivingEntity> ZOMBIE = register(LivingEntity::new, org.bukkit.entity.EntityType.ZOMBIE);
    public static final EntityType<LivingEntity> SLIME = register(LivingEntity::new, org.bukkit.entity.EntityType.SLIME);
    public static final EntityType<LivingEntity> GHAST = register(LivingEntity::new, org.bukkit.entity.EntityType.GHAST);
    public static final EntityType<LivingEntity> ZOMBIFIED_PIGLIN = register(LivingEntity::new, org.bukkit.entity.EntityType.ZOMBIFIED_PIGLIN);
    public static final EntityType<LivingEntity> ENDERMAN = register(LivingEntity::new, org.bukkit.entity.EntityType.ENDERMAN);
    public static final EntityType<LivingEntity> CAVE_SPIDER = register(LivingEntity::new, org.bukkit.entity.EntityType.CAVE_SPIDER);
    public static final EntityType<LivingEntity> SILVERFISH = register(LivingEntity::new, org.bukkit.entity.EntityType.SILVERFISH);
    public static final EntityType<LivingEntity> BLAZE = register(LivingEntity::new, org.bukkit.entity.EntityType.BLAZE);
    public static final EntityType<LivingEntity> MAGMA_CUBE = register(LivingEntity::new, org.bukkit.entity.EntityType.MAGMA_CUBE);
    public static final EntityType<LivingEntity> ENDER_DRAGON = register(LivingEntity::new, org.bukkit.entity.EntityType.ENDER_DRAGON);
    public static final EntityType<LivingEntity> WITHER = register(LivingEntity::new, org.bukkit.entity.EntityType.WITHER);
    public static final EntityType<LivingEntity> BAT = register(LivingEntity::new, org.bukkit.entity.EntityType.BAT);
    public static final EntityType<LivingEntity> WITCH = register(LivingEntity::new, org.bukkit.entity.EntityType.WITCH);
    public static final EntityType<LivingEntity> ENDERMITE = register(LivingEntity::new, org.bukkit.entity.EntityType.ENDERMITE);
    public static final EntityType<LivingEntity> GUARDIAN = register(LivingEntity::new, org.bukkit.entity.EntityType.GUARDIAN);
    public static final EntityType<LivingEntity> SHULKER = register(LivingEntity::new, org.bukkit.entity.EntityType.SHULKER);
    public static final EntityType<LivingEntity> PIG = register(LivingEntity::new, org.bukkit.entity.EntityType.PIG);
    public static final EntityType<LivingEntity> SHEEP = register(LivingEntity::new, org.bukkit.entity.EntityType.SHEEP);
    public static final EntityType<LivingEntity> COW = register(LivingEntity::new, org.bukkit.entity.EntityType.COW);
    public static final EntityType<LivingEntity> CHICKEN = register(LivingEntity::new, org.bukkit.entity.EntityType.CHICKEN);
    public static final EntityType<LivingEntity> SQUID = register(LivingEntity::new, org.bukkit.entity.EntityType.SQUID);
    public static final EntityType<LivingEntity> WOLF = register(LivingEntity::new, org.bukkit.entity.EntityType.WOLF);
    public static final EntityType<LivingEntity> MUSHROOM_COW = register(LivingEntity::new, org.bukkit.entity.EntityType.MUSHROOM_COW);
    public static final EntityType<LivingEntity> SNOWMAN = register(LivingEntity::new, org.bukkit.entity.EntityType.SNOWMAN);
    public static final EntityType<LivingEntity> OCELOT = register(LivingEntity::new, org.bukkit.entity.EntityType.OCELOT);
    public static final EntityType<LivingEntity> IRON_GOLEM = register(LivingEntity::new, org.bukkit.entity.EntityType.IRON_GOLEM);
    public static final EntityType<LivingEntity> HORSE = register(LivingEntity::new, org.bukkit.entity.EntityType.HORSE);
    public static final EntityType<LivingEntity> RABBIT = register(LivingEntity::new, org.bukkit.entity.EntityType.RABBIT);
    public static final EntityType<LivingEntity> POLAR_BEAR = register(LivingEntity::new, org.bukkit.entity.EntityType.POLAR_BEAR);
    public static final EntityType<LivingEntity> LLAMA = register(LivingEntity::new, org.bukkit.entity.EntityType.LLAMA);
    public static final EntityType<Entity> LLAMA_SPIT = register(Entity::new, org.bukkit.entity.EntityType.LLAMA_SPIT);
    public static final EntityType<LivingEntity> PARROT = register(LivingEntity::new, org.bukkit.entity.EntityType.PARROT);
    public static final EntityType<LivingEntity> VILLAGER = register(LivingEntity::new, org.bukkit.entity.EntityType.VILLAGER);
    public static final EntityType<Entity> ENDER_CRYSTAL = register(Entity::new, org.bukkit.entity.EntityType.ENDER_CRYSTAL);
    public static final EntityType<LivingEntity> TURTLE = register(LivingEntity::new, org.bukkit.entity.EntityType.TURTLE);
    public static final EntityType<LivingEntity> PHANTOM = register(LivingEntity::new, org.bukkit.entity.EntityType.PHANTOM);
    public static final EntityType<Entity> TRIDENT = register(Entity::new, org.bukkit.entity.EntityType.TRIDENT);
    public static final EntityType<LivingEntity> COD = register(LivingEntity::new, org.bukkit.entity.EntityType.COD);
    public static final EntityType<LivingEntity> SALMON = register(LivingEntity::new, org.bukkit.entity.EntityType.SALMON);
    public static final EntityType<LivingEntity> PUFFERFISH = register(LivingEntity::new, org.bukkit.entity.EntityType.PUFFERFISH);
    public static final EntityType<LivingEntity> TROPICAL_FISH = register(LivingEntity::new, org.bukkit.entity.EntityType.TROPICAL_FISH);
    public static final EntityType<LivingEntity> DROWNED = register(LivingEntity::new, org.bukkit.entity.EntityType.DROWNED);
    public static final EntityType<LivingEntity> DOLPHIN = register(LivingEntity::new, org.bukkit.entity.EntityType.DOLPHIN);
    public static final EntityType<LivingEntity> CAT = register(LivingEntity::new, org.bukkit.entity.EntityType.CAT);
    public static final EntityType<LivingEntity> PANDA = register(LivingEntity::new, org.bukkit.entity.EntityType.PANDA);
    public static final EntityType<LivingEntity> PILLAGER = register(LivingEntity::new, org.bukkit.entity.EntityType.PILLAGER);
    public static final EntityType<LivingEntity> RAVAGER = register(LivingEntity::new, org.bukkit.entity.EntityType.RAVAGER);
    public static final EntityType<LivingEntity> TRADER_LLAMA = register(LivingEntity::new, org.bukkit.entity.EntityType.TRADER_LLAMA);
    public static final EntityType<LivingEntity> WANDERING_TRADER = register(LivingEntity::new, org.bukkit.entity.EntityType.WANDERING_TRADER);
    public static final EntityType<LivingEntity> FOX = register(LivingEntity::new, org.bukkit.entity.EntityType.FOX);
    public static final EntityType<LivingEntity> BEE = register(LivingEntity::new, org.bukkit.entity.EntityType.BEE);
    public static final EntityType<LivingEntity> HOGLIN = register(LivingEntity::new, org.bukkit.entity.EntityType.HOGLIN);
    public static final EntityType<LivingEntity> PIGLIN = register(LivingEntity::new, org.bukkit.entity.EntityType.PIGLIN);
    public static final EntityType<LivingEntity> STRIDER = register(LivingEntity::new, org.bukkit.entity.EntityType.STRIDER);
    public static final EntityType<LivingEntity> ZOGLIN = register(LivingEntity::new, org.bukkit.entity.EntityType.ZOGLIN);
    public static final EntityType<LivingEntity> PIGLIN_BRUTE = register(LivingEntity::new, org.bukkit.entity.EntityType.PIGLIN_BRUTE);
    public static final EntityType<LivingEntity> AXOLOTL = register(LivingEntity::new, org.bukkit.entity.EntityType.AXOLOTL);
    public static final EntityType<Entity> GLOW_ITEM_FRAME = register(Entity::new, org.bukkit.entity.EntityType.GLOW_ITEM_FRAME);
    public static final EntityType<LivingEntity> GLOW_SQUID = register(LivingEntity::new, org.bukkit.entity.EntityType.GLOW_SQUID);
    public static final EntityType<LivingEntity> GOAT = register(LivingEntity::new, org.bukkit.entity.EntityType.GOAT);
    public static final EntityType<Entity> MARKER = register(Entity::new, org.bukkit.entity.EntityType.MARKER);
    public static final EntityType<LivingEntity> ALLAY = register(LivingEntity::new, org.bukkit.entity.EntityType.ALLAY);
    public static final EntityType<Entity> CHEST_BOAT = register(Entity::new, org.bukkit.entity.EntityType.CHEST_BOAT);
    public static final EntityType<LivingEntity> FROG = register(LivingEntity::new, org.bukkit.entity.EntityType.FROG);
    public static final EntityType<LivingEntity> TADPOLE = register(LivingEntity::new, org.bukkit.entity.EntityType.TADPOLE);
    public static final EntityType<LivingEntity> WARDEN = register(LivingEntity::new, org.bukkit.entity.EntityType.WARDEN);
    public static final EntityType<LivingEntity> CAMEL = register(LivingEntity::new, org.bukkit.entity.EntityType.CAMEL);
    public static final EntityType<Entity> BLOCK_DISPLAY = register(Entity::new, org.bukkit.entity.EntityType.BLOCK_DISPLAY);
    public static final EntityType<Entity> INTERACTION = register(Entity::new, org.bukkit.entity.EntityType.INTERACTION);
    public static final EntityType<Entity> ITEM_DISPLAY = register(Entity::new, org.bukkit.entity.EntityType.ITEM_DISPLAY);
    public static final EntityType<LivingEntity> SNIFFER = register(LivingEntity::new, org.bukkit.entity.EntityType.SNIFFER);
    public static final EntityType<Entity> TEXT_DISPLAY = register(Entity::new, org.bukkit.entity.EntityType.TEXT_DISPLAY);
    public static final EntityType<Entity> FISHING_HOOK = register(Entity::new, org.bukkit.entity.EntityType.FISHING_HOOK);
    public static final EntityType<Entity> LIGHTNING = register(Entity::new, org.bukkit.entity.EntityType.LIGHTNING);
    public static final EntityType<Player> PLAYER = register(Player::new, org.bukkit.entity.EntityType.PLAYER);

    private static final EntityType<Entity> UNKNOWN1 = registerUnknown(Entity::new);
    private static final EntityType<LivingEntity> UNKNOWN2 = registerUnknown(LivingEntity::new);


    public static EntityType<Entity> findEntityType(org.bukkit.entity.Entity entity) {
        EntityType<?> entityType = BukkitHelper.getCustomEntityType(entity);
        if (entityType != null) {
            // noinspection unchecked
            return (EntityType<Entity>) entityType;
        }
        return UNKNOWN1;
    }

    public static EntityType<LivingEntity> findEntityType(org.bukkit.entity.LivingEntity entity) {
        EntityType<?> entityType = BukkitHelper.getCustomEntityType(entity);
        if (entityType != null) {
            // noinspection unchecked
            return (EntityType<LivingEntity>) entityType;
        }
        return UNKNOWN2;
    }

    public static <T extends Entity> EntityType<T> register(EntityType.Factory<T> factory, org.bukkit.entity.EntityType entityType) {
        return EntityType.register(new ResourceLocation("minecraft", entityType.getName()), new EntityType<>(factory) {
            @Override
            public org.bukkit.entity.EntityType asBukkit() {
                return entityType;
            }
        });
    }

    public static <T extends Entity> EntityType<T> registerUnknown(EntityType.Factory<T> factory) {
        return EntityType.register(new ResourceLocation("minecraft", "unknown"), new EntityType<>(factory));
    }
}

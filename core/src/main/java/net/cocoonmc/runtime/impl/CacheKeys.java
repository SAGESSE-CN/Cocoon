package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.utils.SimpleAssociatedKey;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.LivingEntity;
import net.cocoonmc.core.world.entity.Player;

public class CacheKeys {

    public static final SimpleAssociatedKey<BlockData> BLOCK_DATA_KEY = SimpleAssociatedKey.of("BlockData", BlockData.class);

    public static final SimpleAssociatedKey<Entity> ENTITY_KEY = SimpleAssociatedKey.of("Entity", Entity.class);
    public static final SimpleAssociatedKey<LivingEntity> LIVING_ENTITY_KEY = SimpleAssociatedKey.of("LivingEntity", LivingEntity.class);
    public static final SimpleAssociatedKey<Player> PLAYER_KEY = SimpleAssociatedKey.of("Player", Player.class);

    public static final SimpleAssociatedKey<Level> LEVEL_KEY = SimpleAssociatedKey.of("Level", Level.class);


}

package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.utils.SimpleAssociatedKey;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.chunk.Chunk;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.LivingEntity;
import net.cocoonmc.core.world.entity.Player;

public class ConstantKeys {

    //public static final SimpleAssociatedKey<BlockData> BLOCK_DATA_KEY = SimpleAssociatedKey.of(BlockData.class);
    public static final SimpleAssociatedKey<Chunk> CHUNK_DATA_KEY = SimpleAssociatedKey.of(Chunk.class);

    public static final SimpleAssociatedKey<Entity> ENTITY_KEY = SimpleAssociatedKey.of(Entity.class);
    public static final SimpleAssociatedKey<LivingEntity> LIVING_ENTITY_KEY = SimpleAssociatedKey.of(LivingEntity.class);
    public static final SimpleAssociatedKey<Player> PLAYER_KEY = SimpleAssociatedKey.of(Player.class);

    public static final SimpleAssociatedKey<Level> LEVEL_KEY = SimpleAssociatedKey.of(Level.class);

    public static final ResourceLocation NETWORK_KEY = new ResourceLocation(Constants.NETWORK_KEY);
}

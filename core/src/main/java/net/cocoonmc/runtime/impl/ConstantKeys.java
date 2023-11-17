package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.utils.SimpleAssociatedKey;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.chunk.Chunk;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.LivingEntity;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.NamespacedKey;

public class ConstantKeys {

    public static final SimpleAssociatedKey<Entity> ENTITY_KEY = SimpleAssociatedKey.of(Entity.class);
    public static final SimpleAssociatedKey<LivingEntity> LIVING_ENTITY_KEY = SimpleAssociatedKey.of(LivingEntity.class);
    public static final SimpleAssociatedKey<Player> PLAYER_KEY = SimpleAssociatedKey.of(Player.class);

    public static final NamespacedKey CACHE_KEY = BukkitHelper.getKey("cocoon", "chunk");
    public static final NamespacedKey ENTITY_TAG_KEY = BukkitHelper.getKey("cocoon", "tag");
    public static final NamespacedKey ENTITY_TYPE_KEY = BukkitHelper.getKey("cocoon", "type");

    public static final ResourceLocation NETWORK_KEY = new ResourceLocation("cocoon", "play");
}

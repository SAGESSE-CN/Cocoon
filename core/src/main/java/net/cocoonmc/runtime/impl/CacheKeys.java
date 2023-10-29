package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.utils.SimpleAssociatedKey;

public class CacheKeys {

    public static final SimpleAssociatedKey<BlockEntity> BLOCK_ENTITY_KEY = SimpleAssociatedKey.of("BlockEntity", BlockEntity.class);

}

package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.block.BlockEntity;

import java.util.WeakHashMap;

public class Caches {

    public static final WeakHashMap<Object, BlockEntity> STATE_TO_BLOCK_ENTITY = new WeakHashMap<>();
}

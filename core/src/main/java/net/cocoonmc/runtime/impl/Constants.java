package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.utils.SimpleAssociatedKey;

public class Constants {

    public static final String ITEM_REDIRECTED_KEY = "__redirected_id__";

    public static final String BLOCK_REDIRECTED_ID_KEY = "id";
    public static final String BLOCK_REDIRECTED_TAG_KEY = "tag";
    public static final String BLOCK_REDIRECTED_STATE_KEY = "state";

    // base64("{textures:{SKIN:{\"url":\"\",__redirected_block_")
    public static final String BLOCK_REDIRECTED_DATA_PREFIX = "e3RleHR1cmVzOntTS0lOOnsidXJsIjoiIixfX3JlZGlyZWN0ZWRfYmxvY2tfX";
    public static final String BLOCK_REDIRECTED_DATA_FMT = "{textures:{SKIN:{\"url\":\"\",__redirected_block__:%s,__block_redirected__:\"\"}}}";


}

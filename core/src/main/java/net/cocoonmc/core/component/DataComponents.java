package net.cocoonmc.core.component;

import com.mojang.serialization.Codec;
import net.cocoonmc.core.resources.ResourceLocation;

public class DataComponents {

    public static final DataComponentType<String> REDIRECTED_ITEM_ID = register("redirected_id", "__redirected_id__", Codec.STRING);

    public static <T> DataComponentType<T> register(String id, String tagName, Codec<T> codec) {
        ResourceLocation key = new ResourceLocation(id);
        return DataComponentType.register(key, new DataComponentType<>(tagName, codec));
    }
}

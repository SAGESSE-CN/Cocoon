package net.cocoonmc.core.component;

import com.mojang.serialization.Codec;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.resources.ResourceLocation;

import java.util.HashMap;

public class DataComponentType<T> {

    private static final HashMap<ResourceLocation, DataComponentType<?>> KEYED_COMPONENT_TYPES = new HashMap<>();

    private ResourceLocation registryName;

    private final String tagName;
    private final Codec<T> codec;

    public DataComponentType(String tagName, Codec<T> codec) {
        this.tagName = tagName;
        this.codec = codec;
    }

    public String getTagName() {
        return tagName;
    }

    public Codec<T> getCodec() {
        return codec;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public static <T> DataComponentType<T> register(ResourceLocation registryName, DataComponentType<T> componentType) {
        componentType.registryName = registryName;
        KEYED_COMPONENT_TYPES.put(registryName, componentType);
        Cocoon.API.ITEM.registerDataComponentType(componentType);
        return componentType;
    }
}

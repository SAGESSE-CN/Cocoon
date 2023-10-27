package net.cocoonmc.core.block;

import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.utils.BukkitUtils;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;

public class Blocks {

    private static final HashMap<ResourceLocation, Block> KEYED_BLOCKS = new HashMap<>();
    private static final HashMap<String, Block> NAMED_BLOCKS = new HashMap<>();

    public static Block AIR = register(Material.AIR);

    public static Block byId(String id) {
        return NAMED_BLOCKS.computeIfAbsent(id, id1 -> byKey(new ResourceLocation(id1)));
    }

    public static Block byKey(ResourceLocation key) {
        return KEYED_BLOCKS.get(key);
    }

    public static Block register(Material material) {
        ResourceLocation key = ResourceLocation.of(material.getKey());
        Block block = KEYED_BLOCKS.get(key);
        if (block != null) {
            return block;
        }
        Block.Properties properties = new Block.Properties();
        properties.material(material);
        return register(key, new Block(properties));
    }

    public static Block register(ResourceLocation key, Block block) {
        block.setKey(key);
        KEYED_BLOCKS.put(key, block);
        return block;
    }

    static {
        Arrays.stream(Material.values()).filter(Material::isBlock).forEach(Blocks::register);
    }
}

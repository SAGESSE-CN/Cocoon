package net.cocoonmc.core.block;

import net.cocoonmc.core.resources.ResourceLocation;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;

public class Blocks {

    private static final HashMap<String, Block> NAMED_BLOCKS = new HashMap<>();

    public static Block AIR = register(Material.AIR);

    public static Block byId(String id) {
        return NAMED_BLOCKS.computeIfAbsent(id, id1 -> Block.byKey(new ResourceLocation(id1)));
    }

    public static Block register(Material material) {
        ResourceLocation key = ResourceLocation.of(material.getKey());
        Block block = Block.byKey(key);
        if (block != null) {
            return block;
        }
        Block.Properties properties = new Block.Properties();
        properties.material(material);
        return Block.register(key, new Block(properties));
    }

    static {
        Arrays.stream(Material.values()).filter(Material::isBlock).forEach(Blocks::register);
    }
}

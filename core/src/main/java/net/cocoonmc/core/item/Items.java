package net.cocoonmc.core.item;

import net.cocoonmc.core.resources.ResourceLocation;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;

public class Items {

    private static final HashMap<String, Item> NAMED_ITEMS = new HashMap<>();

    public static Item AIR = register(Material.AIR);

    public static Item PLAYER_HEAD = register(Material.PLAYER_HEAD);

    public static Item PAPER = register(Material.PAPER);
    public static Item WHITE_BANNER = register(Material.WHITE_BANNER);
    public static Item FLOWER_BANNER_PATTERN = register(Material.FLOWER_BANNER_PATTERN);


    public static void init() {
    }

    public static Item byId(String id) {
        return NAMED_ITEMS.computeIfAbsent(id, id1 -> Item.byKey(new ResourceLocation(id1)));
    }

    public static Item register(Material material) {
        ResourceLocation key = ResourceLocation.of(material.getKey());
        Item item = Item.byKey(key);
        if (item != null) {
            return item;
        }
        Item.Properties properties = new Item.Properties();
        properties.material(material);
        return Item.register(key, new Item(properties));
    }


    static {
        Arrays.stream(Material.values()).filter(Material::isItem).forEach(Items::register);
    }
}

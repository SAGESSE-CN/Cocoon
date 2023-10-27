package net.cocoonmc.core.item;

import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.utils.BukkitUtils;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;

public class Items {

    private static final HashMap<ResourceLocation, Item> KEYED_ITEMS = new HashMap<>();
    private static final HashMap<String, Item> NAMED_ITEMS = new HashMap<>();

    public static Item AIR = register(Material.AIR);

    public static Item PLAYER_HEAD = register(Material.PLAYER_HEAD);

    public static Item PAPER = register(Material.PAPER);
    public static Item WHITE_BANNER = register(Material.WHITE_BANNER);
    public static Item FLOWER_BANNER_PATTERN = register(Material.FLOWER_BANNER_PATTERN);


    public static void init() {
    }

    public static Item byId(String id) {
        return NAMED_ITEMS.computeIfAbsent(id, id1 -> byKey(new ResourceLocation(id1)));
    }

    public static Item byKey(ResourceLocation key) {
        return KEYED_ITEMS.get(key);
    }

    public static Item register(Material material) {
        ResourceLocation key = ResourceLocation.of(material.getKey());
        Item item = KEYED_ITEMS.get(key);
        if (item != null) {
            return item;
        }
        Item.Properties properties = new Item.Properties();
        properties.material(material);
        return register(key, new Item(properties));
    }

    public static Item register(ResourceLocation key, Item item) {
        item.setKey(key);
        KEYED_ITEMS.put(key, item);
        return item;
    }

    static {
        Arrays.stream(Material.values()).filter(Material::isItem).forEach(Items::register);
    }
}

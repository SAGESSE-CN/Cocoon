package net.cocoonmc;

import net.cocoonmc.core.block.Blocks;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.runtime.IBlockFactory;
import net.cocoonmc.runtime.ICacheFactory;
import net.cocoonmc.runtime.IItemFactory;
import net.cocoonmc.runtime.IMenuFactory;
import net.cocoonmc.runtime.IRuntime;
import net.cocoonmc.runtime.IRuntimeLoader;
import net.cocoonmc.runtime.ITagFactory;
import net.cocoonmc.runtime.impl.CacheFactory;
import net.cocoonmc.runtime.impl.ItemEventListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Cocoon {

    private static JavaPlugin PLUGIN;
    private static PluginManager PLUGIN_MANAGER;

    private static final IRuntime RUNTIME = IRuntimeLoader.load();

    private Cocoon() {
    }

    public static void init(JavaPlugin plugin) {
        PLUGIN = plugin;
        PLUGIN_MANAGER = plugin.getServer().getPluginManager();
        PLUGIN_MANAGER.registerEvents(new ItemEventListener(), plugin);

        Items.init();
        Blocks.init();
    }

    public static JavaPlugin getPlugin() {
        return PLUGIN;
    }

    public static class API {

        public static final ITagFactory TAG = RUNTIME.getTag();
        public static final IItemFactory ITEM = RUNTIME.getItem();
        public static final IBlockFactory BLOCK = RUNTIME.getBlock();
        public static final IMenuFactory MENU = RUNTIME.getMenu();
        public static final ICacheFactory CACHE = new CacheFactory();

    }
}

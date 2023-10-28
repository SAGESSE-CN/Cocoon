package net.cocoonmc;

import net.cocoonmc.core.item.Items;
import net.cocoonmc.runtime.IBlockFactory;
import net.cocoonmc.runtime.IItemFactory;
import net.cocoonmc.runtime.IMenuFactory;
import net.cocoonmc.runtime.IRuntime;
import net.cocoonmc.runtime.IRuntimeLoader;
import net.cocoonmc.runtime.ITagFactory;
import net.cocoonmc.runtime.impl.ItemEventHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Cocoon {

    private static final Cocoon INSTANCE = new Cocoon();
    private static final IRuntime RUNTIME = IRuntimeLoader.load();

    private JavaPlugin plugin;
    private PluginManager pluginManager;

    public static Cocoon getInstance() {
        return INSTANCE;
    }

    public void init(JavaPlugin plugin) {
        this.plugin = plugin;
        this.pluginManager = plugin.getServer().getPluginManager();
        this.pluginManager.registerEvents(new ItemEventHandler(), plugin);
//        if (isInited) {
//            return;
//        }
//        isInited = true;
        // ..
        Items.init();
    }

    public void release() {
    }


    public JavaPlugin getPlugin() {
        return plugin;
    }

    public static class API {

        public static final ITagFactory TAG = RUNTIME.getTag();
        public static final IItemFactory ITEM = RUNTIME.getItem();
        public static final IBlockFactory BLOCK = RUNTIME.getBlock();
        public static final IMenuFactory MENU = RUNTIME.getMenu();
    }
}

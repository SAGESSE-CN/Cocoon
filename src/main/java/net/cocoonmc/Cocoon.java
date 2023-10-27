package net.cocoonmc;

import net.cocoonmc.core.item.Items;
import net.cocoonmc.impl.ItemEventHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Cocoon {

    private static final Cocoon INSTANCE = new Cocoon();

    public static Cocoon getInstance() {
        return INSTANCE;
    }

    private JavaPlugin plugin;
//    JavaPlugin plugin

    public void install(JavaPlugin plugin) {
        this.plugin = plugin;
        PluginManager manager = plugin.getServer().getPluginManager();
        manager.registerEvents(new ItemEventHandler(), plugin);
//        if (isInited) {
//            return;
//        }
//        isInited = true;
        // ..
        Items.init();
    }

    public void uninstall() {
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}

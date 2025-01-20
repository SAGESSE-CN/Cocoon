package net.cocoonmc;

import net.cocoonmc.core.block.Blocks;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.core.network.PacketTransformer;
import net.cocoonmc.runtime.IBlockFactory;
import net.cocoonmc.runtime.ICacheFactory;
import net.cocoonmc.runtime.ICodecFactory;
import net.cocoonmc.runtime.IItemFactory;
import net.cocoonmc.runtime.IMenuFactory;
import net.cocoonmc.runtime.INetworkFactory;
import net.cocoonmc.runtime.IRuntime;
import net.cocoonmc.runtime.IRuntimeLoader;
import net.cocoonmc.runtime.ITagFactory;
import net.cocoonmc.runtime.impl.BlockDataListener;
import net.cocoonmc.runtime.impl.CacheFactory;
import net.cocoonmc.runtime.impl.ChunkDataListener;
import net.cocoonmc.runtime.impl.EntityDataListener;
import net.cocoonmc.runtime.impl.ItemDataListener;
import net.cocoonmc.runtime.impl.LevelData;
import net.cocoonmc.runtime.impl.PacketDataListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Cocoon {

    private static JavaPlugin PLUGIN;

    private static final IRuntime RUNTIME = IRuntimeLoader.load();

    private static void registerEvents(PluginManager manager, JavaPlugin plugin) {
        manager.registerEvents(new ItemDataListener(), plugin);
        manager.registerEvents(new BlockDataListener(), plugin);
        manager.registerEvents(new EntityDataListener(), plugin);
        manager.registerEvents(new ChunkDataListener(), plugin);
        manager.registerEvents(new PacketDataListener(), plugin);
    }

    public static void onLoad(JavaPlugin plugin) {
        PLUGIN = plugin;
    }

    public static void onEnable() {
        Items.init();
        Blocks.init();
        LevelData.open();
        registerEvents(PLUGIN.getServer().getPluginManager(), PLUGIN);
        API.TRANSFORMER.enable();
    }

    public static void onDisable() {
        API.TRANSFORMER.disable();
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
        public static final INetworkFactory NETWORK = RUNTIME.getNetwork();

        public static final ICodecFactory CODEC = RUNTIME.getCodec();

        public static final PacketTransformer TRANSFORMER = new PacketTransformer();
    }
}

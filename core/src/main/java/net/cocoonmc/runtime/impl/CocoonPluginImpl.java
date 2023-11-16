package net.cocoonmc.runtime.impl;

import net.cocoonmc.Cocoon;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class CocoonPluginImpl extends JavaPlugin {

    @Override
    public void onEnable() {
        Cocoon.onEnable();
    }

    @Override
    public void onDisable() {
        Cocoon.onDisable();
    }
}

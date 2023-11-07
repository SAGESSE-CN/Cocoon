package net.cocoonmc.runtime.impl;

import net.cocoonmc.Cocoon;
import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Logs extends Logger {

    private static final Logger LOGGER = new Logs();

    private Logs() {
        super(Cocoon.class.getSimpleName(), null);
        this.setParent(Bukkit.getServer().getLogger());
        this.setLevel(Level.ALL);
    }

    public static void debug(String message, Object... params) {
        LOGGER.finer(_format(message, params));
    }

    public static void info(String message, Object... params) {
        LOGGER.info(_format(message, params));
    }

    public static void error(String message, Object... params) {
        LOGGER.severe(_format(message, params));
    }

    public static void warn(String message, Object... params) {
        LOGGER.warning(_format(message, params));
    }

    private static String _format(String message, Object... params) {
        return String.format(message.replace("{}", "%s"), params);
    }
}

package net.cocoonmc.runtime.impl;

import net.cocoonmc.Cocoon;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Logs {

    private static final Logger LOGGER = getLogger();

    private static Logger getLogger() {
        Logger logger = Cocoon.getPlugin().getLogger();
        logger.setLevel(Level.ALL);
        return logger;
    }

    public static void trace(String message, Object... params) {
        LOGGER.fine(_format(message, params));
    }

    public static void debug(String message, Object... params) {
        LOGGER.config(_format(message, params));
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

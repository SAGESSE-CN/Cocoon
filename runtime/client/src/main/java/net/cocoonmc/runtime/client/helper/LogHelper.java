package net.cocoonmc.runtime.client.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {

    private static final Logger LOGGER = LogManager.getLogger(PacketHelper.class);

    public static void log(String message) {
        LOGGER.info("cocoon " + message);
    }
}

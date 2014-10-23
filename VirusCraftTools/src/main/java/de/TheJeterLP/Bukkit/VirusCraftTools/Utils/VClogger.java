package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TheJeterLP
 */
public class VClogger {

    private static final Logger logger = VCplugin.inst().getLogger();

    public static void log(MessageType type, String message) {
        if (type.equals(MessageType.INFO)) {
            logger.log(Level.INFO, message);
        } else {
            logger.log(Level.SEVERE,  message);
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}

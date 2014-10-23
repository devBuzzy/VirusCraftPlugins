package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TheJeterLP
 */
public class VClogger {

    private static final Logger logger = VCplugin.inst().getLogger();

    public static void log(MessageType type, String message) {
        if (type == MessageType.DEBUG && !Config.DEBUG.getBoolean()) return;
        
        Level l;
        
        switch (type) {
            case INFO:
                l = Level.INFO;
                break;
            case ERROR:
                l = Level.SEVERE;
                break;
            case DEBUG:
                l = Level.INFO;
                break;
            case NOTHING:
                l = Level.INFO;
                break;
            default:
                l = Level.WARNING;
                break;
        }

        logger.log(l, message);
    }

    public static Logger getLogger() {
        return logger;
    }
}

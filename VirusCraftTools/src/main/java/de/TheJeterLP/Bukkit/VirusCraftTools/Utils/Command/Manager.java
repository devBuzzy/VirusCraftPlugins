package de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command;

import de.TheJeterLP.Bukkit.VirusCraftTools.Addon.Addon;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClogger;

/**
 * @author TheJeterLP
 */
public class Manager {

    public static void registerCommand(Class<? extends BaseCommand> clazz) {
        try {
            BaseCommand e = clazz.newInstance();
            e.prepare();
            e.register();
        } catch (Exception ex) {
            VClogger.log(MessageType.ERROR, "Error registering command " + clazz.getName());
            ex.printStackTrace();
        }
    }
    
    public static void registerCommand(Class<? extends BaseCommand> clazz, Addon addon) {
        try {
            BaseCommand e = clazz.newInstance();
            e.prepare();
            e.register(addon);
        } catch (Exception ex) {
            VClogger.log(MessageType.ERROR, "Error registering command " + clazz.getName());
            ex.printStackTrace();
        }
    }

    public static void registerListener(Class<? extends VClistener> clazz) {
        try {
            VClistener listener = clazz.newInstance();
            listener.register();
        } catch (Exception ex) {
            VClogger.log(MessageType.ERROR, "Error registering listener " + clazz.getName());
            ex.printStackTrace();
        }
    }   
    
    public static void registerListener(Class<? extends VClistener> clazz, Addon addon) {
        try {
            VClistener listener = clazz.newInstance();
            listener.register(addon);
        } catch (Exception ex) {
            VClogger.log(MessageType.ERROR, "Error registering listener " + clazz.getName());
            ex.printStackTrace();
        }
    } 
}

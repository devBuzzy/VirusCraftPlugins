package de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClogger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TheJeterLP
 */
public class Manager {

    public static void registerCommand(Class<? extends Executor> clazz) {
        try {
            Executor e = clazz.newInstance();
            e.prepare();
            if (e.getBukkitCommand() != null)
                e.getBukkitCommand().setExecutor(e);
            else
                VClogger.log(MessageType.ERROR, "BukkitCommand is null or empty! Check " + clazz.getName());
        } catch (Exception ex) {
            VClogger.log(MessageType.ERROR, "Error registering command " + clazz.getName());
            ex.printStackTrace();
        }
    }

    public static void registerCommand(Class<? extends Executor> clazz, JavaPlugin pl) {
        try {
            Executor e = clazz.newInstance();
            e.prepare();
            if (e.getBukkitCommand(pl) != null)
                e.getBukkitCommand(pl).setExecutor(e);
            else
                VClogger.log(MessageType.ERROR, "BukkitCommand is null or empty! Check " + clazz.getName());
        } catch (Exception ex) {
            VClogger.log(MessageType.ERROR, "Error registering command " + clazz.getName() + " for Plugin " + pl.getName());
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

    public static void registerListener(Class<? extends VClistener> clazz, JavaPlugin pl) {
        try {
            VClistener listener = clazz.newInstance();
            listener.register(pl);
        } catch (Exception ex) {
            VClogger.log(MessageType.ERROR, "Error registering listener " + clazz.getName() + " for plugin " + pl.getName());
            ex.printStackTrace();
        }
    }
}

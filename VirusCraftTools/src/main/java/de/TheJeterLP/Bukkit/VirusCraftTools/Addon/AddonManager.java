package de.TheJeterLP.Bukkit.VirusCraftTools.Addon;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClogger;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * @author TheJeterLP
 */
public class AddonManager {

    private static final Map<String, Addon> addons = new HashMap<>();
    private static final PluginManager manager = Bukkit.getPluginManager();

    public static void init() {
        VClogger.getLogger().info("Loading addons...");

        File folder = new File(VCplugin.inst().getDataFolder(), "addons");
        folder.mkdirs();

        if (folder.listFiles().length == 0) {
            VClogger.getLogger().info("No addons installed.");
            return;
        }

        for (File f : folder.listFiles()) {
            if (!f.getName().toLowerCase().endsWith(".jar") || !f.isFile()) continue;

            VClogger.getLogger().info("Loading addon " + f.getName());
            try {
                Plugin p = manager.loadPlugin(f);
                if (p instanceof Addon) {
                    manager.enablePlugin(p);
                    VClogger.getLogger().info("Loaded addon " + p.getDescription().getName());
                    addons.put(p.getDescription().getName(), (Addon) p);
                } else {
                    VClogger.log(MessageType.ERROR, "The plugin is not an instance of the Addon class!");
                }
            } catch (Exception e) {
                VClogger.log(MessageType.ERROR, "Error in initializing plugin. " + e.getMessage());
            }
        }
    }

    public static void disable() {
        VClogger.getLogger().info("Disabling addons...");
        for (String s : addons.keySet()) {
            Addon a = addons.get(s);
            manager.disablePlugin(a);
        }
        addons.clear();
    }

}

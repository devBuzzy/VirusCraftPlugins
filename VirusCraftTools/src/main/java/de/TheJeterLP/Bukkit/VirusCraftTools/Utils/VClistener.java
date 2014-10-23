package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * @author TheJeterLP
 */
public abstract class VClistener implements Listener {

    public void register() {
        VCplugin.inst().getServer().getPluginManager().registerEvents(this, VCplugin.inst());
    }

    public void register(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}

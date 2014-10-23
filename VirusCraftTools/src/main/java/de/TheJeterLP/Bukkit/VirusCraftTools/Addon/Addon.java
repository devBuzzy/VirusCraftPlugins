package de.TheJeterLP.Bukkit.VirusCraftTools.Addon;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TheJeterLP
 */
public abstract class Addon extends JavaPlugin {
    
    public abstract void onEnable();
    
    public abstract void onDisable();
}

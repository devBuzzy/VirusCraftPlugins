/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thejeterlp.bukkit.ironore;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TheJeterLP
 */
public class Main extends JavaPlugin {

    private static Main INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
    }

    public static Main getInstance() {
        return INSTANCE;
    }

}

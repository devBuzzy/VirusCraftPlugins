/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TheJeterLP
 */
public class FileUpdate {

    public static void reload(final JavaPlugin pl) {
        VCplugin.inst().getServer().getScheduler().scheduleSyncDelayedTask(VCplugin.inst(), new Runnable() {

            @Override
            public void run() {
                try {
                    final File plFile = new File(pl.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                    final PluginManager manager = VCplugin.inst().getServer().getPluginManager();
                    manager.disablePlugin(pl);

                    final List<Plugin> plugins = getPluginList();
                    final Map<String, Plugin> lookupNames = getLookUpNames();
                    final SimpleCommandMap commandMap = Utils.getCommandMap();
                    final Map<String, Command> knownCommands = getKnownCommands();

                    for (int i = 0; i < plugins.size(); i++) {
                        Plugin p = plugins.get(i);
                        if (p.getName().equals(pl.getName())) {
                            plugins.remove(p);
                        }
                    }

                    if (lookupNames.containsKey(pl.getName())) {
                        lookupNames.remove(pl.getName());
                    }

                    Iterator<Map.Entry<String, Command>> it = knownCommands.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, Command> entry = it.next();
                        if (entry.getValue() instanceof PluginCommand) {
                            PluginCommand command = (PluginCommand) entry.getValue();
                            if (command.getPlugin().getName().equals(pl.getName())) {
                                command.unregister(commandMap);
                                it.remove();
                            }
                        }
                    }

                    if (plFile.exists() && plFile.isFile()) {
                        final Plugin pluginload = manager.loadPlugin(plFile);
                        final JavaPlugin jpluginLoad = (JavaPlugin) pluginload;
                        jpluginLoad.onLoad();
                        manager.enablePlugin(jpluginLoad);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private static List<Plugin> getPluginList() {
        try {
            final Server server = VCplugin.inst().getServer();
            final SimplePluginManager manager = (SimplePluginManager) server.getPluginManager();
            Field f = manager.getClass().getDeclaredField("plugins");
            f.setAccessible(true);
            return (List<Plugin>) f.get(manager);
        } catch (Exception ex) {
            return null;
        }
    }

    private static Map<String, Plugin> getLookUpNames() {
        try {
            final Server server = VCplugin.inst().getServer();
            final SimplePluginManager manager = (SimplePluginManager) server.getPluginManager();
            final Field field = manager.getClass().getDeclaredField("lookupNames");
            field.setAccessible(true);
            return (Map<String, Plugin>) field.get(manager);
        } catch (Exception ex) {
            return null;
        }
    }

    private static Map<String, Command> getKnownCommands() {
        try {
            Field field = Utils.getCommandMap().getClass().getDeclaredField("knownCommands");
            field.setAccessible(true);
            return (Map<String, Command>) field.get(Utils.getCommandMap());
        } catch (Exception ex) {
            return null;
        }
    }

}

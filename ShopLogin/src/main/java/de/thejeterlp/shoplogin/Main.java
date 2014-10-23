/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thejeterlp.shoplogin;

import de.thejeterlp.shoplogin.command.CommandManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TheJeterLP
 */
public class Main extends JavaPlugin {

    private static Main INSTANCE;
    private static Database db;
    private CommandManager manager;
    private static Chat chat;

    @Override
    public void onEnable() {
        INSTANCE = this;

        if (!setupVault()) {
            getLogger().severe("PermissionsEx and/or Vault could NOT be found. Disabling the plugin...");
            setEnabled(false);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Config.load(this, "config.yml");
        db = new MySQL();
        manager = new CommandManager(this);
        manager.registerClass(Executor.class);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
    }

    @Override
    public void onDisable() {
    }

    public static Main getInstance() {
        return INSTANCE;
    }
    
    public static Chat getChat() {
        return chat;
    }

    public static Database getDB() {
        return db;
    }

    private boolean setupVault() {
        if (!getServer().getPluginManager().isPluginEnabled("Vault") || !getServer().getPluginManager().isPluginEnabled("PermissionsEx")) return false;
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
        if (chatProvider != null && chatProvider.getPlugin() != null && chatProvider.getPlugin().isEnabled()) {
            chat = chatProvider.getProvider();
            return true;
        } else {
            return false;
        }

    }

}

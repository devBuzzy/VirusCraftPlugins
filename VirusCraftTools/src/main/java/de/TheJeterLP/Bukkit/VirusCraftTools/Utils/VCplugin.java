package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import de.TheJeterLP.Bukkit.VirusCraftTools.Addon.AddonManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Bar.BarTool;
import de.TheJeterLP.Bukkit.VirusCraftTools.Chest.VirtualChestManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Database.ConnectionManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Listeners.*;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Manager;
import de.TheJeterLP.Bukkit.VirusCraftTools.commands.Essential.Reload;
import de.TheJeterLP.Bukkit.VirusCraftTools.commands.Party.PartyCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.commands.chest.Chest;
import de.TheJeterLP.Bukkit.VirusCraftTools.commands.chest.ClearChest;
import java.sql.SQLException;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TheJeterLP
 */
public abstract class VCplugin extends JavaPlugin {

    private VirtualChestManager chestManager;
    private ConnectionManager connmanager;
    
    private static VCplugin INSTANCE;
    private static Chat chat;
    private static Permission perm;

    public static VCplugin inst() {
        return INSTANCE;
    }
  
    public abstract void onStart();

    public abstract void onStop();

    @Override
    public void onEnable() {
        INSTANCE = this;
        Config.load();
        setupVault();
        setupDatabase();
        setupManagers();
        startBroadcast();
        registerCommands();
        registerEvents();
        onStart();
    }

    @Override
    public void onDisable() {
        AddonManager.disable();
        getServer().getScheduler().cancelTasks(this);
        VClogger.log(MessageType.INFO, "Saved " + getChestManager().save() + " chests.");

        try {
            getConnectionManager().closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        onStop();
    }

    private void registerCommands() {
        Manager.registerCommand(Chest.class);
        Manager.registerCommand(ClearChest.class);
        Manager.registerCommand(Reload.class);
        Manager.registerCommand(de.TheJeterLP.Bukkit.VirusCraftTools.commands.Essential.Announcer.class);
        Manager.registerCommand(PartyCommand.class);
    }

    private void registerEvents() {
        Manager.registerListener(PlayerListener.class);
        Manager.registerListener(CommandListener.class);
        Manager.registerListener(HubListener.class);
    }

    public ConnectionManager getConnectionManager() {
        return connmanager;
    }

    public VirtualChestManager getChestManager() {
        return chestManager;
    }

    private void setupDatabase() {
        connmanager = new ConnectionManager();
    }

    private void setupManagers() {
        chestManager = new VirtualChestManager();
        BarTool.init();
        AddonManager.init();
    }

    public Chat getChat() {
        return chat;
    }

    public Permission getPermissions() {
        return perm;
    }

    private boolean setupVault() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
        if (chatProvider != null && chatProvider.getPlugin() != null && chatProvider.getPlugin().isEnabled()) {
            chat = chatProvider.getProvider();
        }

        RegisteredServiceProvider<Permission> permProvider = getServer().getServicesManager().getRegistration(Permission.class);
        if (permProvider != null && permProvider.getPlugin() != null && permProvider.getPlugin().isEnabled()) {
            perm = permProvider.getProvider();
        }
        return true;
    }

    private void startBroadcast() {
        int time = Config.ANNOUNCER_TIME.getInt();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Announcer(), time, time);
    }
}

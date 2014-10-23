package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import de.TheJeterLP.Bukkit.VirusCraftTools.Bar.BarTool;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Manager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Listeners.*;
import de.TheJeterLP.Bukkit.VirusCraftTools.commands.Essential.*;
import de.TheJeterLP.Bukkit.VirusCraftTools.commands.Management.tools;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TheJeterLP
 */
public abstract class VCplugin extends JavaPlugin {

    private static VCplugin INSTANCE;
    private static Chat CHAT;
    private static Permission PERM;

    public static VCplugin inst() {
        return INSTANCE;
    }

    public abstract void onStart();

    public abstract void onStop();

    @Override
    public void onEnable() {
        INSTANCE = this;
        Config.load();
        
        if (!setupVault()) {
            VClogger.log(MessageType.ERROR, "---VAULT WAS NOT FOUND! DISABLING THE PLUGIN---");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (Utils.isSevenPlus()) {
            getLogger().severe("1.7+! Enabling bar...");
            BarTool.init();
        } else {
            getLogger().severe("Not 1.7+! Disabling bar...");
        }

        if (Config.BLOCKTAB.getBoolean()) {
            new TabListener().init();
        }
        
        registerCommands();
        registerEvents();
        startBroadcast();
        onStart();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        onStop();
    }

    private void registerCommands() {
        Manager.registerCommand(tools.class);
        Manager.registerCommand(CW.class);
        Manager.registerCommand(de.TheJeterLP.Bukkit.VirusCraftTools.commands.Essential.Announcer.class);
        if (Utils.isSevenPlus()) Manager.registerCommand(UUID.class);
    }

    private void registerEvents() {
        Manager.registerListener(PlayerListener.class);
        Manager.registerListener(CommandListener.class);
    }

    public Chat getChat() {
        return CHAT;
    }

    public Permission getPermissions() {
        return PERM;
    }

    private boolean setupVault() {
        try {
            RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
            if (chatProvider != null) {
                CHAT = chatProvider.getProvider();
            }

            RegisteredServiceProvider<Permission> permProvider = getServer().getServicesManager().getRegistration(Permission.class);
            if (chatProvider != null) {
                PERM = permProvider.getProvider();
            }
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private void startBroadcast() {
        int time = Config.ANNOUNCER_TIME.getInt();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Announcer(), time, time);
    }
}

package de.TheJeterLP.Bukkit.SurvivalGames;

import de.TheJeterLP.Bukkit.SurvivalGames.util.SettingsManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageManager;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.GameManager;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.Game;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.LobbyManager;
import de.TheJeterLP.Bukkit.SurvivalGames.commands.CommandHandler;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.TheJeterLP.Bukkit.SurvivalGames.events.ChestListener;
import de.TheJeterLP.Bukkit.SurvivalGames.events.PlayerListener;
import de.TheJeterLP.Bukkit.SurvivalGames.events.SpectatorListener;
import de.TheJeterLP.Bukkit.SurvivalGames.util.ItemGenerator;
import de.TheJeterLP.Bukkit.VirusCraftTools.Addon.Addon;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Manager;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class SurvivalGames extends Addon {

    private static SurvivalGames INSTANCE;

    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        SettingsManager.getInstance().saveSpawns();
        SettingsManager.getInstance().saveSystemConfig();
        for (Game g : GameManager.getInstance().getGames()) {
            try {
                g.disable();
            } catch (Exception e) {
            }
        }

        getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " has now been disabled and reset");
    }

    public static SurvivalGames getInstance() {
        return INSTANCE;
    }

    public void onEnable() {
        INSTANCE = this;
        getCommand("survivalgames").setExecutor(new CommandHandler(this));
        SettingsManager.getInstance().setup();
        MessageManager.getInstance().setup();
        GameManager.getInstance().setup();
        LobbyManager.getInstance().setup();
        ItemGenerator.getInstance().load();
        Manager.registerListener(ChestListener.class, this);
        Manager.registerListener(PlayerListener.class, this);
        Manager.registerListener(SpectatorListener.class, this);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (GameManager.getInstance().getBlockGameId(p.getLocation()) != -1) {
                p.teleport(SettingsManager.getInstance().getLobbySpawn());
            }
        }
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEdit instanceof WorldEditPlugin) {
            return (WorldEditPlugin) worldEdit;
        } else {
            return null;
        }
    }

    public static void $(String msg) {
        INSTANCE.getLogger().log(Level.INFO, msg);
    }

    public static void $(Level l, String msg) {
        INSTANCE.getLogger().log(l, msg);
    }
}

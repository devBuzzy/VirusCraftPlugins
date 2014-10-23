package de.TheJeterLP.Bukkit.VillagerHunt.Bukkit;

import de.TheJeterLP.Bukkit.VillagerHunt.Arena.Arena;
import de.TheJeterLP.Bukkit.VillagerHunt.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VillagerHunt.commands.CommandHandler;
import de.TheJeterLP.Bukkit.VillagerHunt.listener.BukkitBlockListener;
import de.TheJeterLP.Bukkit.VillagerHunt.listener.BukkitDamageEvent;
import de.TheJeterLP.Bukkit.VillagerHunt.listener.BukkitDeathEvent;
import de.TheJeterLP.Bukkit.VillagerHunt.listener.BukkitMessageEvent;
import de.TheJeterLP.Bukkit.VillagerHunt.listener.BukkitMobSpawnListener;
import de.TheJeterLP.Bukkit.VillagerHunt.listener.BukkitQuitEvent;
import de.TheJeterLP.Bukkit.VillagerHunt.listener.BukkitTargetListener;
import de.TheJeterLP.Bukkit.VillagerHunt.listener.CommandListener;
import de.TheJeterLP.Bukkit.VillagerHunt.listener.LobbySignListener;
import de.TheJeterLP.Bukkit.VirusCraftTools.Addon.Addon;
import de.TheJeterLP.Bukkit.VirusCraftTools.Database.Games.Database;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Manager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Game;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
import java.sql.SQLException;

public class VillagerHunt extends Addon { 

    private static VillagerHunt INSTANCE;
    protected static MessageManager msgManager = null;
    protected static Database db = null;

    @Override
    public void onEnable() {
        INSTANCE = this;
        msgManager = new MessageManager(Game.VILLAGERHUNT);
        db = new Database(Game.VILLAGERHUNT);
        try {
            db.setup();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        ArenaManager.setupArenas();
        Manager.registerListener(BukkitBlockListener.class, this);
        Manager.registerListener(BukkitDamageEvent.class, this);
        Manager.registerListener(BukkitDeathEvent.class, this);
        Manager.registerListener(BukkitQuitEvent.class, this);
        Manager.registerListener(LobbySignListener.class, this);
        Manager.registerListener(BukkitMessageEvent.class, this);
        Manager.registerListener(CommandListener.class, this);
        Manager.registerListener(BukkitTargetListener.class, this);
        Manager.registerListener(BukkitMobSpawnListener.class, this);
        Manager.registerCommand(CommandHandler.class, this);
    }

    @Override
    public void onDisable() {
        for (Arena a : ArenaManager.getArenas()) {
            a.stop("Reload!");
        }
        getServer().getScheduler().cancelTasks(this);
    }

    public static VillagerHunt getInstance() {
        return INSTANCE;
    }

    public static MessageManager getMessageManager() {
        return msgManager;
    }

    public static Database getDB() {
        return db;
    }

}

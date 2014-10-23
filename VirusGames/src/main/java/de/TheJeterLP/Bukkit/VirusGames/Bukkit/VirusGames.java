package de.TheJeterLP.Bukkit.VirusGames.Bukkit;

import de.TheJeterLP.Bukkit.VirusCraftTools.Addon.Addon;
import de.TheJeterLP.Bukkit.VirusCraftTools.Database.Games.Database;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Manager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Game;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
import de.TheJeterLP.Bukkit.VirusGames.Arena.Arena;
import de.TheJeterLP.Bukkit.VirusGames.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VirusGames.Arena.Team;
import de.TheJeterLP.Bukkit.VirusGames.commands.CommandHandler;
import de.TheJeterLP.Bukkit.VirusGames.listener.BukkitBlockListener;
import de.TheJeterLP.Bukkit.VirusGames.listener.BukkitDamageEvent;
import de.TheJeterLP.Bukkit.VirusGames.listener.BukkitDeathEvent;
import de.TheJeterLP.Bukkit.VirusGames.listener.BukkitHungerEvent;
import de.TheJeterLP.Bukkit.VirusGames.listener.BukkitMessageEvent;
import de.TheJeterLP.Bukkit.VirusGames.listener.BukkitMoveListener;
import de.TheJeterLP.Bukkit.VirusGames.listener.BukkitQuitEvent;
import de.TheJeterLP.Bukkit.VirusGames.listener.CommandListener;
import de.TheJeterLP.Bukkit.VirusGames.listener.KitCreateListener;
import de.TheJeterLP.Bukkit.VirusGames.listener.KitSignListener;
import de.TheJeterLP.Bukkit.VirusGames.listener.LobbySignListener;
import de.TheJeterLP.Bukkit.VirusGames.util.Kits;
import java.sql.SQLException;

public class VirusGames extends Addon {

    private static VirusGames INSTANCE;
    protected static MessageManager msgManager = null;
    protected static Database db = null;

    @Override
    public void onEnable() {
        INSTANCE = this;
        db = new Database(Game.VIRUSGAMES);
        msgManager = new MessageManager(Game.VIRUSGAMES);
        try {
            db.setup();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        Kits.getInstance().setup();
        Manager.registerListener(BukkitBlockListener.class, this);
        Manager.registerListener(BukkitDamageEvent.class, this);
        Manager.registerListener(BukkitDeathEvent.class, this);
        Manager.registerListener(BukkitQuitEvent.class, this);
        Manager.registerListener(KitSignListener.class, this);
        Manager.registerListener(LobbySignListener.class, this);
        Manager.registerListener(BukkitHungerEvent.class, this);
        Manager.registerListener(BukkitMessageEvent.class, this);
        Manager.registerListener(KitCreateListener.class, this);
        Manager.registerListener(CommandListener.class, this);
        Manager.registerListener(BukkitMoveListener.class, this);
        Manager.registerCommand(CommandHandler.class, this);
        ArenaManager.setupArenas();
    }

    @Override
    public void onDisable() {
        for (Arena a : ArenaManager.getArenas()) {
            a.stop(Team.LOBBY);
        }
    }

    public static VirusGames getInstance() {
        return INSTANCE;
    }

    public static MessageManager getMessageManager() {
        return msgManager;
    }

    public static Database getDB() {
        return db;
    }

}

package de.TheJeterLP.Bukkit.CakePoke.Bukkit;

import de.TheJeterLP.Bukkit.CakePoke.Arena.Arena;
import de.TheJeterLP.Bukkit.CakePoke.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.CakePoke.commands.CommandHandler;
import de.TheJeterLP.Bukkit.CakePoke.listener.BukkitBlockListener;
import de.TheJeterLP.Bukkit.CakePoke.listener.BukkitDeathEvent;
import de.TheJeterLP.Bukkit.CakePoke.listener.BukkitMessageEvent;
import de.TheJeterLP.Bukkit.CakePoke.listener.BukkitMoveListener;
import de.TheJeterLP.Bukkit.CakePoke.listener.BukkitQuitEvent;
import de.TheJeterLP.Bukkit.CakePoke.listener.CommandListener;
import de.TheJeterLP.Bukkit.CakePoke.listener.LobbySignListener;
import de.TheJeterLP.Bukkit.VirusCraftTools.Addon.Addon;
import de.TheJeterLP.Bukkit.VirusCraftTools.Database.Games.Database;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Manager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Game;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
import java.sql.SQLException;

public class CakePoke extends Addon { 

    private static CakePoke INSTANCE;
    protected static MessageManager msgManager = null;
    protected static Database db = null;

    @Override
    public void onEnable() {
        INSTANCE = this;
        msgManager = new MessageManager(Game.CAKEPOKE);
        db = new Database(Game.CAKEPOKE);
        try {
            db.setup();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        ArenaManager.setupArenas();
        Manager.registerListener(BukkitBlockListener.class, this);
        Manager.registerListener(BukkitDeathEvent.class, this);
        Manager.registerListener(BukkitQuitEvent.class, this);
        Manager.registerListener(LobbySignListener.class, this);
        Manager.registerListener(BukkitMessageEvent.class, this);
        Manager.registerListener(CommandListener.class, this);
        Manager.registerListener(BukkitMoveListener.class, this);
        Manager.registerCommand(CommandHandler.class, this);
    }

    @Override
    public void onDisable() {
        for (Arena a : ArenaManager.getArenas()) {
            a.stop(null);
        }
    }

    public static CakePoke getInstance() {
        return INSTANCE;
    }

    public static MessageManager getMessageManager() {
        return msgManager;
    }

    public static Database getDB() {
        return db;
    }

}

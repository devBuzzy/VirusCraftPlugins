package de.TheJeterLP.Bukkit.VirusSpleef.Bukkit;

import de.TheJeterLP.Bukkit.VirusCraftTools.Addon.Addon;
import de.TheJeterLP.Bukkit.VirusCraftTools.Database.Games.Database;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Manager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Game;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
import de.TheJeterLP.Bukkit.VirusSpleef.Arena.Arena;
import de.TheJeterLP.Bukkit.VirusSpleef.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VirusSpleef.commands.CommandHandler;
import de.TheJeterLP.Bukkit.VirusSpleef.listener.BukkitBlockListener;
import de.TheJeterLP.Bukkit.VirusSpleef.listener.BukkitDamageEvent;
import de.TheJeterLP.Bukkit.VirusSpleef.listener.BukkitDeathEvent;
import de.TheJeterLP.Bukkit.VirusSpleef.listener.BukkitMessageEvent;
import de.TheJeterLP.Bukkit.VirusSpleef.listener.BukkitQuitEvent;
import de.TheJeterLP.Bukkit.VirusSpleef.listener.CommandListener;
import de.TheJeterLP.Bukkit.VirusSpleef.listener.LobbySignListener;
import java.sql.SQLException;

public class VirusSpleef extends Addon {

    private static VirusSpleef INSTANCE;
    private static MessageManager msgManager = null;
    private static Database db = null;

    @Override
    public void onEnable() {
        INSTANCE = this;
        msgManager = new MessageManager(Game.SPLEEF);
        db = new Database(Game.SPLEEF);
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
        Manager.registerCommand(CommandHandler.class, this);
    }

    @Override
    public void onDisable() {
        for (Arena a : ArenaManager.getArenas()) {
            a.stop(null);
        }
    }

    public static VirusSpleef getInstance() {
        return INSTANCE;
    }

    public static MessageManager getMessageManager() {
        return msgManager;
    }

    public static Database getDB() {
        return db;
    }

}

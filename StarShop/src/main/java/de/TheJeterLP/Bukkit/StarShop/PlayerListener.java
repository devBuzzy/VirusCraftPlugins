package de.TheJeterLP.Bukkit.StarShop;

import de.TheJeterLP.Bukkit.StarShop.Database.MySQL;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author TheJeterLP
 */
public class PlayerListener extends VClistener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent event) throws SQLException {
        if (MySQL.getStars(event.getPlayer()) == -1) {
            MySQL.addPlayer(event.getPlayer());
        }

        ScoreboardFactory.updateBoard(event.getPlayer());

        for (Player op : Bukkit.getOnlinePlayers()) {
            if (ScoreboardFactory.hasBoard(op)) {
                ScoreboardFactory.updateBoard(op);
            }
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        for (Player op : Bukkit.getOnlinePlayers()) {
            if (ScoreboardFactory.hasBoard(op)) {
                ScoreboardFactory.updateBoard(op);
            }
        }
    }

    @EventHandler
    public void onKick(final PlayerKickEvent e) {
        for (Player op : Bukkit.getOnlinePlayers()) {
            if (ScoreboardFactory.hasBoard(op)) {
                ScoreboardFactory.updateBoard(op);
            }
        }
    }
}

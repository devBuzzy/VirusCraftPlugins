package de.TheJeterLP.Bukkit.StarshopLite;

import java.sql.SQLException;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final HashMap<Player, Long> playerStack = new HashMap<Player, Long>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        Player p = evt.getPlayer();
        if (!this.playerStack.containsKey(p)) {
            this.playerStack.put(p, System.currentTimeMillis());
        }
        p.sendMessage("§6You get Stars for being online! \n §6If you are §aWarrior§6, you get §a10§6 stars for every hour you have been online. \n §6As a normal player, you get §a5 §6for every hour. You get them after you logOut.");
        p.sendMessage("\n\n");
        p.sendMessage("§n§9You should check your stars after you logout!");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) throws SQLException {
        Player p = evt.getPlayer();

        if (this.playerStack.containsKey(p)) {
            long timeDiff = System.currentTimeMillis() - this.playerStack.get(p);
            if (p.hasPermission("stars.warrior") && getHours(timeDiff) >= 1) {
                if (Database.getStars(p) == -1) {
                    Database.addPlayer(p);
                }
                Database.addStars(p, getHours(timeDiff) * 10);
            } else if (getHours(timeDiff) >= 1) {
                if (Database.getStars(p) == -1) {
                    Database.addPlayer(p);
                }
                Database.addStars(p, getHours(timeDiff) * 5);
            }
            this.playerStack.remove(p);
        }
    }

    public int formatTime(long time) {
        int sec = (int) (time / 1000);
        return sec;
    }

    public int getHours(long time) {
        return (formatTime(time) / 60) / 60;
    }

    public int getMinutes(long time) {
        return formatTime(time) / 60;
    }
}

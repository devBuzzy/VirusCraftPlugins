package de.TheJeterLP.Bukkit.CakePoke.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import de.TheJeterLP.Bukkit.CakePoke.Arena.Arena;
import de.TheJeterLP.Bukkit.CakePoke.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.CakePoke.Arena.PlayerData;
import org.bukkit.event.EventPriority;

public class BukkitMessageEvent extends VClistener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) throws NullPointerException {
        if (ArenaManager.getArena(e.getEntity()) == null) return;
        Arena a = ArenaManager.getArena(e.getEntity());
        a.removePlayer(e.getEntity());
        for (PlayerData pd : a.getDatas()) {
            pd.getPlayer().sendMessage(e.getEntity().getName() + ChatColor.GOLD + " died!");
        }
        e.setDeathMessage(null);
    }

}

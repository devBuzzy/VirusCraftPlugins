package de.TheJeterLP.Bukkit.VirusGames.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import de.TheJeterLP.Bukkit.VirusGames.Arena.Arena;
import de.TheJeterLP.Bukkit.VirusGames.Arena.Team;
import de.TheJeterLP.Bukkit.VirusGames.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VirusGames.Arena.PlayerData;

public class BukkitMessageEvent extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) throws NullPointerException {
        if (ArenaManager.getArena(e.getEntity()) == null) return;
        try {
            Arena a = ArenaManager.getArena(e.getEntity());
            Team k = a.getPlayer(e.getEntity().getKiller()).getTeam(), d = a.getPlayer(e.getEntity()).getTeam();
            for (PlayerData pd : a.getDatas()) {
                pd.getPlayer().sendMessage(d.getColor() + e.getEntity().getName() + ChatColor.GOLD + " was killed by " + k.getColor() + e.getEntity().getKiller().getName());
            }
            e.setDeathMessage(null);
        } catch (Exception ex) {
            e.setDeathMessage(null);
        }
    }
   
}

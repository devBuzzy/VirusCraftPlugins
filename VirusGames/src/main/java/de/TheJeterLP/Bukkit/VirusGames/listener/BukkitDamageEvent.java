package de.TheJeterLP.Bukkit.VirusGames.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import de.TheJeterLP.Bukkit.VirusGames.Arena.Arena;
import de.TheJeterLP.Bukkit.VirusGames.Arena.ArenaState;
import de.TheJeterLP.Bukkit.VirusGames.Arena.ArenaManager;

public class BukkitDamageEvent extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent e) throws Exception {
        if (!(e.getEntity() instanceof Player)) return;
        if (ArenaManager.getArena((Player) e.getEntity()) == null) return;
        if (e.getDamager() instanceof Player) {
            if (ArenaManager.getArena((Player) e.getEntity()).getState() == ArenaState.WAITING || ArenaManager.getArena((Player) e.getEntity()).getState() == ArenaState.COUNTDING_DOWN) {
                e.setCancelled(true);
                return;
            }
            Player d = (Player) e.getEntity(), k = (Player) e.getDamager();
            Arena a = ArenaManager.getArena(d);
            Arena b = ArenaManager.getArena(k);
            if (a == null || b == null) {
                e.setCancelled(true);
                return;
            }
            if (a.getPlayer(d).getTeam() == a.getPlayer(k).getTeam()) e.setCancelled(true);
        }
        if (e.getDamager() instanceof Arrow) {
            if (!(((Arrow) e.getDamager()).getShooter() instanceof Player)) return;
            Player d = (Player) e.getEntity(), k = (Player) ((Arrow) e.getDamager()).getShooter();
            Arena a = ArenaManager.getArena(d);
            Arena b = ArenaManager.getArena(k);
            if (a == null || b == null) {
                e.setCancelled(true);
                return;
            }
            if (a.getPlayer(d).getTeam() == a.getPlayer(k).getTeam()) e.setCancelled(true);
        }
    }
}

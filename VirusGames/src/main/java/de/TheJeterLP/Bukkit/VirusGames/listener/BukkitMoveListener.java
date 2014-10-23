package de.TheJeterLP.Bukkit.VirusGames.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VirusGames.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VirusGames.Arena.ArenaState;
import de.TheJeterLP.Bukkit.VirusGames.Arena.PlayerData;
import de.TheJeterLP.Bukkit.VirusGames.Arena.Team;
import org.bukkit.Effect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author TheJeterLP
 */
public class BukkitMoveListener extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onMove(final PlayerMoveEvent e) {
        if (ArenaManager.getArena(e.getPlayer()) == null) return;
        PlayerData p = ArenaManager.getArena(e.getPlayer()).getPlayer(e.getPlayer());
        if (p.getTeam() == Team.VIRUS && ArenaManager.getArena(e.getPlayer()).getState() == ArenaState.STARTED) {
            p.getPlayer().getWorld().playEffect(p.getPlayer().getLocation(), Effect.ENDER_SIGNAL, 1);
        }
    }

}

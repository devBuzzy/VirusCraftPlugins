package de.TheJeterLP.Bukkit.VirusGames.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.TheJeterLP.Bukkit.VirusGames.Arena.ArenaManager;

public class BukkitQuitEvent extends VClistener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (ArenaManager.getArena(e.getPlayer()) == null) return;
        ArenaManager.getArena(e.getPlayer()).removePlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        if (ArenaManager.getArena(e.getPlayer()) == null) return;
        ArenaManager.getArena(e.getPlayer()).removePlayer(e.getPlayer());
    }

}

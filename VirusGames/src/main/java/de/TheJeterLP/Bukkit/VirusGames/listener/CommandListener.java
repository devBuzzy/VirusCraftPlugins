package de.TheJeterLP.Bukkit.VirusGames.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VirusGames.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VirusGames.Bukkit.VirusGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author TheJeterLP
 */
public class CommandListener extends VClistener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (ArenaManager.getArena(p) == null) return;
        if (e.getMessage().split(" ")[0].equalsIgnoreCase("vg")) return;
        if (p.isOp()) return;
        VirusGames.getMessageManager().message(p, MessageManager.PrefixType.BAD, "Commands are disabled during the game!");
        e.setCancelled(true);
    }

}

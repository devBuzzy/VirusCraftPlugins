package de.TheJeterLP.Bukkit.CakePoke.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.CakePoke.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.CakePoke.Bukkit.CakePoke;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
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
        if (e.getMessage().split(" ")[0].equalsIgnoreCase("cp")) return;
        if (p.isOp()) return;
        CakePoke.getMessageManager().message(p, MessageManager.PrefixType.BAD, "Commands are disabled during the game!");
        e.setCancelled(true);
    }

}

package de.TheJeterLP.Bukkit.VirusGames.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VirusGames.Arena.Class;
import de.TheJeterLP.Bukkit.VirusGames.Bukkit.VirusGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;

/**
 * @author TheJeterLP
 */
public class KitCreateListener extends VClistener {

    @EventHandler
    public void onSignCreate(SignChangeEvent e) {
        if (e.isCancelled()) return;
        if (!e.getLine(0).equalsIgnoreCase("[VG]")) return;
        if (!e.getLine(1).equalsIgnoreCase("[CLASS]")) return;
        Player p = e.getPlayer();
        if (!p.isOp()) {
            VirusGames.getMessageManager().message(p, MessageManager.PrefixType.BAD, "You don't have permission.");
            e.setCancelled(true);
            return;
        }
        Class c;
        try {
            c = Class.valueOf(e.getLine(2).toUpperCase());
        } catch (IllegalArgumentException ex) {
            e.setCancelled(true);
            VirusGames.getMessageManager().message(p, MessageManager.PrefixType.BAD, "Unknown class.");
            return;
        }
        e.setLine(0, "§6[§aVG§6]");
        e.setLine(1, "§3[§6Class§3]");
        e.setLine(2, "§a" + c.name().toLowerCase());
    }

}

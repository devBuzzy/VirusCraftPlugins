package de.TheJeterLP.Bukkit.VirusCraftTools.Listeners;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author TheJeterLP
 */
public class CommandListener extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        String command = e.getMessage().split(" ")[0];

        if (command.contains(":")) {
            command = "/" + command.split(":")[1];
        }

        if (command.equalsIgnoreCase("/pl") || command.equalsIgnoreCase("/plugins") || command.equalsIgnoreCase("/pl-fake")) {
            if (!p.hasPermission("viruscraft.plugins")) {
                p.sendMessage("Plugins (3): §aTools§r, §aGames§r, §aAdminCMD");
                e.setCancelled(true);
                return;
            }
            if (command.equalsIgnoreCase("/pl-fake")) {
                p.sendMessage("Plugins (3): §aTools§r, §aGames§r, §aAdminCMD");
                e.setCancelled(true);
                return;
            }
        }
        if ((command.equalsIgnoreCase("/op") || command.equalsIgnoreCase("/deop") || command.equalsIgnoreCase("/help") || command.equalsIgnoreCase("/?")) && !p.isOp()) {
            Utils.sendMessage(MessageType.ERROR, p, "Usage of the " + command + " command is disabled.");
            e.setCancelled(true);
            return;
        }

        if (!Utils.isCmdRegistered(command.replaceFirst("/", ""))) {
            Utils.sendMessage(MessageType.ERROR, p, "Command not found!");
            e.setCancelled(true);
        }
    }

}

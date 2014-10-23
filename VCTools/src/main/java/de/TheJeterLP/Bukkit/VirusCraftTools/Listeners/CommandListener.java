package de.TheJeterLP.Bukkit.VirusCraftTools.Listeners;

import com.google.common.base.Joiner;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Config;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author TheJeterLP
 */
public class CommandListener extends VClistener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        final String command = e.getMessage().split(" ")[0];

        if (command.equalsIgnoreCase("/pl") || command.equalsIgnoreCase("/plugins")) {
            if (!p.hasPermission("viruscraft.plugins")) {
                int num = Config.FAKE_PL_LIST.getStringList().size();
                String list = Joiner.on("§r, ").join(Config.FAKE_PL_LIST.getStringList());
                p.sendMessage("Plugins (" + num + "): §a" + list);
                e.setCancelled(true);
                return;
            }            
        }
        if (Config.DISABLED_COMMANDS.getStringList().contains(command.toLowerCase()) && !p.isOp()) {
            Utils.sendMessage(MessageType.ERROR, p, "Usage of the " + command + " command is disabled.");
            e.setCancelled(true);
            return;
        }

        for (Player op : VCplugin.inst().getServer().getOnlinePlayers()) {            
            if (!Utils.hasCWEnabled(op)) continue;
            if (p.getName().equals(op.getName())) continue;
            Utils.sendMessage(MessageType.INFO, op, p.getName() + " ran command: " + e.getMessage());
            return;
        }
    }

}

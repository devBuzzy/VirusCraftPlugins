package de.TheJeterLP.Bukkit.Nametags;

import de.TheJeterLP.Bukkit.Nametags.Utils.NametagManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class NametagEventHandler extends VClistener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        NametagManager.sendTeamsToPlayer(e.getPlayer());
        NametagManager.clear(e.getPlayer().getName());
        Main.setNametag(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onCommand(final org.bukkit.event.server.ServerCommandEvent e) {
        if (e.getCommand().startsWith("pex")) {
            Main.getInstance().load();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommand(final PlayerCommandPreprocessEvent e) {
        if (e.getMessage().startsWith("/pex") && e.getPlayer().isOp()) {
            Main.getInstance().load();
        }
    }

}

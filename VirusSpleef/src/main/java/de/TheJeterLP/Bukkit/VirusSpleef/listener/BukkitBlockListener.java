package de.TheJeterLP.Bukkit.VirusSpleef.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VirusSpleef.Arena.Arena;
import de.TheJeterLP.Bukkit.VirusSpleef.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VirusSpleef.Arena.ArenaState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BukkitBlockListener extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockDamage(final PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Player p = e.getPlayer();
        Arena a = ArenaManager.getArena(p);
        if (a == null) return;
        if (a.getState() != ArenaState.STARTED) return;
        a.getBlockStorage().breakBlock(e.getClickedBlock());
        e.setCancelled(true);
    }

}

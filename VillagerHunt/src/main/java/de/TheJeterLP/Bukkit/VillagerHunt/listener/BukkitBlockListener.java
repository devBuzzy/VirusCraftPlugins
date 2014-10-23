package de.TheJeterLP.Bukkit.VillagerHunt.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VillagerHunt.Arena.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import de.TheJeterLP.Bukkit.VillagerHunt.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VillagerHunt.Arena.ArenaState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class BukkitBlockListener extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent e) {
        if (e.getPlayer().isOp()) return;
        Arena a = ArenaManager.getArena(e.getPlayer());
        if (a == null) return;
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().isOp()) return;
        Arena a = ArenaManager.getArena(e.getPlayer());
        if (a == null) return;
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getPlayer().isOp()) return;
        if (ArenaManager.getArena(e.getPlayer()) == null) return;
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onExpChange(final PlayerExpChangeEvent e) {
        if (ArenaManager.getArena(e.getPlayer()) == null) return;
        Arena a = ArenaManager.getArena(e.getPlayer());
        if (a.getState() != ArenaState.STARTED) return;
        e.setAmount(0);
    }

}

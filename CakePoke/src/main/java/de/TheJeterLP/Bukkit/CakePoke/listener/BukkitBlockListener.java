package de.TheJeterLP.Bukkit.CakePoke.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.CakePoke.Arena.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import de.TheJeterLP.Bukkit.CakePoke.Arena.ArenaManager;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BukkitBlockListener extends VClistener {
        
        @EventHandler(ignoreCancelled = true)
        public void onBlockDamage(BlockDamageEvent e) {
                Arena a = ArenaManager.getArena(e.getPlayer());
                if (a == null) return;
                e.setCancelled(true);
        }
        
        @EventHandler(ignoreCancelled = true)
        public void onBlockBreak(BlockBreakEvent e) {
                Arena a = ArenaManager.getArena(e.getPlayer());
                if (a == null) return;
                e.setCancelled(true);
        }
        
        @EventHandler(ignoreCancelled = true)
        public void onBlockPlace(BlockPlaceEvent e) {
                Arena a = ArenaManager.getArena(e.getPlayer());
                if (a == null) return;
                e.setCancelled(true);
        }
        
        @EventHandler(ignoreCancelled = true)
        public void onButtonClick(final PlayerInteractEvent e) {
                Arena a = ArenaManager.getArena(e.getPlayer());
                if (a == null) return;
                if (e.getClickedBlock().getType() != Material.WOOD_BUTTON && e.getClickedBlock().getType() != Material.STONE_BUTTON) {
                        e.setCancelled(true);
                        return;
                }
                a.stop(e.getPlayer());
        }
        
}

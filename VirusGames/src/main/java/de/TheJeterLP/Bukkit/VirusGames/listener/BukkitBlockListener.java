package de.TheJeterLP.Bukkit.VirusGames.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.TheJeterLP.Bukkit.VirusGames.Arena.ArenaManager;

public class BukkitBlockListener extends VClistener {

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		if(ArenaManager.getArena(e.getPlayer()) == null || e.getPlayer().isOp()) return;
		e.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent e) {
		if(ArenaManager.getArena(e.getPlayer()) == null || e.getPlayer().isOp()) return;
		e.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBreak(PlayerInteractEvent e) {
		if(ArenaManager.getArena(e.getPlayer()) == null|| e.getPlayer().isOp()) return;
		if(e.getClickedBlock() == null) return;
		if(e.getClickedBlock().getType() == Material.PAINTING || e.getClickedBlock().getType() == Material.ITEM_FRAME) e.setCancelled(true);
	}
	
}

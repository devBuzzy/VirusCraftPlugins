package de.TheJeterLP.Bukkit.VirusGames.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import de.TheJeterLP.Bukkit.VirusGames.Arena.ArenaManager;

public class BukkitHungerEvent extends VClistener {

	@EventHandler(ignoreCancelled = true)
	public void onHunger(FoodLevelChangeEvent e){
		if(!(e.getEntity() instanceof Player)) return;
		if(ArenaManager.getArena((Player)e.getEntity()) == null) return;
		e.setCancelled(true);
	}
	
}

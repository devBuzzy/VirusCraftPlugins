package de.TheJeterLP.Bukkit.VillagerHunt.listener;

import de.TheJeterLP.Bukkit.VillagerHunt.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * @author TheJeterLP
 */
public class BukkitMobSpawnListener extends VClistener {

    private World w = null;

    public BukkitMobSpawnListener() {
        if (!ArenaManager.getArenas().isEmpty()) {
            w = ArenaManager.getArenas().get(0).getSpawn().getWorld();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onZombieSpawn(CreatureSpawnEvent e) {
        if (w == null) return;
        if (!e.getLocation().getWorld().getName().equalsIgnoreCase(w.getName())) return;
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) return;
        e.setCancelled(true);
    }

}

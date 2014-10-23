package de.TheJeterLP.Bukkit.VillagerHunt.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VillagerHunt.Arena.Arena;
import org.bukkit.event.entity.PlayerDeathEvent;
import de.TheJeterLP.Bukkit.VillagerHunt.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VillagerHunt.Arena.ArenaState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class BukkitDeathEvent extends VClistener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (ArenaManager.getArena(e.getEntity()) == null) return;
        e.getDrops().clear();
        e.getEntity().setFireTicks(0);
        Arena a = ArenaManager.getArena(e.getEntity());
        Utils.respawn(e.getEntity());
        a.removePlayer(e.getEntity());
    }

    @EventHandler
    public void onVillagerDeath(EntityDeathEvent e) {
        if (e.getEntityType() == EntityType.VILLAGER) {
            Villager v = (Villager) e.getEntity();
            if (v.getCustomName() != null) {
                try {
                    int id = Integer.valueOf(v.getCustomName());
                    Arena a = ArenaManager.getArena(id);
                    if (a != null && a.getState() == ArenaState.STARTED) {
                        a.stop("The villager was killed!");
                    }
                } catch (NumberFormatException ex) {
                }
            }
        } else if (e.getEntityType() == EntityType.ZOMBIE) {
            Zombie z = (Zombie) e.getEntity();
            if (z.getCustomName() != null) {
                try {
                    int id = Integer.valueOf(z.getCustomName());
                    Arena a = ArenaManager.getArena(id);
                    if (a != null && a.getState() == ArenaState.STARTED) {
                        a.getSpawnThread().killZombie(z);
                        e.getDrops().clear();
                    }
                } catch (NumberFormatException ex) {
                }
            }
        }

    }
}

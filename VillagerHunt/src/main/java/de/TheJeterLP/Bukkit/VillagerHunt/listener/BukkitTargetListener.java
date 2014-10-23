package de.TheJeterLP.Bukkit.VillagerHunt.listener;

import de.TheJeterLP.Bukkit.VillagerHunt.Arena.Arena;
import de.TheJeterLP.Bukkit.VillagerHunt.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VillagerHunt.Arena.ArenaState;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

/**
 * @author TheJeterLP
 */
public class BukkitTargetListener extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onZombieTarget(EntityTargetLivingEntityEvent e) {
        if (e.getEntity().getType() == EntityType.ZOMBIE) {
            Zombie z = (Zombie) e.getEntity();
            if (z.getCustomName() != null) {
                try {
                    int arenaID = Integer.valueOf(z.getCustomName());
                    Arena a = ArenaManager.getArena(arenaID);
                    if (a != null && a.getState() == ArenaState.STARTED) {
                        e.setTarget(a.getVillager());
                    }
                } catch (NumberFormatException ex) {
                }
            }
        }
    }

}

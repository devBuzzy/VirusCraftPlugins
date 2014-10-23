package de.TheJeterLP.Bukkit.VillagerHunt.listener;

import de.TheJeterLP.Bukkit.VillagerHunt.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VillagerHunt.Arena.ArenaState;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import java.util.Random;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class BukkitDamageEvent extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onVillagerDamage(EntityDamageByEntityEvent e) throws Exception {
        if (e.getEntity() instanceof Villager) {
            Villager v = (Villager) e.getEntity();
            if (v.getCustomName() == null) return;
            try {
                int arenaID = Integer.valueOf(v.getCustomName());
                if (ArenaManager.loaded(arenaID)) {
                    if (ArenaManager.getArena(arenaID).getState() == ArenaState.STARTED) {
                        if (e.getDamager().getType() != EntityType.ZOMBIE)
                            e.setCancelled(true);
                    }
                }
            } catch (NumberFormatException ex) {
            }
        } else if (e.getEntity() instanceof Zombie) {
            Zombie z = (Zombie) e.getEntity();
            if (z.getCustomName() == null) return;
            try {
                int arenaID = Integer.valueOf(z.getCustomName());
                if (ArenaManager.loaded(arenaID)) {
                    e.setDamage(getRandomInt(7, 21));
                }
            } catch (NumberFormatException ex) {
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onZombieFireTicks(EntityDamageEvent e) {
        if (e.getEntity() instanceof Zombie) {
            Zombie z = (Zombie) e.getEntity();
            if (z.getCustomName() == null) return;
            try {
                int arenaID = Integer.valueOf(z.getCustomName());
                if (ArenaManager.loaded(arenaID)) {
                    if (e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || e.getCause() == EntityDamageEvent.DamageCause.FIRE) {
                        e.setDamage(0.0);
                        e.setCancelled(true);
                        z.setFireTicks(0);
                    }
                }
            } catch (NumberFormatException ex) {
            }
        }
    }

    private int getRandomInt(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }
}

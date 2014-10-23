package de.TheJeterLP.Bukkit.VirusSpleef.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import de.TheJeterLP.Bukkit.VirusSpleef.Arena.ArenaManager;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class BukkitDamageEvent extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent e) throws Exception {
        if (!(e.getEntity() instanceof Player)) return;
        if (ArenaManager.getArena((Player) e.getEntity()) == null) return;
        if (e.getCause() != DamageCause.VOID) {
            e.setCancelled(true);
            e.setDamage(0.0);
        } else {
            ArenaManager.getArena((Player) e.getEntity()).removePlayer((Player) e.getEntity());
        }
    }
}

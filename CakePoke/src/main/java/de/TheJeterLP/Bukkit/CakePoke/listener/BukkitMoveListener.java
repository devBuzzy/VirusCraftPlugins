package de.TheJeterLP.Bukkit.CakePoke.listener;

import de.TheJeterLP.Bukkit.CakePoke.Arena.Arena;
import de.TheJeterLP.Bukkit.CakePoke.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.CakePoke.Arena.ArenaState;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * @author TheJeterLP
 */
public class BukkitMoveListener extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onDamage(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (ArenaManager.getArena(p) == null) return;
        Arena a = ArenaManager.getArena(p);
        if (a.getState() != ArenaState.STARTED) {
            e.setCancelled(true);
            return;
        }
        if (e.getCause() == DamageCause.VOID) {
            a.portBack(p);
        } else {
            if (e.getCause() != DamageCause.ENTITY_ATTACK) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByEntity(final EntityDamageByEntityEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER || e.getDamager().getType() != EntityType.PLAYER) return;
        Player p = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();
        if (ArenaManager.getArena(p) == null) return;

        Arena a = ArenaManager.getArena(p);
        if (a.getState() != ArenaState.STARTED) {
            e.setCancelled(true);
            return;
        }

        if (ArenaManager.getArena(damager) == null) {
            e.setCancelled(true);
            return;
        }

        if (damager.getItemInHand().getType() != Material.CAKE) {
            e.setCancelled(true);
            return;
        }

        if (!damager.getItemInHand().getEnchantments().containsKey(Enchantment.KNOCKBACK)) {
            e.setCancelled(true);
            return;
        }

        e.setDamage(0.0);
    }
}

package de.TheJeterLP.Bukkit.VirusGames.listener;

import de.TheJeterLP.Bukkit.StarShop.Database.MySQL;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VirusGames.Arena.Arena;
import de.TheJeterLP.Bukkit.VirusGames.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VirusGames.Bukkit.VirusGames;
import java.sql.SQLException;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BukkitDeathEvent extends VClistener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) throws SQLException {
        if (!(e.getEntity() instanceof Player)) return;
        final Player p = e.getEntity();
        if (ArenaManager.getArena(p) == null) return;
        e.getDrops().clear();
        Arena a = ArenaManager.getArena(p);
        if (p.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK) {
            if (p.getLastDamageCause().getEntityType() == EntityType.PLAYER) {
                Player killer = p.getKiller();
                MySQL.addStars(killer, 10);
                VirusGames.getMessageManager().message(killer, MessageManager.PrefixType.INFO, "You got 10 stars for killing " + p.getName());
            }
        }
        Utils.respawn(p);       
        a.removePlayer(p);
    }

}

package de.TheJeterLP.Bukkit.CakePoke.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.CakePoke.Arena.Arena;
import org.bukkit.event.entity.PlayerDeathEvent;
import de.TheJeterLP.Bukkit.CakePoke.Arena.ArenaManager;
import org.bukkit.event.EventHandler;

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
}

/*
 * Copyright 2014 TheJeterLP.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.thejeterlp.bukkit.viruscmd.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.thejeterlp.bukkit.viruscmd.player.PlayerManager;
import de.thejeterlp.bukkit.viruscmd.player.VCPlayer;
import de.thejeterlp.bukkit.viruscmd.spawn.SpawnManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * @author TheJeterLP
 */
public class PlayerDamageListener extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onDamage(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        VCPlayer vcp = PlayerManager.getVCPlayer(p);

        if (Utils.isAtHub(p) && e.getCause() == DamageCause.VOID) {
            SpawnManager.getSpawn(vcp).teleport(p);
            e.setCancelled(true);
            e.setDamage(0.0);
        } else {
            if (vcp.god()) {
                e.setCancelled(true);
                e.setDamage(0.0);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHunger(final FoodLevelChangeEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        VCPlayer vcp = PlayerManager.getVCPlayer((Player) e.getEntity());
        if (vcp.god()) e.setCancelled(true);
    }

}

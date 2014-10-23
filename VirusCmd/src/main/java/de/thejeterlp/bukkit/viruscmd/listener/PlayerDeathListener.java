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
import de.thejeterlp.bukkit.viruscmd.VirusCmd;
import de.thejeterlp.bukkit.viruscmd.player.PlayerManager;
import de.thejeterlp.bukkit.viruscmd.spawn.Spawn;
import de.thejeterlp.bukkit.viruscmd.spawn.SpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * @author TheJeterLP
 */
public class PlayerDeathListener extends VClistener {
    
    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        Utils.respawn(e.getEntity());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(final PlayerRespawnEvent e) {
        Spawn s = SpawnManager.getSpawn(PlayerManager.getVCPlayer(e.getPlayer()));
        e.setRespawnLocation(s.getLocation());
    }
    
}

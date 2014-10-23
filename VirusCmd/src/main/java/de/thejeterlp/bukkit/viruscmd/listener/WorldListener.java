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

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.thejeterlp.bukkit.viruscmd.world.VCWorld;
import de.thejeterlp.bukkit.viruscmd.world.WorldManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

/**
 * @author TheJeterLP
 */
public class WorldListener extends VClistener {
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onWorldLoad(final WorldLoadEvent e) {
        VCWorld vcw = WorldManager.getWorld(e.getWorld());
        if (vcw == null) {
            VCWorld nvc = new VCWorld(e.getWorld(), false, String.valueOf(e.getWorld().getTime()));
            WorldManager.createWorld(nvc);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onWorldUnload(WorldUnloadEvent e) {
        VCWorld vcw = WorldManager.getWorld(e.getWorld());
        if (vcw != null) {
            WorldManager.unloadWorld(vcw);
        }
    }
    
}

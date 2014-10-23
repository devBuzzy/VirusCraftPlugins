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

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.thejeterlp.bukkit.viruscmd.player.PlayerManager;
import de.thejeterlp.bukkit.viruscmd.player.VCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author TheJeterLP
 */
public class PlayerCommandListener extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        VCPlayer vcp = PlayerManager.getVCPlayer(e.getPlayer());
        for (Player bp : Bukkit.getOnlinePlayers()) {
            VCPlayer bvcp = PlayerManager.getVCPlayer(bp);
            if (!bvcp.commandwatcher()) continue;
            if (bvcp.getID() == vcp.getID()) continue;
            bvcp.sendMessage(MessageType.INFO, vcp.getDisplayName() + " ran Command: " + e.getMessage());
        }
    }

}

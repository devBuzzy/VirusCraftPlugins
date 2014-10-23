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
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

/**
 * @author TheJeterLP
 */
public class DoubleJumpListener extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        VCPlayer vcp = PlayerManager.getVCPlayer(player);

        if (vcp.fly()) return;

        if (player.getGameMode() != GameMode.CREATIVE && Utils.isAtHub(player)) {
            event.setCancelled(true);
            player.setAllowFlight(false);
            player.setFlying(false);
            Utils.launchPlayer(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        VCPlayer vcp = PlayerManager.getVCPlayer(player);

        if (vcp.fly()) return;

        if (player.getGameMode() != GameMode.CREATIVE) {
            if (Utils.isAtHub(player) && player.isFlying()) {
                player.setFlying(false);
            } else if ((player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) && (!player.isFlying()) && Utils.isAtHub(player)) {
                player.setAllowFlight(true);
            }
        }
    }

}

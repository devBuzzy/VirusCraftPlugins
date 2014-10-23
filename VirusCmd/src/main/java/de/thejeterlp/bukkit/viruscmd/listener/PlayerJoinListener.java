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

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Config;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.thejeterlp.bukkit.viruscmd.VirusCmd;
import de.thejeterlp.bukkit.viruscmd.player.PlayerManager;
import de.thejeterlp.bukkit.viruscmd.player.SQLPlayer;
import de.thejeterlp.bukkit.viruscmd.player.VCPlayer;
import de.thejeterlp.bukkit.viruscmd.spawn.SpawnManager;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author TheJeterLP
 */
public class PlayerJoinListener extends VClistener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLateJoin(final PlayerJoinEvent e) throws SQLException {
        if (!PlayerManager.hasPlayedBefore(e.getPlayer())) {
            SQLPlayer player = new SQLPlayer(e.getPlayer());
            PlayerManager.insert(player);
            String message = Utils.formatWithPlayer(Config.MESSAGES_FIRSTJOIN.getString(), e.getPlayer());
            e.setJoinMessage(message);
        } else {
            String message = Utils.formatWithPlayer(Config.MESSAGES_JOIN.getString(), e.getPlayer());
            e.setJoinMessage(message);
        }

        final VCPlayer vcp = PlayerManager.getVCPlayer(e.getPlayer());
        vcp.setDisplayName(vcp.getDisplayName());

        if (vcp.invisible()) {
            for (Player op : Bukkit.getOnlinePlayers()) {
                op.hidePlayer(vcp.getPlayer());
            }
            Utils.sendMessage(MessageType.INFO, e.getPlayer(), "You joined vanished.");
            e.setJoinMessage(null);
        }

        if (vcp.getPlayer().getGameMode() != GameMode.CREATIVE) vcp.setFly(vcp.fly());

        for (Player op : Bukkit.getOnlinePlayers()) {
            VCPlayer ovcp = PlayerManager.getVCPlayer(op);
            if (ovcp == null) continue;
            if (ovcp.invisible()) {
                vcp.getPlayer().hidePlayer(ovcp.getPlayer());
            }
        }

        SpawnManager.getSpawn(vcp).teleport(vcp.getPlayer());

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(final PlayerQuitEvent e) {
        String message = Utils.formatWithPlayer(Config.MESSAGES_QUIT.getString(), e.getPlayer());
        e.setQuitMessage(message);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onKick(final PlayerKickEvent e) {
        String message = Utils.formatWithPlayer(Config.MESSAGES_KICK.getString(), e.getPlayer());
        e.setLeaveMessage(message);
    }

}

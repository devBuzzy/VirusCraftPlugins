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
package de.thejeterlp.bukkit.viruscmd.player;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public abstract class VCPlayer {

    private final UUID uuid;
    private final int id;

    public VCPlayer(UUID uuid, int id) {
        this.uuid = uuid;
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public UUID getUUID() {
        return uuid;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public boolean isOnline() {
        return !invisible() && getOfflinePlayer().isOnline();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public String getGroup() {
        if (getPlayer() == null) return null;
        return VCplugin.inst().getChat().getPlayerGroups(getPlayer())[0];
    }

    public String getPrefix() {
        return Utils.replaceColors(VCplugin.inst().getChat().getPlayerPrefix(getPlayer()));
    }

    public String getSuffix() {
        return Utils.replaceColors(VCplugin.inst().getChat().getPlayerSuffix(getPlayer()));
    }

    public CommandResult sendMessage(MessageType type, String message) {
        return Utils.sendMessage(type, getPlayer(), message);
    }

    public abstract boolean god();

    public abstract boolean invisible();

    public abstract boolean commandwatcher();

    public abstract boolean spy();

    public abstract void setGod(boolean bool);

    public abstract void setInvisible(boolean bool);

    public abstract void setCommandWatcher(boolean bool);

    public abstract void setSpy(boolean bool);

    public abstract boolean fly();

    public abstract void setFly(boolean bool);

    public abstract String getDisplayName();

    public abstract void setDisplayName(String name);

    public abstract void setMuted(boolean bool);

    public abstract boolean isMuted();

}

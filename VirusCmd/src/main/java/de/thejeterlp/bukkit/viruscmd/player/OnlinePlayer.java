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

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Config;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author TheJeterLP
 */
public class OnlinePlayer extends VCPlayer {

    public OnlinePlayer(int id, UUID uuid, String displayname, boolean god, boolean invis, boolean cw, boolean spy, boolean fly, boolean muted) {
        super(uuid, id);
        this.god = god;
        this.invis = invis;
        this.cw = cw;
        this.spy = spy;
        this.displayname = displayname;
        this.fly = fly;
        this.muted = muted;

    }

    private boolean god = false, invis = false, cw = false, spy = false, fly = false, muted = false;
    private String displayname;

    @Override
    public boolean god() {
        return this.god;
    }

    @Override
    public boolean invisible() {
        return this.invis;
    }

    @Override
    public boolean commandwatcher() {
        return this.cw;
    }

    @Override
    public boolean spy() {
        return this.spy;
    }

    @Override
    public void setGod(boolean bool) {
        this.god = bool;
    }

    @Override
    public void setInvisible(boolean bool) {
        if (bool) {
            if (getPlayer() != null) {
                Utils.broadcastMessage(MessageType.NOTHING, Utils.formatWithPlayer(Config.MESSAGES_QUIT.getString(), getPlayer()));
                for (Player op : Bukkit.getOnlinePlayers()) {
                    op.hidePlayer(getPlayer());
                }
                getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
            }
        } else {
            if (getPlayer() != null) {
                Utils.broadcastMessage(MessageType.NOTHING, Utils.formatWithPlayer(Config.MESSAGES_JOIN.getString(), getPlayer()));
                for (Player op : Bukkit.getOnlinePlayers()) {
                    op.showPlayer(getPlayer());
                }
                getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }
        this.invis = bool;
    }

    @Override
    public void setCommandWatcher(boolean bool) {
        this.cw = bool;
    }

    @Override
    public void setSpy(boolean bool) {
        this.spy = bool;
    }

    @Override
    public String getDisplayName() {
        return displayname;
    }

    @Override
    public void setDisplayName(String name) {
        this.displayname = name;
        if (getPlayer() != null) {
            getPlayer().setDisplayName(name);
            getPlayer().setPlayerListName(name);
        }
    }

    @Override
    public boolean fly() {
        return this.fly;
    }

    @Override
    public void setFly(boolean bool) {
        this.fly = bool;
        if (getPlayer() != null) {
            getPlayer().setAllowFlight(this.fly);
        }
    }

    @Override
    public void setMuted(boolean bool) {
        this.muted = bool;
    }

    @Override
    public boolean isMuted() {
        return this.muted;
    }
}

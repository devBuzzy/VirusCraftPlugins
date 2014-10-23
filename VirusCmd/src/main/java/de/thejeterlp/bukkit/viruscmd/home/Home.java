/*
 * Copyright 2014 Joey.
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
package de.thejeterlp.bukkit.viruscmd.home;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.thejeterlp.bukkit.viruscmd.player.VCPlayer;
import de.thejeterlp.bukkit.viruscmd.world.VCWorld;
import de.thejeterlp.bukkit.viruscmd.world.WorldManager;
import org.bukkit.Location;

/**
 * @author TheJeterLP
 */
public class Home {

    private Location loc;
    private final VCPlayer owner;
    private final String name;
    private final int id;
    private static int nextIndex = 1;

    public Home(Location loc, VCPlayer owner, String name) {
        this.loc = loc;
        this.owner = owner;
        this.name = name;
        this.id = nextIndex;
        nextIndex++;
    }

    public Home(String serializedLocation, VCPlayer owner, String name) {
        this.loc = Utils.deserialLocation(serializedLocation);
        this.owner = owner;
        this.name = name;
        this.id = nextIndex;
        nextIndex++;
    }

    public Home(String serializedLocation, VCPlayer owner, String name, int id) {
        this.loc = Utils.deserialLocation(serializedLocation);
        this.owner = owner;
        this.name = name;
        this.id = id;
        if (id > nextIndex) {
            nextIndex = id;
        }
        nextIndex++;
    }

    protected int getID() {
        return id;
    }

    public Location getLocation() {
        return loc;
    }

    public void updateLocation(Location loc) {
        this.loc = loc;
    }

    public void updateLocation(String serializedLocation) {
        this.loc = Utils.deserialLocation(serializedLocation);
    }

    public String getSerializedLocation() {
        return Utils.serialLocation(loc);
    }

    public String getName() {
        return name;
    }

    public VCPlayer getOwner() {
        return owner;
    }

    public VCWorld getWorld() {
        return WorldManager.getWorld(loc);
    }

    public void teleport() {
        getOwner().getPlayer().teleport(loc);
    }
}

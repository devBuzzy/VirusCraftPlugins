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
package de.thejeterlp.bukkit.viruscmd.spawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Spawn {

    private Location loc;
    private final SpawnType type;
    private final String group;

    public Spawn(final Location loc, SpawnType type, String group) {
        this.loc = loc;
        this.type = type;
        this.group = group;
    }

    public Location getLocation() {
        return loc;
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }
    
    public String getGroup() {
        return group;
    }

    public void teleport(Player p) {
        p.teleport(loc);
    }

    public SpawnType getType() {
        return type;
    }

}

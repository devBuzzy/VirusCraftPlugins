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
package de.thejeterlp.bukkit.viruscmd.world;

import de.thejeterlp.bukkit.viruscmd.VirusCmd;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * @author TheJeterLP
 */
public class VCWorld {

    private final World w;
    private long moment;
    private boolean timePaused;

    public VCWorld(World world, boolean timePaused, String moment) {
        this.w = world;
        this.timePaused = timePaused;
        this.moment = Long.valueOf(moment);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VirusCmd.getInstance(), new ResetTime(this), 20 * 3, 20 * 3);
    }

    public World getWorld() {
        return w;
    }

    public void pauseTime() {
        this.timePaused = true;
        this.moment = w.getTime();
    }

    public void unPauseTime() {
        this.timePaused = false;
    }

    public boolean isTimePaused() {
        return timePaused;
    }

    public long getTimePauseMoment() {
        return moment;
    }
}

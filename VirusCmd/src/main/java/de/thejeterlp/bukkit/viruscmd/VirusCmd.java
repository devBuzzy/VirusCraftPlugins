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
package de.thejeterlp.bukkit.viruscmd;

import de.TheJeterLP.Bukkit.VirusCraftTools.Addon.Addon;
import de.TheJeterLP.Bukkit.VirusCraftTools.Database.DataConnection;
import de.thejeterlp.bukkit.viruscmd.home.HomeManager;
import de.thejeterlp.bukkit.viruscmd.player.PlayerManager;
import de.thejeterlp.bukkit.viruscmd.spawn.SpawnManager;
import de.thejeterlp.bukkit.viruscmd.world.WorldManager;
import java.sql.SQLException;

/**
 * @author TheJeterLP
 */
public class VirusCmd extends Addon {
    
    private static VirusCmd INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        try {
            VCHelper.initDatabase();
            PlayerManager.init();
            WorldManager.init();
            HomeManager.init();
            SpawnManager.init();
            VCHelper.registerCommands();
            VCHelper.registerListener();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        SpawnManager.save();
        HomeManager.save();
        PlayerManager.save();
        WorldManager.save();
        getServer().getScheduler().cancelTasks(this);
    }
    
    public static VirusCmd getInstance() {
        return INSTANCE;
    }
    
    public static DataConnection getDB() {
        return VCHelper.getDatabase();
    }

}

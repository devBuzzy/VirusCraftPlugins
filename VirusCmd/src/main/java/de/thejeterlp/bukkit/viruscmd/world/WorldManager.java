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

import de.TheJeterLP.Bukkit.VirusCraftTools.Database.DataConnection;
import de.thejeterlp.bukkit.viruscmd.VCHelper;
import de.thejeterlp.bukkit.viruscmd.VirusCmd;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author TheJeterLP
 */
public class WorldManager {

    private static final HashMap<String, VCWorld> worlds = new HashMap<>();
    private static final DataConnection conn = VCHelper.getDatabase();

    public static void init() {
        worlds.clear();        
        try {
            PreparedStatement st = conn.getPreparedStatement("SELECT * FROM `vc_worlds`;");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                boolean paused = rs.getBoolean("paused");
                String time = rs.getString("time");
                if (Bukkit.getWorld(name) == null) {
                    PreparedStatement str = conn.getPreparedStatement("DELETE FROM `vc_worlds` WHERE `name` = ?;");
                    str.setString(1, name);
                    str.executeUpdate();
                    conn.closeStatment(str);
                } else {
                    VCWorld vcw = new VCWorld(Bukkit.getWorld(name), paused, time);
                    worlds.put(name, vcw);
                }
            }

            conn.closeResultSet(rs);
            conn.closeStatment(st);

            for (World w : Bukkit.getWorlds()) {
                if (getWorld(w) == null) {
                    createWorld(new VCWorld(w, false, String.valueOf(w.getTime())));
                }
            }
            
            VirusCmd.getInstance().getLogger().info("Loaded " + worlds.size() + " worlds!");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static VCWorld getWorld(Location loc) {
        VCWorld ret = worlds.get(loc.getWorld().getName());
        return ret;
    }

    public static VCWorld getWorld(World w) {
        return worlds.get(w.getName());
    }

    public static void createWorld(VCWorld w) {
        try {
            PreparedStatement s = conn.getPreparedStatement("INSERT INTO `vc_worlds` (`name`, `paused`, `time`) VALUES (?, ?, ?);");
            s.setString(1, w.getWorld().getName());
            s.setBoolean(2, w.isTimePaused());
            s.setString(3, String.valueOf(w.getTimePauseMoment()));
            s.executeUpdate();
            conn.closeStatment(s);
            worlds.put(w.getWorld().getName(), w);
            VirusCmd.getInstance().getLogger().info("World " + w.getWorld().getName() + " was put into the database.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void save() {
        int saved = worlds.size();
        for (VCWorld w : worlds.values()) {
            update(w);
        }
        worlds.clear();
        VirusCmd.getInstance().getLogger().info("Saved " + saved + " worlds!");
    }

    public static void unloadWorld(VCWorld vcw) {
        worlds.remove(vcw.getWorld().getName());
    }

    private static void update(VCWorld w) {
        try {
            PreparedStatement st = conn.getPreparedStatement("UPDATE `vc_worlds` SET `paused` = ?, `time` = ? WHERE `name` = ?;");
            st.setBoolean(1, w.isTimePaused());
            st.setString(2, String.valueOf(w.getTimePauseMoment()));
            st.setString(3, w.getWorld().getName());
            st.executeUpdate();
            conn.closeStatment(st);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}

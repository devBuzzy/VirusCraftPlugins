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

import de.TheJeterLP.Bukkit.VirusCraftTools.Database.DataConnection;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.thejeterlp.bukkit.viruscmd.VCHelper;
import de.thejeterlp.bukkit.viruscmd.VirusCmd;
import de.thejeterlp.bukkit.viruscmd.player.VCPlayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author TheJeterLP
 */
public class SpawnManager {

    private static final HashMap<String, Spawn> spawns = new HashMap<>();
    private static final DataConnection conn = VCHelper.getDatabase();

    public static void init() {
        spawns.clear();
        try {
            PreparedStatement st = conn.getPreparedStatement("SELECT * FROM `vc_spawns`");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                SpawnType type = SpawnType.valueOf(rs.getString("type").toUpperCase());
                String group = rs.getString("group");
                Location loc = Utils.deserialLocation(rs.getString("location"));
                Spawn s = new Spawn(loc, type, group);
                spawns.put(group, s);
            }
            conn.closeResultSet(rs);
            conn.closeStatment(st);

            boolean found = false;
            for (Spawn h : spawns.values()) {
                if (h.getType() == SpawnType.DEFAULT) found = true;
            }

            if (!found) {
                try {
                    Spawn spawn = new Spawn(Bukkit.getWorlds().get(0).getSpawnLocation(), SpawnType.DEFAULT, "global_spawn");
                    spawns.put("global_spawn", spawn);
                    PreparedStatement pst = conn.getPreparedStatement("INSERT INTO `vc_spawns` (`type`, `group`, `location`) VALUES (?, ?, ?);");
                    pst.setString(1, spawn.getType().toString());
                    pst.setString(2, "global_spawn");
                    pst.setString(3, Utils.serialLocation(spawn.getLocation()));
                    pst.executeUpdate();
                    conn.closeStatment(pst);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            VirusCmd.getInstance().getLogger().info("Loaded " + spawns.size() + " spawns!");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<String> getSpawns() {
        List<String> ret = new ArrayList<>();
        for (String s : spawns.keySet()) {
            if (spawns.get(s) == null) continue;
            ret.add(s);
        }
        return ret;
    }

    public static Spawn getDefaultSpawn() {
        Spawn spawn = spawns.get("global_spawn");
        return spawn;
    }

    public static Spawn getSpawn(String group) {
        return spawns.get(group);
    }

    public static Spawn getSpawn(VCPlayer p) {
        Spawn s = getDefaultSpawn();

        for (Spawn sp : spawns.values()) {
            if (sp == null) continue;
            if (sp.getType() == SpawnType.DEFAULT) {
                s = sp;
                break;
            }
        }

        for (String g : spawns.keySet()) {
            if (g.equalsIgnoreCase(p.getGroup())) {
                if (spawns.get(g) == null) continue;
                s = spawns.get(g);
                break;
            }
        }

        return s;
    }

    public static void deleteSpawn(Spawn s) {
        if (s.getType() == SpawnType.DEFAULT) return;
        try {
            PreparedStatement st = conn.getPreparedStatement("DELETE FROM `vc_spawns` WHERE `group` = ?;");
            st.setString(1, s.getGroup());
            st.executeUpdate();
            conn.closeStatment(st);
            spawns.remove(s.getGroup());
            spawns.put(s.getGroup(), null);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void save() {
        int saved = spawns.size();
        for (String group : spawns.keySet()) {
            Spawn s = spawns.get(group);
            if (s == null) {
                try {
                    PreparedStatement st = conn.getPreparedStatement("DELETE FROM `vc_spawns` WHERE `group` = ?;");
                    st.setString(1, group);
                    st.executeUpdate();
                    conn.closeStatment(st);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    PreparedStatement st = conn.getPreparedStatement("UPDATE `vc_spawns` SET `location` = ? WHERE `group` = ?;");
                    st.setString(1, Utils.serialLocation(s.getLocation()));
                    st.setString(2, group);
                    st.executeUpdate();
                    conn.closeStatment(st);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        spawns.clear();
        VirusCmd.getInstance().getLogger().info("Saved " + saved + " spawns!");
    }

    public static Spawn getGroupSpawn(String group) {
        return spawns.get(group);
    }

    public static void createSpawn(Spawn s) {
        try {
            PreparedStatement pst = conn.getPreparedStatement("INSERT INTO `vc_spawns` (`type`, `group`, `location`) VALUES (?, ?, ?);");
            pst.setString(1, s.getType().toString());
            pst.setString(2, s.getGroup());
            pst.setString(3, Utils.serialLocation(s.getLocation()));
            pst.executeUpdate();
            conn.closeStatment(pst);
            spawns.put(s.getGroup(), s);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}

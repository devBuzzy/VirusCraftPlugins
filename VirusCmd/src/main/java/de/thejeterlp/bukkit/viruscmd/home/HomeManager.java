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

import de.thejeterlp.bukkit.viruscmd.VCHelper;
import de.thejeterlp.bukkit.viruscmd.VirusCmd;
import de.thejeterlp.bukkit.viruscmd.player.PlayerManager;
import de.thejeterlp.bukkit.viruscmd.player.VCPlayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class HomeManager {

    private static final HashMap<Integer, HashMap<String, Home>> homes = new HashMap<>();

    public static Home getHome(Player p, String name) {
        VCPlayer player = PlayerManager.getVCPlayer(p);
        if (player == null) return null;
        if (!homes.containsKey(player.getID())) return null;
        return homes.get(player.getID()).get(name);
    }

    public static void deleteHome(Home h) {
        HashMap<String, Home> home = homes.get(h.getOwner().getID());
        if (!home.containsKey(h.getName()) || home.get(h.getName()) == null) return;
        home.remove(h.getName());
        home.put(h.getName(), null);
    }

    public static HashMap<String, Home> getHomes(Player p) {
        VCPlayer player = PlayerManager.getVCPlayer(p);
        if (player == null) return new HashMap<>();
        if (!homes.containsKey(player.getID())) return new HashMap<>();
        HashMap<String, Home> ret = new HashMap<>();
        for (String name : homes.get(player.getID()).keySet()) {
            Home home = homes.get(player.getID()).get(name);
            if (home == null) continue;
            ret.put(name, home);
        }
        return ret;
    }

    public static void createHome(Player owner, String name) {
        Location target = owner.getLocation();
        VCPlayer player = PlayerManager.getVCPlayer(owner);
        if (player == null) return;
        Home h = new Home(target, player, name);
        if (!homes.containsKey(player.getID())) homes.put(player.getID(), new HashMap<String, Home>());
        homes.get(player.getID()).put(name, h);
    }

    public static void init() throws SQLException {
        Statement s = VCHelper.getDatabase().getStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM `vc_homes`");
        int loaded = 0;
        
        while (rs.next()) {
            VCPlayer player = PlayerManager.getVCPlayer(rs.getInt("playerid"));
            if (player == null) continue;
            Home h = new Home(rs.getString("location"), player, rs.getString("name"), rs.getInt("id"));
            if (!homes.containsKey(player.getID())) homes.put(player.getID(), new HashMap<String, Home>());
            homes.get(player.getID()).put(h.getName(), h);
            loaded++;
        }
         
        VCHelper.getDatabase().closeStatment(s);
        VCHelper.getDatabase().closeResultSet(rs);
        VirusCmd.getInstance().getLogger().info("Loaded " + loaded + " homes!");
    }

    public static void save() {
        if (homes.isEmpty()) return;
        int saved = 0;
        for (final Integer id : homes.keySet()) {
            for (final String name : homes.get(id).keySet()) {
                Home h = homes.get(id).get(name);
                saved++;
                if (h == null) {
                    try {
                        PreparedStatement s = VCHelper.getDatabase().getPreparedStatement("DELETE FROM `vc_homes` WHERE `playerid` = ? AND `name` = ?;");
                        s.setInt(1, id);
                        s.setString(2, name);
                        s.executeUpdate();
                        VCHelper.getDatabase().closeStatment(s);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    continue;
                }

                try {
                    PreparedStatement s = VCHelper.getDatabase().getPreparedStatement("SELECT `id` FROM `vc_homes` WHERE `id` = ? LIMIT 1;");
                    s.setInt(1, h.getID());
                    ResultSet rs = s.executeQuery();

                    if (rs.next()) {
                        PreparedStatement sta = VCHelper.getDatabase().getPreparedStatement("UPDATE `vc_homes` SET `location` = ? WHERE `id` = ?;");
                        sta.setString(1, h.getSerializedLocation());
                        sta.setInt(2, h.getID());
                        sta.executeUpdate();
                        VCHelper.getDatabase().closeStatment(sta);
                    } else {
                        PreparedStatement sta = VCHelper.getDatabase().getPreparedStatement("INSERT INTO `vc_homes` (`id`, `playerid`, `location`, `name`) VALUES (?, ?, ?, ?);");
                        sta.setInt(1, h.getID());
                        sta.setInt(2, id);
                        sta.setString(3, h.getSerializedLocation());
                        sta.setString(4, h.getName());
                        sta.executeUpdate();
                        VCHelper.getDatabase().closeStatment(sta);
                    }

                    VCHelper.getDatabase().closeResultSet(rs);
                    VCHelper.getDatabase().closeStatment(s);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        homes.clear();
        VirusCmd.getInstance().getLogger().info("Saved " + saved + " homes!");
    }

}

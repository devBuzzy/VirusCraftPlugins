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

import de.TheJeterLP.Bukkit.VirusCraftTools.Database.DataConnection;
import de.thejeterlp.bukkit.viruscmd.VCHelper;
import de.thejeterlp.bukkit.viruscmd.VirusCmd;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class PlayerManager {
    
    private static final HashMap<UUID, VCPlayer> players = new HashMap<>();
    private static final DataConnection conn = VCHelper.getDatabase();
    
    public static HashMap<UUID, VCPlayer> getPlayers() {
        return players;
    }
    
    public static void init() {
        players.clear();
        try {
            PreparedStatement s = conn.getPreparedStatement("SELECT `id` FROM `vc_player`");
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                SQLPlayer p = new SQLPlayer(rs.getInt("id"));
                VCPlayer vcp = p.convert();
                players.put(vcp.getUUID(), vcp);
            }
            VirusCmd.getInstance().getLogger().info("Loaded " + players.size() + " players!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void save() {
        int saved = players.size();       
        for (UUID uuid : players.keySet()) {
            VCPlayer p = players.get(uuid);
            update(p);
        }
        players.clear();
        VirusCmd.getInstance().getLogger().info("Saved " + saved + " players!");
    }
    
    private static void update(VCPlayer p) {
        try {
            PreparedStatement st = conn.getPreparedStatement("UPDATE `vc_player` SET `displayname` = ?, `god` = ?, `invisible` = ?, `commandwatcher` = ?, `spy` = ?, `fly` = ?, `muted` = ? WHERE `id` = ?;");
            st.setString(1, p.getDisplayName());
            st.setBoolean(2, p.god());
            st.setBoolean(3, p.invisible());
            st.setBoolean(4, p.commandwatcher());
            st.setBoolean(5, p.spy());
            st.setBoolean(6, p.fly());
            st.setBoolean(7, p.isMuted());
            st.setInt(8, p.getID());
            st.executeUpdate();
            conn.closeStatment(st);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static VCPlayer getVCPlayer(OfflinePlayer p) {
        return players.get(p.getUniqueId());
    }
    
    public static VCPlayer getVCPlayer(int id) {
        for (UUID u : players.keySet()) {
            VCPlayer p = players.get(u);
            if (p.getID() == id) return p;
        }
        return null;
    }
    
    public static void unload(VCPlayer vcp) {
        update(vcp);
        if (players.containsKey(vcp.getUUID()))
            players.remove(vcp.getUUID());
    }
    
    public static void insert(SQLPlayer p) {
        VCPlayer op = p.convert();
        players.put(op.getUUID(), op);
    }
    
    public static boolean hasPlayedBefore(Player p) {
        return getVCPlayer(p) != null;
    }
    
}

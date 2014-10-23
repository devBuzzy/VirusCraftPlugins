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

import de.TheJeterLP.Bukkit.VirusCraftTools.Database.ConnectionManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class SQLPlayer {

    public SQLPlayer(int id) {
        if (id > nextId) nextId = id;
        nextId++;
        this.id = id;
    }

    public SQLPlayer(Player op) throws SQLException {
        this.id = nextId;
        nextId++;
        PreparedStatement s = conn.getPreparedStatement("INSERT INTO `vc_player` (`id`, `uuid`, `displayname`, `god`, `invisible`, `commandwatcher`, `spy`, `fly`, `muted`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
        s.setInt(1, id);
        s.setString(2, op.getUniqueId().toString());
        s.setString(3, op.getName());
        s.setBoolean(4, false);
        s.setBoolean(5, false);
        s.setBoolean(6, false);
        s.setBoolean(7, false);
        s.setBoolean(8, false);
        s.setBoolean(9, false);
        s.executeUpdate();
        conn.closeStatment(s);
    }

    private final int id;
    private static int nextId = 1;
    private final ConnectionManager conn = VCplugin.inst().getConnectionManager();

    public int getID() {
        return this.id;
    }

    public VCPlayer convert() {
        try {
            PreparedStatement s = conn.getPreparedStatement("SELECT * FROM `vc_player` WHERE `id` = ?;");
            s.setInt(1, id);
            ResultSet rs = s.executeQuery();
            UUID uuid = UUID.fromString(rs.getString("uuid"));
            boolean god = rs.getBoolean("god");
            boolean inv = rs.getBoolean("invisible");
            boolean cw = rs.getBoolean("commandwatcher");
            boolean spy = rs.getBoolean("spy");
            boolean fly = rs.getBoolean("fly");
            String name = rs.getString("displayname");
            boolean muted = rs.getBoolean("muted");
            OnlinePlayer ret = new OnlinePlayer(id, uuid, name, god, inv, cw, spy, fly, muted);
            conn.closeResultSet(rs);
            conn.closeStatment(s);
            return ret;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}

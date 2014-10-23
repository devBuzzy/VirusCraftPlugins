/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thejeterlp.shoplogin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * @author TheJeterLP
 */
public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

            @Override
            public void run() {
                try {
                    PreparedStatement sta = Main.getDB().getPreparedStatement("SELECT bought FROM user WHERE uuid = ? LIMIT 1;");
                    sta.setString(1, e.getPlayer().getUniqueId().toString());
                    ResultSet rs = sta.executeQuery();
                    if (rs.next()) {
                        if (rs.getString("bought").trim().isEmpty()) {
                            Main.getDB().closeResultSet(rs);
                            Main.getDB().closeStatement(sta);
                        } else {
                            String[] parts = rs.getString("bought").split(";;");
                            Main.getDB().closeResultSet(rs);
                            Main.getDB().closeStatement(sta);
                            if (Config.SERVER_NAME.getString().equalsIgnoreCase(parts[0])) {
                                PermissionsEx.getUser(e.getPlayer()).setGroups(new String[]{parts[1]});
                                e.getPlayer().sendMessage("§aYou have been promoted to " + parts[1] + "! Thanks for your purchase :)");
                                PreparedStatement stat = Main.getDB().getPreparedStatement("UPDATE user SET bought = ? WHERE uuid = ?;");
                                stat.setString(1, "");
                                stat.setString(2, e.getPlayer().getUniqueId().toString());
                                stat.executeUpdate();
                                Main.getDB().closeStatement(stat);
                            }                           
                        }
                    }

                    PreparedStatement st = Main.getDB().getPreparedStatement("UPDATE user SET rank = ? WHERE uuid = ?;");
                    st.setString(1, Main.getInstance().getChat().getPlayerGroups(e.getPlayer())[0]);
                    st.setString(2, e.getPlayer().getUniqueId().toString());
                    st.executeUpdate();
                    Main.getDB().closeStatement(st);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) throws SQLException {
        PreparedStatement st = Main.getDB().getPreparedStatement("UPDATE user SET rank = ? WHERE uuid = ?;");
        st.setString(1, Main.getInstance().getChat().getPlayerGroups(e.getPlayer())[0]);
        st.setString(2, e.getPlayer().getUniqueId().toString());
        st.executeUpdate();
        Main.getDB().closeStatement(st);
    }

    @EventHandler
    public void onKick(final PlayerKickEvent e) throws SQLException {
        PreparedStatement st = Main.getDB().getPreparedStatement("UPDATE user SET rank = ? WHERE uuid = ?;");
        st.setString(1, Main.getInstance().getChat().getPlayerGroups(e.getPlayer())[0]);
        st.setString(2, e.getPlayer().getUniqueId().toString());
        st.executeUpdate();
        Main.getDB().closeStatement(st);
    }

}

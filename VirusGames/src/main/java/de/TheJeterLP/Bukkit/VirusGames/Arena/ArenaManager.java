package de.TheJeterLP.Bukkit.VirusGames.Arena;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager.LogType;
import de.TheJeterLP.Bukkit.VirusGames.Bukkit.VirusGames;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ArenaManager {

    private static final ArrayList<Arena> arenas = new ArrayList<>();

    public static void setupArenas() {
        try {
            Statement st = VirusGames.getDB().getStatement();
            ResultSet rs = st.executeQuery("SELECT `id` FROM `maps`");
            while (rs.next()) {
                load(rs.getInt("id"));
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            VirusGames.getMessageManager().log(LogType.SEVERE, "MYSQL_ERROR Couldn't load the arenas! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean exsist(int id) {
        try {
            PreparedStatement st = VirusGames.getDB().getPreparedStatement("SELECT `id` FROM `maps` WHERE `id` = ? LIMIT 1;");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            boolean next = rs.next();
            st.close();
            rs.close();
            return next;
        } catch (SQLException e) {
            return true;
        }
    }

    public static Arena addArena(Arena arena) {
        return arena;
    }

    public static Arena getArena(int id) {
        for (Arena a : arenas) {
            if (a.getID() == id) return a;
        }
        return null;
    }

    public static Arena getArena(Player p) {
        for (Arena a : arenas) {
            if (a.containsPlayer(p)) return a;
        }
        return null;
    }

    public static ArrayList<Arena> getArenas() {
        return arenas;
    }

    private static void unload(int id) {
        for (int i = 0; i < arenas.size(); i++) {
            Arena a = getArena(id);
            if (a.getID() == id) {
                if (a.getState() != ArenaState.WAITING) {
                    a.stop(null);
                }
                arenas.remove(a);
                break;
            }
        }
    }

    public static void load(int id) {
        try {
            arenas.add(new Arena(id));
            VirusGames.getMessageManager().log(LogType.INFO, "Loading arena  (" + id + ")!");
        } catch (SQLException e) {
            VirusGames.getMessageManager().log(LogType.SEVERE, "MYSQL_ERROR Counldn't load the arenas! error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean delete(int id) {
        unload(id);
        try {
            PreparedStatement st = VirusGames.getDB().getPreparedStatement("DELETE FROM `maps` WHERE `id` = ?;");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean loaded(int id) {
        return getArena(id) != null;
    }

    public static boolean create(int id, int numPlayers, String map) {
        try {
            PreparedStatement st = VirusGames.getDB().getPreparedStatement("INSERT INTO `maps` (`id`, `status`, `map`, `lobby`, `player`, `virus`, `sign`, `numPlayers`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            st.setInt(1, id);
            st.setString(2, ArenaState.WAITING.toString());
            st.setString(3, map);
            st.setInt(4, 0);
            st.setInt(5, 0);
            st.setInt(6, 0);
            st.setInt(7, 0);
            st.setInt(8, numPlayers);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
   
}

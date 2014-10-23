package de.TheJeterLP.Bukkit.StarShop.Database;

import de.TheJeterLP.Bukkit.StarShop.Starshop;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class MySQL {

    private static Connection conn;
    private static final String table = "stars_new";

    public static void init() throws SQLException {
        Starshop.getInstance().getDataFolder().mkdirs();
        loadDriver();
        setUp();
    }

    public static Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    public static PreparedStatement getPreparedStatement(String query) throws SQLException {
        return getConnection().prepareStatement(query);
    }

    private static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            String password = Config.MYSQL_PASSWORD.getString();
            String user = Config.MYSQL_USER.getString();
            String Database = Config.MYSQL_DATABASE.getString();
            String port = Config.MYSQL_PORT.getString();
            String host = Config.MYSQL_HOST.getString();
            String dsn = "jdbc:mysql://" + host + ":" + port + "/" + Database;
            conn = DriverManager.getConnection(dsn, user, password);
        }
        return conn;
    }

    public static void closeConnection() throws SQLException {
        if (conn == null || conn.isClosed()) return;
        conn.close();
    }

    private static void closeStatement(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    public static void setUp() throws SQLException {
        final Statement st = getStatement();
        st.execute("CREATE TABLE IF NOT EXISTS " + table + " (`uuid` varchar(64) PRIMARY KEY NOT NULL DEFAULT '0', `stars` INTEGER NOT NULL DEFAULT '0');");
        closeStatement(st);
    }

    public static void addPlayer(final Player p) throws SQLException {
        final PreparedStatement stmnt = getPreparedStatement("INSERT INTO " + table + " (`uuid`, `stars`) VALUES (?, ?);");
        stmnt.setString(1, p.getUniqueId().toString());
        stmnt.setInt(2, 0);
        stmnt.executeUpdate();
        closeStatement(stmnt);
    }

    public static void setStars(final Player player, final int stars) throws SQLException {
        final PreparedStatement stmnt = getPreparedStatement("UPDATE " + table + " SET `stars` = ? WHERE `uuid` = ?;");
        stmnt.setInt(1, stars);
        stmnt.setString(2, player.getUniqueId().toString());
        stmnt.executeUpdate();
        closeStatement(stmnt);
    }

    public static int getStars(Player player) throws SQLException {
        int ret = -1;
        PreparedStatement stmnt = getPreparedStatement("SELECT `stars` FROM " + table + " WHERE `uuid` = ?");
        stmnt.setString(1, player.getUniqueId().toString());
        ResultSet rs = stmnt.executeQuery();
        while (rs.next()) {
            ret = rs.getInt("stars");
        }
        closeResultSet(rs);
        return ret;
    }

    public static void removeStars(Player player, int stars) throws SQLException {
        int s = getStars(player);
        if (s == -1) {
            addPlayer(player);
        }
        setStars(player, s - stars);
    }

    public static void addStars(Player player, int stars) throws SQLException {
        int s = getStars(player);
        if (s == -1) {
            addPlayer(player);
        }
        setStars(player, s + stars);
    }

    public static boolean hasStars(Player player, int stars) throws SQLException {
        int s = getStars(player);
        if (s == -1) {
            return false;
        }
        return s >= stars;
    }
}

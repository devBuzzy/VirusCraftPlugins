package de.TheJeterLP.Bukkit.VirusCraftTools.Database.Games;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Game;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author TheJeterLP
 */
public class Database {

    public Database(Game g) {
        this.g = g;
        this.folder = new File(VCplugin.inst().getDataFolder().getAbsolutePath() + File.separator + "addons" + File.separator + "Games");
        this.db = new File(folder, g.getDBName());
    }

    private final Game g;
    private final File folder;
    private final File db;
    private Connection conn;

    public File getDatabaseFile() {
        return db;
    }

    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            reactivateConnection();
        }
        return conn;
    }

    public Statement getStatement() {
        try {
            return getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PreparedStatement getPreparedStatement(String query) {
        try {
            return getConnection().prepareStatement(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setup() throws SQLException {
        folder.mkdirs();
        loadDriver();
        conn = getConnection();
        String query = "";
        switch (g) {
            case CAKEPOKE:
                query = "CREATE TABLE IF NOT EXISTS `maps` ("
                        + "`id` INTEGER NOT NULL DEFAULT '0',"
                        + "`status` varchar(64) NOT NULL DEFAULT 'disabled',"
                        + "`lobby` varchar(500) NOT NULL DEFAULT 'lobbyloc',"
                        + "`spawn` varchar(500) NOT NULL DEFAULT 'spawn',"
                        + "`sign` varchar(500) NOT NULL DEFAULT 'sign',"
                        + "`numplayers` INTEGER NOT NULL DEFAULT '0'"
                        + ");";
                break;
            case SPLEEF:
                query = "CREATE TABLE IF NOT EXISTS `maps` ("
                        + "`id` INTEGER NOT NULL DEFAULT '0',"
                        + "`status` varchar(64) NOT NULL DEFAULT 'disabled',"
                        + "`lobby` varchar(500) NOT NULL DEFAULT 'lobbyloc',"
                        + "`spawn` varchar(500) NOT NULL DEFAULT 'spawn',"
                        + "`sign` varchar(500) NOT NULL DEFAULT 'sign',"
                        + "`numplayers` INTEGER NOT NULL DEFAULT '0'"
                        + ");";
                break;
            case VILLAGERHUNT:
                query = "CREATE TABLE IF NOT EXISTS `maps` ("
                        + "`id` INTEGER NOT NULL DEFAULT '0',"
                        + "`status` varchar(64) NOT NULL DEFAULT 'disabled',"
                        + "`lobby` varchar(500) NOT NULL DEFAULT 'lobbyloc',"
                        + "`spawn` varchar(500) NOT NULL DEFAULT 'spawn',"
                        + "`sign` varchar(500) NOT NULL DEFAULT 'sign',"
                        + "`numplayers` INTEGER NOT NULL DEFAULT '0'"
                        + ");";
                break;
            case VIRUSGAMES:
                query = "CREATE TABLE IF NOT EXISTS `maps` ("
                        + "`id` INTEGER NOT NULL DEFAULT '0',"
                        + "`status` varchar(64) NOT NULL DEFAULT 'disabled',"
                        + "`map` varchar(64) NOT NULL DEFAULT 'map',"
                        + "`lobby` varchar(500) NOT NULL DEFAULT 'lobbyloc',"
                        + "`player` varchar(500) NOT NULL DEFAULT 'playerloc',"
                        + "`virus` varchar(500) NOT NULL DEFAULT 'virusloc',"
                        + "`sign` varchar(500) NOT NULL DEFAULT 'sign',"
                        + "`numplayers` INTEGER NOT NULL DEFAULT '0'"
                        + ");";
                break;
        }

        if (query.isEmpty()) return;
        getStatement().execute(query);
    }

    public void reactivateConnection() throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite://" + db.getAbsolutePath());
    }
}

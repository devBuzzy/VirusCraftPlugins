package de.TheJeterLP.Bukkit.VirusCraftTools.Database;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClogger;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author TheJeterLP
 */
public final class SQLiteConnection implements DataConnection {

    private Connection conn;

    public SQLiteConnection() throws SQLException {
        loadDriver();
        VClogger.log(MessageType.INFO, "Loaded the database!");
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection("jdbc:sqlite://" + VCplugin.inst().getDataFolder().getAbsolutePath() + File.separator + "Database.db");
        }
        return conn;
    }

    @Override
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void closeStatment(Statement stmnt) {
        if (stmnt != null) {
            try {
                stmnt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PreparedStatement getPreparedStatement(String query) throws SQLException {
        return getConnection().prepareStatement(query);
    }

    @Override
    public Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }
}

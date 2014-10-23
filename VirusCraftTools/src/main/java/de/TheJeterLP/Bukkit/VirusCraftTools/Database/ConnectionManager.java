package de.TheJeterLP.Bukkit.VirusCraftTools.Database;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Bukkit;

/**
 * @author TheJeterLP
 */
public final class ConnectionManager implements DataConnection {

    private DataConnection handler = null;

    public ConnectionManager() {
        try {
            handler = new SQLiteConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(VCplugin.inst());
        }
    }

    @Override
    public void loadDriver() throws SQLException {
        handler.loadDriver();
    }

    @Override
    public void closeConnection() throws SQLException {
        handler.closeConnection();
    }

    @Override
    public void closeResultSet(ResultSet rs) throws SQLException {
        handler.closeResultSet(rs);
    }

    @Override
    public void closeStatment(Statement stmnt) throws SQLException {
        handler.closeStatment(stmnt);
    }

    @Override
    public PreparedStatement getPreparedStatement(String query) throws SQLException {
        return handler.getPreparedStatement(query);
    }

    @Override
    public Statement getStatement() throws SQLException {
        return handler.getStatement();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return handler.getConnection();
    }
}

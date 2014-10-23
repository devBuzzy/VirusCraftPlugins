package de.TheJeterLP.Bukkit.VirusCraftTools.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author TheJeterLP
 */
public interface DataConnection {

    public void loadDriver() throws SQLException;

    public void closeConnection() throws SQLException;

    public void closeResultSet(ResultSet rs) throws SQLException;

    public void closeStatment(Statement stmnt) throws SQLException;

    public PreparedStatement getPreparedStatement(String query) throws SQLException;

    public Statement getStatement() throws SQLException;

    public Connection getConnection() throws SQLException;
}

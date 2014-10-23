/*   Copyright 2014 BossLetsPlays(Matthew Rogers), TheJeterLP (Joey Peter)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package de.thejeterlp.shoplogin;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {

    private Connection conn = null;

    public Database(String driver) {
        try {
            Class<?> driverClass = Class.forName(driver);
            Object o = driverClass.newInstance();
            if (!(o instanceof Driver)) {
                throw new IllegalArgumentException("Class is not an instance of the Driver class!");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Main.getInstance().getDataFolder().mkdirs();
         
        try {
            setup();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public final void setConnection(Connection conn) {
        this.conn = conn;
    }

    public final Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed())
            reactivateConnection();
        return conn;
    }

    public final void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed())
            conn.close();
    }

    public final void closeStatement(Statement s) throws SQLException {
        if (s != null)
            s.close();

    }

    public final void closeResultSet(ResultSet rs) throws SQLException {
        if (rs != null)
            rs.close();
    }

    public final Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    public final PreparedStatement getPreparedStatement(String query) throws SQLException {
        return getConnection().prepareStatement(query);
    }

    public final void executeStatement(String query) throws SQLException {
        Statement s = getStatement();
        s.execute(query);
        closeStatement(s);
    }

    public abstract void reactivateConnection() throws SQLException;

    public abstract void setup() throws SQLException;

}

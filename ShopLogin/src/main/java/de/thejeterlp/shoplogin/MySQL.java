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

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends Database {

    public MySQL() {
        super("com.mysql.jdbc.Driver");
    }

    @Override
    public void setup() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS user ("
                + "ID INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, "
                + "uuid varchar(64) NOT NULL, "
                + "email varchar(64) NOT NULL, "
                + "password varchar(64) NOT NULL, "
                + "rank varchar(64) NOT NULL, "
                + "UNIQUE KEY `uuid` (`uuid`), "
                + "UNIQUE KEY `email` (`email`)"
                + ");";
        executeStatement(query);
    }

    @Override
    public void reactivateConnection() throws SQLException {
        String password = Config.MYSQL_PASSWORD.getString();
        String user = Config.MYSQL_USER.getString();
        String Database = Config.MYSQL_DATABASE.getString();
        String port = Config.MYSQL_PORT.getString();
        String host = Config.MYSQL_HOST.getString();
        String dsn = "jdbc:mysql://" + host + ":" + port + "/" + Database;
        setConnection(DriverManager.getConnection(dsn, user, password));
    }

}

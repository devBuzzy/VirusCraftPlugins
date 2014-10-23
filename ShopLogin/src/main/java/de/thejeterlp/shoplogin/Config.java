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

import java.io.File;
import java.io.IOException;
import net.minecraft.util.com.google.common.base.Joiner;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TheJeterLP
 */
public enum Config {

    MYSQL_HOST("SQL.MySQLHost", "localhost", "The MySQL host. (Not needed if I use SQLite)"),
    MYSQL_PORT("SQL.MySQLPort", 3306, "The MySQL port. (Not needed if I use SQLite)"),
    MYSQL_DATABASE("SQL.MySQLDatabase", "minecraft", "The MySQL database. (Not needed if I use SQLite)"),
    MYSQL_USER("SQL.MySQLUser", "root", "The MySQL user. (Not needed if I use SQLite)"),
    MYSQL_PASSWORD("SQL.MySQLPassword", "", "The MySQL password. (Not needed if I use SQLite)"),
    SERVER_NAME("Server.Name", "viruscraft", "the servername. (lockup or viruscraft)");

    private final Object value;
    private final String path;
    private final String comment;
    private static YamlConfiguration cfg;
    private static File f;

    private Config(String path, Object val, String comment) {
        this.path = path;
        this.value = val;
        this.comment = comment;
    }
    
    public int getInt() {
        return cfg.getInt(path);
    }

    public boolean getBoolean() {
        return cfg.getBoolean(path);
    }

    public double getDouble() {
        return cfg.getDouble(path);
    }

    public String getString() {
        return cfg.getString(path);
    }

    public Material getMaterial() {
        String item = cfg.getString(path);
        try {
            return Material.valueOf(item.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void load(JavaPlugin pl, String cfgFile) {
        pl.getDataFolder().mkdirs();
        f = new File(pl.getDataFolder(), cfgFile);
        cfg = YamlConfiguration.loadConfiguration(f);
        String header = pl.getDescription().getName() + " plugin by " + Joiner.on(", ").join(pl.getDescription().getAuthors()) + "\n";
        for (Config c : values()) {
            header += c.path + ": " + c.comment + "\n";
            if (!cfg.contains(c.path)) {
                cfg.set(c.path, c.value);
            }
        }
        cfg.options().header(header);
        try {
            cfg.save(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

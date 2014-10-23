package de.TheJeterLP.Bukkit.StarshopLite;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author TheJeterLP
 */
public enum Config {

        MYSQL_HOST("SQL.MySQLHost", "localhost"),
        MYSQL_PORT("SQL.MySQLPort", 3306),
        MYSQL_DATABASE("SQL.MySQLDatabase", "database"),
        MYSQL_USER("SQL.MySQLUser", "root"),
        MYSQL_PASSWORD("SQL.MySQLPassword", "root");

        private final Object value;
        private final String path;
        private static YamlConfiguration cfg;
        private static final File f = new File(StarshopLite.getInstance().getDataFolder(), "config.yml");

        private Config(String path, Object val) {
                this.path = path;
                this.value = val;
        }

        public String getPath() {
                return path;
        }

        public int getInt() {
                return cfg.getInt(path);
        }

        public Object getDefaultValue() {
                return value;
        }

        public float getFloat() {
                return Float.valueOf(cfg.getString(path));
        }

        public boolean getBoolean() {
                return cfg.getBoolean(path);
        }

        public String getString() {
                return cfg.getString(path);
        }

        public List<String> getStringList() {
                return cfg.getStringList(path);
        }

        public void set(Object value) {
                if (value instanceof String) {
                        String s = (String) value;
                        s = s.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
                        cfg.set(path, value);
                } else {
                        cfg.set(path, value);
                }
                save();
                reload(false);
        }

        public static void load() {
                StarshopLite.getInstance().getDataFolder().mkdirs();
                reload(false);
                for (Config c : values()) {
                        if (!cfg.contains(c.getPath())) {
                                c.set(c.getDefaultValue());
                        }
                }
                try {
                        cfg.save(f);
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
        }

        public static void save() {
                try {
                        cfg.save(f);
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
        }

        public static void reload(boolean complete) {
                if (!complete) {
                        cfg = YamlConfiguration.loadConfiguration(f);
                        return;
                }
                load();
        }
}

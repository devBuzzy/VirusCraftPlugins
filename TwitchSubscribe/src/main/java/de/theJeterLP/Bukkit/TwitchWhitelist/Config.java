package de.theJeterLP.Bukkit.TwitchWhitelist;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author TheJeterLP
 */
public enum Config {

    UNIQUE_ID("UniqueID", "siylisstv537016f90c3a2"),
    GROUP_AFTER_PROMOTE("GroupAfterPromote", "vip"),
    BYPASS_GROUPS("BypassPromoting",Arrays.asList("Owner", "Helper", "VCN"));
    
    private final Object value;
    private final String path;
    private static YamlConfiguration cfg;
    private static final File f = new File(Main.getInstance().getDataFolder(), "config.yml");

    private Config(String path, Object val) {
        this.path = path;
        this.value = val;
    }

    public String getPath() {
        return path;
    }

    public Object getDefaultValue() {
        return value;
    }

    public boolean getBoolean() {
        return cfg.getBoolean(path);
    }

    public String getString() {
        return cfg.getString(path).replaceAll("&((?i)[0-9a-fk-or])", "§$1");
    }

    public List<String> getStringList() {
        return cfg.getStringList(path);
    }

    public static void load() {
        Main.getInstance().getDataFolder().mkdirs();
        reload(false);
        for (Config c : values()) {
            if (!cfg.contains(c.getPath())) {
                c.set(c.getDefaultValue(), false);
            }
        }
        try {
            cfg.save(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void set(Object value, boolean save) {
        cfg.set(path, value);
        if (save) {
            try {
                cfg.save(f);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            reload(false);
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

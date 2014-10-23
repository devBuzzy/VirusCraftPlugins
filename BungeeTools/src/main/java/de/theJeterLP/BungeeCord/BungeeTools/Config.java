package de.theJeterLP.BungeeCord.BungeeTools;

import de.TheJeterLP.JeterConfig.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.md_5.bungee.protocol.ProtocolConstants;

/**
 * @author TheJeterLP
 */
public enum Config {

    VERSION("SupportedVersion", Arrays.asList(ProtocolConstants.MINECRAFT_14_11_a, ProtocolConstants.MINECRAFT_1_7_6, ProtocolConstants.MINECRAFT_1_7_2)),
    SERVER_IP("Server.IP", "127.0.0.1"),
    SERVER_PORT("Server.Port", 25565);

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
    
    public int getInt() {
        return cfg.getInt(path);
    }

    public List<String> getStringList() {
        return cfg.getStringList(path);
    }
    
    public Object getObject() {
        return cfg.get(path);
    }
    
    public String getString() {
        String s = cfg.getString(path);
        return s.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
    }

    public static void load() {
        Main.getInstance().getDataFolder().mkdirs();
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

    public void set(Object value) {
        cfg.set(path, value);
        try {
            cfg.save(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        reload(false);
    }

    public static void reload(boolean complete) {
        if (!complete) {
            cfg = YamlConfiguration.loadConfiguration(f);
            return;
        }
        load();
    }
}

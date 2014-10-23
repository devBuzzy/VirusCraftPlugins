package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
    MYSQL_PASSWORD("SQL.MySQLPassword", "root"),
    MESSAGES_JOIN("Messages.onJoin", "%player%&e joined the VirusCraftNetwork!"),
    MESSAGES_FIRSTJOIN("Messages.onFirstJoin", "%player%&e joined VirusCraftNetwork &6for the first time!"),
    MESSAGES_KICK("Messages.onKick", "%player%&e was kicked from VirusCraftNetwork!"),
    MESSAGES_QUIT("Messages.onQuit", "%player%&e left VirusCraftNetwork!"),
    ANNOUNCER_MESSAGES("Announcer.Messages", Arrays.asList("§9We now have a Teamspeak 3 server! IP: \n §3ts.vcnetwork.beastnode.net. \n§6It is hosted by our dev Joey!")),
    ANNOUNCER_TIME("Announcer.Time", new ServerTicks(120).getTicks()),
    ANNOUNCER_NOPLAYER("Announcer.DisplayIfNoPlayerIsOnline", false),
    BAR_MESSAGES("Bar.Messages", Arrays.asList("§4§lVirus§5§lCraft§1§lNetwork", "§aWe recently changed our hosts!", "§6Sorry for the long downTime!")),
    GROUP_PLAYERPLUS("Groups.Player+", 1500),
    GROUP_DONATOR("Groups.Donator", 3000),
    GROUP_ELITE("Groups.Elite", 5000),
    GROUP_ULTIMATE("Groups.Ultimate", 6500),    
    SETTINGS_MAIN_WORLD("Settings.Hub", VCplugin.inst().getServer().getWorlds().get(0).getName()),
    ;

    private final Object value;
    private final String path;
    private static YamlConfiguration cfg;
    private static final File f = new File(VCplugin.inst().getDataFolder(), "config.yml");

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
            s = Utils.replaceColors(s);
            cfg.set(path, value);
        } else {
            cfg.set(path, value);
        }
        save();
        reload(false);
    }

    public static void load() {
        VCplugin.inst().getDataFolder().mkdirs();
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

package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.util.com.google.common.base.Joiner;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author TheJeterLP
 */
public enum Config {

    AUTORESPAWN("Options.Autorespawn", true, "Should a player auto respawn after death?"),
    PLAYER_FORMAT("Options.PlayerFormat", "%prefix %player %suffix", "The format of %player%"),
    DEBUG("Options.DebugLog", false, "Should the debug log be enabled?"),
    BLOCKTAB("Options.BlockTabComplete", false, "Blocks /<tab> (Needs ProtocolLob)"),
    MESSAGES_JOIN("Messages.onJoin", "%player%&e joined the VirusCraftNetwork!", "Normal join message"),
    MESSAGES_FIRSTJOIN("Messages.onFirstJoin", "%player%&e joined VirusCraftNetwork &6for the first time!", "First join message"),
    MESSAGES_KICK("Messages.onKick", "%player%&e was kicked from VirusCraftNetwork!", "Kick message"),
    MESSAGES_QUIT("Messages.onQuit", "%player%&e left VirusCraftNetwork!", "Quit message"),
    ANNOUNCER_MESSAGES("Announcer.Messages", Arrays.asList("§9Feel free to join our teamspeak! The ip §ais vc-network.com"), "Autoannouncer messages"),
    ANNOUNCER_TIME("Announcer.Time", new ServerTicks(300).getTicks(), "After what amount comes the next message?"),
    ANNOUNCER_NOPLAYER("Announcer.DisplayIfNoPlayerIsOnline", false, "Should the announcer display the messages if no player is online?"),
    ANNOUNCER_PREFIX("Announcer.Prefix", "&6&l[]=====&4&lVirus&5&lCraft&1&lNetwork&6&l=====[]", "The header and footer of the messages"),
    BAR_MESSAGES("Bar.Messages", Arrays.asList("&4&lVirus&5&lCraft&1&lNetwork"), "The messages which will be shown in the health bar"),
    DISABLED_COMMANDS("Commands.Disabled", Arrays.asList("/op", "/deop", "/help", "/?", "/version", "/ver"), "The disabled commands"),
    FAKE_PL_LIST("Commands.FakePlugins", Arrays.asList("&a" + VCplugin.inst().getDescription().getName()), "Plugins which will be shown if a player types /plugins");

    private final Object value;
    private final String path;
    private final String desc;
    private static YamlConfiguration cfg;
    private static final File f = new File(VCplugin.inst().getDataFolder(), "config.yml");

    private Config(String path, Object val, String desc) {
        this.path = path;
        this.value = val;
        this.desc = desc;
    }

    private String getPath() {
        return path;
    }

    private Object getDefaultValue() {
        return value;
    }

    private String getDescription() {
        return desc;
    }

    public int getInt() {
        return cfg.getInt(path);
    }

    public boolean getBoolean() {
        return cfg.getBoolean(path);
    }

    public String getString() {
        return Utils.replaceColors(cfg.getString(path));
    }

    public List<String> getStringList() {
        List<String> ret = new ArrayList<>();
        for (String s : cfg.getStringList(path)) {
            ret.add(Utils.replaceColors(s));
        }
        return ret;
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
        String header = VCplugin.inst().getDescription().getName() + " plugin by " + Joiner.on(", ").join(VCplugin.inst().getDescription().getAuthors()) + System.lineSeparator();
        for (Config c : values()) {
            header += c.getPath() + ": " + c.getDescription() + System.lineSeparator();
            if (!cfg.contains(c.getPath())) {
                c.set(c.getDefaultValue());
            }
        }
        cfg.options().header(header);
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

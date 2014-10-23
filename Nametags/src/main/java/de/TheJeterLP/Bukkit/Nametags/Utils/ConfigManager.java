package de.TheJeterLP.Bukkit.Nametags.Utils;

import de.TheJeterLP.Bukkit.Nametags.Main;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class ConfigManager {

    private static final HashMap<String, String> groupPrefixes = new HashMap<>();
    private static final HashMap<String, String> groupSuffixes = new HashMap<>();
    private static final HashMap<UUID, String> userPrefixes = new HashMap<>();
    private static final HashMap<UUID, String> userSuffixes = new HashMap<>();

    public static void init() {
        Main.getInstance().getDataFolder().mkdirs();
        File cfgFile = new File(Main.getInstance().getDataFolder(), "config.yml");
        if (!cfgFile.exists()) Main.getInstance().saveResource("config.yml", false);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(cfgFile);
        groupPrefixes.clear();
        groupSuffixes.clear();
        userPrefixes.clear();
        userSuffixes.clear();
        if (cfg.getConfigurationSection("Group") != null) {
            for (String group : cfg.getConfigurationSection("Group").getKeys(false)) {
                String prefix = cfg.getString("Group." + group + ".prefix");
                String suffix = cfg.getString("Group." + group + ".suffix");
                if (prefix != null && !prefix.isEmpty()) groupPrefixes.put(group.toLowerCase(), prefix);
                if (suffix != null && !suffix.isEmpty()) groupSuffixes.put(group.toLowerCase(), suffix);
            }
        }

        if (cfg.getConfigurationSection("User") != null) {
            for (String uuid : cfg.getConfigurationSection("User").getKeys(false)) {
                String prefix = cfg.getString("User." + uuid + ".prefix");
                String suffix = cfg.getString("User." + uuid + ".suffix");
                if (prefix != null && !prefix.isEmpty()) userPrefixes.put(UUID.fromString(uuid), prefix);
                if (suffix != null && !suffix.isEmpty()) userSuffixes.put(UUID.fromString(uuid), suffix);
            }
        }
    }

    public static void save() {
        Main.getInstance().getDataFolder().mkdirs();
        File cfgFile = new File(Main.getInstance().getDataFolder(), "config.yml");
        if (!cfgFile.exists()) Main.getInstance().saveResource("config.yml", false);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(cfgFile);
        int gp = 0;
        int gs = 0;
        int up = 0;
        int us = 0;

        for (String group : groupPrefixes.keySet()) {
            cfg.set("Group." + group + ".prefix", groupPrefixes.get(group));
            gp++;
        }

        for (String group : groupSuffixes.keySet()) {
            cfg.set("Group." + group + ".suffix", groupSuffixes.get(group));
            gs++;
        }

        for (UUID uuid : userPrefixes.keySet()) {
            cfg.set("User." + uuid.toString() + ".prefix", userPrefixes.get(uuid));
            up++;
        }

        for (UUID uuid : userSuffixes.keySet()) {
            cfg.set("User." + uuid.toString() + ".suffix", userSuffixes.get(uuid));
            us++;
        }

        try {
            cfg.save(cfgFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Main.getInstance().getLogger().info("Saved " + gp + " groupPrefixes.");
        Main.getInstance().getLogger().info("Saved " + gs + " groupSuffixes.");
        Main.getInstance().getLogger().info("Saved " + up + " userPrefixes.");
        Main.getInstance().getLogger().info("Saved " + us + " userSuffixes.");
    }

    public static String getPrefix(final Player p) {
        if (userPrefixes.containsKey(p.getUniqueId())) return userPrefixes.get(p.getUniqueId());
        final String group = Main.getChat().getPlayerGroups(p)[0];
        if (!groupPrefixes.containsKey(group.toLowerCase())) return "";
        return groupPrefixes.get(group.toLowerCase());
    }

    public static String getSuffix(final Player p) {
        if (userSuffixes.containsKey(p.getUniqueId())) return userSuffixes.get(p.getUniqueId());
        final String group = Main.getChat().getPlayerGroups(p)[0];
        if (!groupSuffixes.containsKey(group.toLowerCase())) return "";
        return groupSuffixes.get(group.toLowerCase());
    }

    public static void setPrefix(Player p, String prefix) {
        if (userPrefixes.containsKey(p.getUniqueId())) userPrefixes.remove(p.getUniqueId());
        userPrefixes.put(p.getUniqueId(), prefix);
    }

    public static void setSuffix(Player p, String suffix) {
        if (userSuffixes.containsKey(p.getUniqueId())) userSuffixes.remove(p.getUniqueId());
        userSuffixes.put(p.getUniqueId(), suffix);
    }

}

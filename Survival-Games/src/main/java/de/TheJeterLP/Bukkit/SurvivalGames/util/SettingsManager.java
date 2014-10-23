package de.TheJeterLP.Bukkit.SurvivalGames.util;

import de.TheJeterLP.Bukkit.SurvivalGames.SurvivalGames;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.GameManager;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SettingsManager {

    private static final SettingsManager instance = new SettingsManager();
    private static final SurvivalGames p = SurvivalGames.getInstance();
    private FileConfiguration spawns, system, messages;
    private File Fspawns, Fsystem, Fmessages;

    public static SettingsManager getInstance() {
        return instance;
    }

    public void setup() {
        Fspawns = new File(p.getDataFolder(), "spawns.yml");
        Fsystem = new File(p.getDataFolder(), "system.yml");
        Fmessages = new File(p.getDataFolder(), "messages.yml");
        try {
            if (!Fspawns.exists()) Fspawns.createNewFile();
            if (!Fsystem.exists()) Fsystem.createNewFile();
            if (!Fmessages.exists()) loadFile("messages.yml");

        } catch (Exception e) {
            e.printStackTrace();
        }
        reloadSystem();
        saveSystemConfig();
        reloadSpawns();
        saveSpawns();
        reloadMessages();
        saveMessages();

    }

    public FileConfiguration getSystemConfig() {
        return system;
    }

    public FileConfiguration getSpawns() {
        return spawns;
    }

    public FileConfiguration getMessageConfig() {
        return messages;
    }

    public static World getGameWorld(int game) {
        if (SettingsManager.getInstance().getSystemConfig().getString("sg-system.arenas." + game + ".world") == null) {
            return null;

        }
        return p.getServer().getWorld(SettingsManager.getInstance().getSystemConfig().getString("sg-system.arenas." + game + ".world"));
    }

    public void reloadConfig() {
        p.reloadConfig();
    }

    public String getNextName(String name, int n) {
        File ff = new File(SurvivalGames.getInstance().getDataFolder(), name + ".old" + n);
        if (!ff.exists()) {
            return ff.getName();
        } else {
            return getNextName(name, n + 1);
        }
    }

    public void reloadSpawns() {
        spawns = YamlConfiguration.loadConfiguration(Fspawns);
        saveSpawns();
    }

    public void reloadSystem() {
        system = YamlConfiguration.loadConfiguration(Fsystem);
        saveSystemConfig();
    }

    public void reloadMessages() {
        messages = YamlConfiguration.loadConfiguration(Fmessages);
        saveMessages();
    }

    public void saveSystemConfig() {
        try {
            system.save(Fsystem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSpawns() {
        try {
            spawns.save(Fspawns);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void saveMessages() {
        try {
            messages.save(Fmessages);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getSpawnCount(int gameid) {
        return spawns.getInt("spawns." + gameid + ".count");
    }

    public Location getLobbySpawn() {
        try {
            return new Location(Bukkit.getWorld(system.getString("sg-system.lobby.spawn.world")),
                    system.getInt("sg-system.lobby.spawn.x"),
                    system.getInt("sg-system.lobby.spawn.y"),
                    system.getInt("sg-system.lobby.spawn.z"),
                    system.getInt("sg-system.lobby.spawn.yaw"),
                    system.getInt("sg-system.lobby.spawn.pitch"));
        } catch (Exception e) {
            return null;
        }
    }

    public Location getSpawnPoint(int gameid, int spawnid) {
        return new Location(getGameWorld(gameid),
                spawns.getInt("spawns." + gameid + "." + spawnid + ".x"),
                spawns.getInt("spawns." + gameid + "." + spawnid + ".y"),
                spawns.getInt("spawns." + gameid + "." + spawnid + ".z"),
                (float) spawns.getDouble("spawns." + gameid + "." + spawnid + ".yaw"),
                (float) spawns.getDouble("spawns." + gameid + "." + spawnid + ".pitch"));
    }

    public void setLobbySpawn(Location l) {
        system.set("sg-system.lobby.spawn.world", l.getWorld().getName());
        system.set("sg-system.lobby.spawn.x", l.getBlockX());
        system.set("sg-system.lobby.spawn.y", l.getBlockY());
        system.set("sg-system.lobby.spawn.z", l.getBlockZ());
        system.set("sg-system.lobby.spawn.yaw", l.getYaw());
        system.set("sg-system.lobby.spawn.pitch", l.getPitch());

    }

    public void setSpawn(int gameid, int spawnid, Location v) {
        spawns.set("spawns." + gameid + "." + spawnid + ".x", v.getBlockX());
        spawns.set("spawns." + gameid + "." + spawnid + ".y", v.getBlockY());
        spawns.set("spawns." + gameid + "." + spawnid + ".z", v.getBlockZ());
        spawns.set("spawns." + gameid + "." + spawnid + ".yaw", v.getYaw());
        spawns.set("spawns." + gameid + "." + spawnid + ".pitch", v.getPitch());

        if (spawnid > spawns.getInt("spawns." + gameid + ".count")) {
            spawns.set("spawns." + gameid + ".count", spawnid);
        }
        try {
            spawns.save(Fspawns);
        } catch (IOException e) {

        }
        GameManager.getInstance().getGame(gameid).addSpawn();

    }

    public void loadFile(String file) {
        SurvivalGames.getInstance().saveResource(file, true);

    }
}

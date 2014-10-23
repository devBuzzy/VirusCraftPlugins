package de.TheJeterLP.Bukkit.VillagerHunt.Arena;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import de.TheJeterLP.Bukkit.VillagerHunt.Bukkit.VillagerHunt;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager.PrefixType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import java.sql.PreparedStatement;
import java.util.Random;
import org.bukkit.*;
import org.bukkit.entity.*;

public final class Arena {

    private final int id, numPlayers;
    private final ArrayList<PlayerData> data = new ArrayList<>();
    private final Location lobbyPoint, spawn;
    private final Sign sign;
    private final Random r = new Random();

    private SpawnThread spawnThread;
    private int taskid, spawntask, countdown;
    private Villager villager = null;
    private ArenaState state = ArenaState.WAITING;

    protected Arena(int id) throws SQLException {
        this.id = id;
        final PreparedStatement st = VillagerHunt.getDB().getPreparedStatement("SELECT * FROM `maps` WHERE `id` = ? LIMIT 1;");
        st.setInt(1, id);
        final ResultSet rs = st.executeQuery();
        if (rs.next()) {
            this.numPlayers = rs.getInt("numPlayers");
            this.lobbyPoint = Utils.deserialLocation(rs.getString("lobby"));
            this.spawn = Utils.deserialLocation(rs.getString("spawn"));
            this.state = ArenaState.valueOf(rs.getString("status").toUpperCase());
            this.sign = (Sign) Utils.deserialLocation(rs.getString("sign")).getBlock().getState();
            updateStatusAndSign(state);
        } else {
            this.numPlayers = 0;
            this.lobbyPoint = null;
            this.spawn = null;
            this.sign = null;
        }
        st.close();
        rs.close();
    }

    public SpawnThread getSpawnThread() {
        return spawnThread;
    }

    public int getID() {
        return id;
    }

    public Sign getSign() {
        return sign;
    }

    public Location getSpawn() {
        return lobbyPoint;
    }

    public int getCurrentPlayers() {
        return data.size();
    }

    public void addPlayer(final Player p) {
        if (ArenaManager.getArena(p) != null) {
            VillagerHunt.getMessageManager().message(p, PrefixType.BAD, "You are already in an arena.");
            return;
        }

        if (getCurrentPlayers() >= numPlayers) {
            VillagerHunt.getMessageManager().message(p, PrefixType.INFO, "The arena is full. Please choose a different one.");
            return;
        }

        if (state == ArenaState.STARTED || state == ArenaState.WINNED) {
            VillagerHunt.getMessageManager().message(p, PrefixType.BAD, "The arena is already ingame.");
            return;
        }

        if (getPlayer(p) != null) {
            return;
        }

        if (state == ArenaState.WAITING) {
            start();
        }

        data.add(new PlayerData(p));
        p.teleport(lobbyPoint);
        for (PlayerData pd : data) {
            pd.getPlayer().sendMessage("The player " + p.getName() + " joined the arena. (" + data.size() + "/" + numPlayers + ")");
        }
        updateStatusAndSign(state);

    }

    public void removePlayer(Player p) {
        for (PlayerData pd : data) {
            if (state != ArenaState.WINNED)
                pd.getPlayer().sendMessage(ChatColor.GOLD + "The player " + p.getName() + " left the game. (" + (data.size() - 1) + "/" + numPlayers + ")");
        }
        PlayerData pl = getPlayerData(p);
        pl.restorePlayerData();
        p.teleport(getSpawn());
        undisguise(p);
        data.remove(pl);

        if (state != ArenaState.WINNED) {
            if (data.isEmpty()) {
                updateStatusAndSign(ArenaState.WINNED);
                stop("No players!");
            } else if (data.size() == 1) {
                updateStatusAndSign(ArenaState.WINNED);
                stop("No players!");
            }
            updateStatusAndSign(state);
        }

    }

    public ArenaState getState() {
        return this.state;
    }

    public Villager getVillager() {
        return villager;
    }

    public boolean containsPlayer(Player p) {
        return getPlayerData(p) != null;
    }

    public ArrayList<PlayerData> getDatas() {
        return data;
    }

    private PlayerData getPlayerData(Player p) {
        for (PlayerData d : data) {
            if (d.isForPlayer(p)) return d;
        }
        return null;
    }

    public Location getRandomSpawn() {
        Location base = spawn.clone();
        base.setX(base.getX() + r.nextInt(15));
        base.setZ(base.getZ() + r.nextInt(15));
        return base;
    }

    public void start() {
        updateStatusAndSign(ArenaState.COUNTDING_DOWN);
        taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(VillagerHunt.getInstance(), new Runnable() {
            private int countdown = 45;

            @Override
            public void run() {
                if (countdown > 0) {
                    for (PlayerData pd : data) {
                        pd.getPlayer().playNote(pd.getPlayer().getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.G));
                        pd.getPlayer().setExp(0);
                        pd.getPlayer().setLevel(countdown);
                    }

                    if (state == ArenaState.COUNTDING_DOWN) {
                        sign.setLine(1, state.getText() + "(" + countdown + ")");
                        sign.update(true);
                    }
                }

                if (countdown == 45 || countdown == 30 || countdown == 15 || countdown == 10 || countdown == 9 || countdown == 8 || countdown == 7 || countdown == 6 || countdown == 5 || countdown == 4 || countdown == 3 || countdown == 2 || countdown == 1) {
                    for (PlayerData pd : data) {
                        VillagerHunt.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GREEN + "Game starts in " + ChatColor.GOLD + countdown + ChatColor.GREEN + " seconds.");
                    }
                } else if (countdown <= 0) {
                    if (data.size() == 1 && numPlayers != 1) {
                        data.get(0).getPlayer().sendMessage("§cNot enough players joined your arena.");
                        Bukkit.getServer().getScheduler().cancelTask(taskid);
                        stop("Not enough players!");
                        return;
                    }
                    getSpawn().getWorld().setTime(15000);
                    spawnThread = new SpawnThread(id);
                    startCountdown();
                    for (PlayerData pd : data) {
                        pd.getPlayer().setLevel(0);
                        pd.getPlayer().playSound(pd.getPlayer().getLocation(), Sound.LEVEL_UP, 3, 1);
                        pd.getPlayer().teleport(spawn);
                        disguise(pd.getPlayer());
                        VillagerHunt.getMessageManager().message(pd.getPlayer(), PrefixType.INFO, "First zombie wave will come in 45 seconds!");
                    }
                    updateStatusAndSign(ArenaState.STARTED);
                    villager = (Villager) getSpawn().getWorld().spawnEntity(spawn, EntityType.VILLAGER);
                    getVillager().setCustomName(String.valueOf(id));
                    getVillager().setCustomNameVisible(false);
                    getVillager().setMaxHealth(30.0);
                    getVillager().setHealth(30.0);
                    getVillager().setCanPickupItems(false);
                    Villager.Profession prof = Villager.Profession.values()[r.nextInt(Villager.Profession.values().length)];
                    getVillager().setProfession(prof);
                    Bukkit.getServer().getScheduler().cancelTask(taskid);
                    spawntask = Bukkit.getScheduler().scheduleSyncRepeatingTask(VillagerHunt.getInstance(), spawnThread, 45 * 20, 45 * 20);

                }
                countdown--;
            }
        }, 0, 20);
    }

    public Zombie spawnZombie() {
        Zombie z = (Zombie) getSpawn().getWorld().spawnEntity(getRandomSpawn(), EntityType.ZOMBIE);
        z.setCustomNameVisible(false);
        z.setCustomName(String.valueOf(id));
        return z;
    }

    public void stop(String message) {
        updateStatusAndSign(ArenaState.WINNED);
        final ArrayList<PlayerData> pdata = new ArrayList<>();
        pdata.addAll(data);

        for (PlayerData pd : pdata) {
            VillagerHunt.getMessageManager().message(pd.getPlayer(), PrefixType.INFO, message);
            undisguise(pd.getPlayer());
            removePlayer(pd.getPlayer());
        }

        if (spawnThread != null) spawnThread.killZombies();

        if (villager != null && !villager.isDead()) {
            villager.damage(villager.getMaxHealth());
        }

        data.clear();
        Bukkit.getScheduler().cancelTask(taskid);
        Bukkit.getScheduler().cancelTask(spawntask);
        Bukkit.getScheduler().cancelTask(countdown);
        villager = null;
        spawnThread = null;
        updateStatusAndSign(ArenaState.WAITING);
    }

    public PlayerData getPlayer(Player p) {
        for (PlayerData pd : data) {
            if (pd.isForPlayer(p)) return pd;
        }
        return null;
    }

    public void updateStatusAndSign(ArenaState state) {
        this.state = state;
        this.sign.setLine(0, "[VH] - " + this.id);
        this.sign.setLine(1, state.getText());
        this.sign.setLine(2, ChatColor.RED + "" + getCurrentPlayers() + "/" + numPlayers);
        this.sign.update(true);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void disguise(final Player p) {
        boolean has = VCplugin.inst().getPermissions().has(p, "*");
        if (!has) {
            VCplugin.inst().getPermissions().playerAdd(p, "*");
        }
        p.performCommand("disguise irongolem");
        if (!has) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(VillagerHunt.getInstance(), new Runnable() {
                @Override
                public void run() {
                    VCplugin.inst().getPermissions().playerRemove(p, "*");
                }
            }, 2);
        }
    }

    public void undisguise(final Player p) {
        boolean has = VCplugin.inst().getPermissions().has(p, "*");
        if (!has) {
            VCplugin.inst().getPermissions().playerAdd(p, "*");
            p.performCommand("undisguise");
            Bukkit.getScheduler().scheduleSyncDelayedTask(VillagerHunt.getInstance(), new Runnable() {
                @Override
                public void run() {
                    VCplugin.inst().getPermissions().playerRemove(p, "*");
                }
            }, 2);
        } else {
            p.performCommand("undisguise");
        }
    }

    private void startCountdown() {
        countdown = Bukkit.getScheduler().scheduleSyncDelayedTask(VillagerHunt.getInstance(), new Runnable() {
            @Override
            public void run() {
                stop("The time is over!");
            }
        }, 24000);
    }

}

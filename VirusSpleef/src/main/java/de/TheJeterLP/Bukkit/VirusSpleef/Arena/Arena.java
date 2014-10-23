package de.TheJeterLP.Bukkit.VirusSpleef.Arena;

import de.TheJeterLP.Bukkit.StarShop.Database.MySQL;
import de.TheJeterLP.Bukkit.StarShop.ScoreboardFactory;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager.PrefixType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusSpleef.Bukkit.VirusSpleef;
import de.TheJeterLP.Bukkit.VirusSpleef.util.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public final class Arena {

    private final int id, numPlayers;
    private final ArrayList<PlayerData> data = new ArrayList<>();
    private final Location lobbyPoint, spawn;
    private final Sign sign;
    private final BlockStorage storage = new BlockStorage(this);

    private ArenaState state = ArenaState.WAITING;
    private int taskid;

    protected Arena(int id) throws SQLException {
        this.id = id;
        final PreparedStatement st = VirusSpleef.getDB().getPreparedStatement("SELECT * FROM `maps` WHERE `id` = ? LIMIT 1;");
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

    public int getID() {
        return id;
    }

    public BlockStorage getBlockStorage() {
        return this.storage;
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
            VirusSpleef.getMessageManager().message(p, PrefixType.BAD, "You are already in an arena.");
            return;
        }

        if (getCurrentPlayers() >= numPlayers) {
            VirusSpleef.getMessageManager().message(p, PrefixType.INFO, "The arena is full. Please choose a different one.");
            return;
        }

        if (state == ArenaState.STARTED || state == ArenaState.WINNED) {
            VirusSpleef.getMessageManager().message(p, PrefixType.BAD, "The arena is already ingame.");
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
            pd.getPlayer().sendMessage(ChatColor.GOLD + "The player " + p.getName() + " joined the arena. (" + data.size() + "/" + numPlayers + ")");
        }
        updateStatusAndSign(state);

    }

    public void removePlayer(Player p) {
        for (PlayerData pd : data) {
            if (state != ArenaState.WINNED)
                pd.getPlayer().sendMessage(ChatColor.GOLD + "The player " + p.getName() + " left the game. (" + (data.size() - 1) + "/" + numPlayers + ")");
        }
        VirusSpleef.getMessageManager().message(p, PrefixType.INFO, "You died!");
        PlayerData pl = getPlayerData(p);
        pl.restorePlayerData();
        p.teleport(getSpawn());
        data.remove(pl);

        if (state != ArenaState.WINNED) {
            if (data.isEmpty()) {
                updateStatusAndSign(ArenaState.WINNED);
                stop(null);
            } else if (data.size() == 1) {
                updateStatusAndSign(ArenaState.WINNED);
                stop(data.get(0).getPlayer());
            }
            updateStatusAndSign(state);
        }
    }

    public ArenaState getState() {
        return this.state;
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

    private Location getRandomSpawn() {
        Location base = this.spawn;
        Random r = new Random();
        boolean plus = r.nextBoolean();
        if (plus) {
            base.setX(base.getX() + r.nextInt(2));
            base.setZ(base.getZ() + r.nextInt(2));
        } else {
            base.setX(base.getX() - r.nextInt(2));
            base.setZ(base.getZ() - r.nextInt(2));
        }
        return base;
    }

    public void start() {
        updateStatusAndSign(ArenaState.COUNTDING_DOWN);

        taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(VirusSpleef.getInstance(), new Runnable() {
            private int countdown = 30;

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

                if (countdown == 30 || countdown == 15 || countdown == 10 || countdown == 9 || countdown == 8 || countdown == 7 || countdown == 6 || countdown == 5 || countdown == 4 || countdown == 3 || countdown == 2 || countdown == 1) {
                    for (PlayerData pd : data) {
                        VirusSpleef.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GREEN + "Game starts in " + ChatColor.GOLD + countdown + ChatColor.GREEN + " seconds.");
                    }
                } else if (countdown <= 0) {
                    if (data.size() == 1 && numPlayers != 1) {
                        data.get(0).getPlayer().sendMessage("§cNot enough players joined your arena.");
                        Bukkit.getServer().getScheduler().cancelTask(taskid);
                        stop(null);
                        return;
                    }

                    for (PlayerData pd : data) {
                        pd.getPlayer().setLevel(0);
                        pd.getPlayer().playSound(pd.getPlayer().getLocation(), Sound.LEVEL_UP, 3, 1);
                        pd.getPlayer().teleport(getRandomSpawn());
                        VirusSpleef.getMessageManager().message(pd.getPlayer(), PrefixType.INFO, "Start digging!");
                    }

                    updateStatusAndSign(ArenaState.STARTED);
                    Bukkit.getServer().getScheduler().cancelTask(taskid);
                }
                countdown--;
            }
        }, 0, 20);
    }

    public void stop(final Player winner) {
        updateStatusAndSign(ArenaState.WINNED);
        final ArrayList<PlayerData> pdata = new ArrayList<>();
        pdata.addAll(data);
        if (winner != null) {
            try {
                MySQL.addStars(winner, 75);
            } catch (SQLException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
            winner.sendMessage("§aYou got 75 stars for winning Spleef!");
            ScoreboardFactory.updateBoard(winner);
        }
        for (PlayerData pd : pdata) {
            if (winner != null)
                VirusSpleef.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GOLD + winner.getName() + " has won the game.");
            if (pd.getPlayer() != null)
                removePlayer(pd.getPlayer());
        }
        data.clear();
        storage.resetArena();
        Bukkit.getScheduler().cancelTask(this.taskid);
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
        this.sign.setLine(0, "[Spleef] - " + this.id);
        this.sign.setLine(1, state.getText());
        this.sign.setLine(2, ChatColor.RED + "" + getCurrentPlayers() + "/" + numPlayers);
        this.sign.update(true);
    }

    public void setWinner(Player p) {
        updateStatusAndSign(ArenaState.WINNED);
        stop(p);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

}

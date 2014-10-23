package de.TheJeterLP.Bukkit.CakePoke.Arena;

import de.TheJeterLP.Bukkit.CakePoke.Bukkit.CakePoke;
import de.TheJeterLP.Bukkit.StarShop.Database.MySQL;
import de.TheJeterLP.Bukkit.StarShop.ScoreboardFactory;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager.PrefixType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public final class Arena {

    private final int id, numPlayers;
    private int taskid, countdown;
    private final ArrayList<PlayerData> data = new ArrayList<>();
    private final Location lobbyPoint, spawn;
    private final Sign sign;
    private ArenaState state = ArenaState.WAITING;

    protected Arena(int id) throws SQLException {
        this.id = id;
        final PreparedStatement st = CakePoke.getDB().getPreparedStatement("SELECT * FROM `maps` WHERE `id` = ? LIMIT 1;");
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
            CakePoke.getMessageManager().message(p, PrefixType.BAD, "You are already in an arena.");
            return;
        }

        if (getCurrentPlayers() >= numPlayers) {
            CakePoke.getMessageManager().message(p, PrefixType.BAD, "The arena is full. Please choose a different one.");
            return;
        }

        if (state == ArenaState.STARTED || state == ArenaState.WINNED) {
            CakePoke.getMessageManager().message(p, PrefixType.BAD, "The arena is already ingame.");
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
            pd.getPlayer().sendMessage(ChatColor.GOLD + "The player " + p.getName() + " joined the game. (" + data.size() + "/" + numPlayers + ")");
        }
        updateStatusAndSign(state);

    }

    public void removePlayer(PlayerData pl) {
        for (PlayerData pd : data) {
            if (state != ArenaState.WINNED)
                pd.getPlayer().sendMessage(ChatColor.GOLD + "The player " + pl.getPlayer().getName() + " left the game. (" + (data.size()) + "/" + numPlayers + ")");
        }
        pl.restorePlayerData();
        pl.getPlayer().teleport(getSpawn());
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

    public void removePlayer(Player p) {
        PlayerData pd = getPlayer(p);
        if (pd == null) return;
        removePlayer(pd);
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

    public void start() {
        updateStatusAndSign(ArenaState.COUNTDING_DOWN);

        taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(CakePoke.getInstance(), new Runnable() {
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
                        CakePoke.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GREEN + "Game starts in " + ChatColor.GOLD + countdown + ChatColor.GREEN + " seconds.");
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
                        pd.getPlayer().teleport(spawn);
                    }
                    runCountdown(300);
                    updateStatusAndSign(ArenaState.STARTED);
                    Bukkit.getServer().getScheduler().cancelTask(taskid);
                }
                countdown--;
            }
        }, 0, 20);
    }

    public void stop(final Player winner) {
        updateStatusAndSign(ArenaState.WINNED);
        if (winner != null) {
            try {
                MySQL.addStars(winner, 100);
            } catch (SQLException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
            winner.sendMessage("§aYou got 100 stars for winning CakePoke!");
            ScoreboardFactory.updateBoard(winner);
        }
        List<PlayerData> pdata = new ArrayList<>();
        pdata.addAll(data);
        for (PlayerData pd : pdata) {
            if (winner != null) {
                CakePoke.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GOLD + winner.getName() + " has won the game.");
            } else {
                CakePoke.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GOLD + "The time is over!");
            }
            removePlayer(pd);
        }
        Bukkit.getScheduler().cancelTask(this.countdown);
        Bukkit.getScheduler().cancelTask(this.taskid);
        data.clear();
        pdata.clear();
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
        this.sign.setLine(0, "[CP] - " + this.id);
        this.sign.setLine(1, state.getText());
        this.sign.setLine(2, ChatColor.RED + "" + getCurrentPlayers() + "/" + numPlayers);
        this.sign.update(true);
    }

    public void setWinner(Player p) {
        updateStatusAndSign(ArenaState.WINNED);
        stop(p);
    }

    public void portBack(Player p) {
        p.teleport(spawn);
        CakePoke.getMessageManager().message(p, PrefixType.INFO, "You got teleported back to the start!");
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    private void runCountdown(int seconds) {
        countdown = Bukkit.getScheduler().scheduleSyncDelayedTask(CakePoke.getInstance(), new Runnable() {
            @Override
            public void run() {
                setWinner(null);
            }
        }, seconds * 20);
    }
}

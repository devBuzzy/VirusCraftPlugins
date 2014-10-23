package de.TheJeterLP.Bukkit.VirusGames.Arena;

import de.TheJeterLP.Bukkit.StarShop.Database.MySQL;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager.PrefixType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusGames.Bukkit.VirusGames;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public final class Arena {

    private final int id, numPlayers;
    private final String name;
    private final ArrayList<PlayerData> data = new ArrayList<>();
    private final Location lobbyPoint, player, virus;
    private final Sign sign;
    
    private int taskid, scoreboardId, task;
    private ArenaState state = ArenaState.WAITING;
    private ArenaScoreboard board;

    protected Arena(int id) throws SQLException {
        this.id = id;
        final PreparedStatement st = VirusGames.getDB().getPreparedStatement("SELECT * FROM `maps` WHERE `id` = ? LIMIT 1;");
        st.setInt(1, id);
        final ResultSet rs = st.executeQuery();
        if (rs.next()) {
            this.numPlayers = rs.getInt("numPlayers");
            this.lobbyPoint = Utils.deserialLocation(rs.getString("lobby"));
            this.player = Utils.deserialLocation(rs.getString("player"));
            this.virus = Utils.deserialLocation(rs.getString("virus"));
            this.sign = (Sign) Utils.deserialLocation(rs.getString("sign")).getBlock().getState();
            this.state = ArenaState.valueOf(rs.getString("status").toUpperCase());
            this.name = rs.getString("map");
            updateStatusAndSign(state);
        } else {
            this.numPlayers = 0;
            this.name = "unknown";
            this.lobbyPoint = null;
            this.player = null;
            this.virus = null;
            this.sign = null;
        }
        st.close();
        rs.close();

    }

    public ArenaScoreboard getScoreboard() {
        return board;
    }

    public int getViruses() {
        int ret = 0;
        for (PlayerData pd : data) {
            if (pd.getTeam() == Team.VIRUS) ret++;
        }
        return ret;
    }

    public int getPlayers() {
        int ret = 0;
        for (PlayerData pd : data) {
            if (pd.getTeam() == Team.PLAYER) ret++;
        }
        return ret;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Sign getSign() {
        return sign;
    }

    public Location getSpawn(Team team) {
        switch (team) {
            case LOBBY:
                return lobbyPoint;
            case PLAYER:
                return player;
            case VIRUS:
                return virus;
        }
        return null;
    }

    public void addPlayer(final Player p) {
        if (ArenaManager.getArena(p) != null) {
            VirusGames.getMessageManager().message(p, PrefixType.BAD, "You are already in an arena.");
            return;
        }

        if (data.size() >= numPlayers) {
            VirusGames.getMessageManager().message(p, PrefixType.INFO, "The arena is full. Please choose a different one.");
            return;
        }

        if (state == ArenaState.STARTED || state == ArenaState.WINNED) {
            VirusGames.getMessageManager().message(p, PrefixType.BAD, "The arena is already ingame.");
            return;
        }

        if (getPlayer(p) != null) {
            return;
        }

        if (state == ArenaState.WAITING) {
            start();
        }

        data.add(new PlayerData(p, Team.LOBBY));
        p.teleport(lobbyPoint);
        for (PlayerData pd : data) {
            pd.getPlayer().sendMessage(ChatColor.GOLD + "The player " + p.getName() + " joined the arena. (" + data.size() + "/" + numPlayers + ")");
        }
        VirusGames.getMessageManager().message(p, PrefixType.INFO, "Please pick your class now.");
        updateStatusAndSign(state);

    }

    public void removePlayer(Player p) {
        for (PlayerData pd : data) {
            if (state != ArenaState.WINNED)
                pd.getPlayer().sendMessage(ChatColor.GOLD + "The player " + p.getName() + " left the game. (" + (data.size() - 1) + "/" + numPlayers + ")");
        }

        PlayerData pl = getPlayerData(p);
        pl.restorePlayerData();
        data.remove(pl);
        p.teleport(sign.getLocation());

        if (state != ArenaState.WINNED) {
            if (data.isEmpty()) {
                updateStatusAndSign(ArenaState.WINNED);
                stop(null);
            } else if (data.size() == 1) {
                updateStatusAndSign(ArenaState.WINNED);
                stop(pl.getTeam().getOtherTeam());
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

    public void start() {
        updateStatusAndSign(ArenaState.COUNTDING_DOWN);
        taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(VirusGames.getInstance(), new Runnable() {
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
                        VirusGames.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GREEN + "Game starts in " + ChatColor.GOLD + countdown + ChatColor.GREEN + " seconds.");
                        if (countdown == 60 || countdown == 30 || countdown == 10) {
                            VirusGames.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GREEN + "########################");
                            VirusGames.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GREEN + "Map: " + ChatColor.GOLD + name);
                            VirusGames.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GREEN + "########################");
                        }
                    }
                } else if (countdown <= 0) {
                    if (data.size() == 1 && numPlayers != 1) {
                        data.get(0).getPlayer().sendMessage("§cNot enough players joined your arena.");
                        Bukkit.getServer().getScheduler().cancelTask(taskid);
                        stop(null);
                        return;
                    }
                    board = new ArenaScoreboard();
                    for (PlayerData pd : data) {
                        if (pd.getTeam() == Team.VIRUS) {
                            VirusGames.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GOLD + "You are a " + Team.PLAYER.getColor() + "Virus" + ChatColor.GOLD + ".");
                            pd.getPlayer().teleport(virus);
                            pd.getPlayer().setMaxHealth(120.0);
                            pd.getPlayer().setHealth(120.0);
                        } else if (pd.getTeam() == Team.PLAYER) {
                            VirusGames.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GOLD + "You are a " + Team.VIRUS.getColor() + "Player" + ChatColor.GOLD + ".");
                            pd.getPlayer().teleport(player);
                        } else {
                            VirusGames.getMessageManager().message(pd.getPlayer(), PrefixType.BAD, "You have not picked a class.");
                            removePlayer(pd.getPlayer());
                            return;
                        }
                        pd.getPlayer().setLevel(0);
                        pd.getPlayer().playSound(pd.getPlayer().getLocation(), Sound.LEVEL_UP, 3, 1);
                        pd.getPlayer().setScoreboard(board.getScoreboard());
                        VirusGames.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, "§3Fight against the " + pd.getTeam().getOtherTeam().getName() + " team!");
                    }
                    scoreboardId = board.startCountdown();
                    runCountdown(180);
                    updateStatusAndSign(ArenaState.STARTED);
                    Bukkit.getServer().getScheduler().cancelTask(taskid);
                }

                countdown--;
            }
        }, 0, 20);
    }

    public void stop(final Team winner) {
        updateStatusAndSign(ArenaState.WINNED);
        final ArrayList<PlayerData> pdata = new ArrayList<>();
        pdata.addAll(data);

        for (PlayerData pd : pdata) {
            VirusGames.getMessageManager().message(pd.getPlayer(), PrefixType.NORMAL, ChatColor.GOLD + "The " + winner.getColor() + winner.getName() + ChatColor.GOLD + " s Team has won!");
            try {
                MySQL.addStars(pd.getPlayer(), 50);
                VirusGames.getMessageManager().message(pd.getPlayer(), PrefixType.INFO, "You got 50 stars for playing!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            removePlayer(pd.getPlayer());
        }

        data.clear();
        pdata.clear();

        Bukkit.getScheduler().cancelTask(taskid);
        Bukkit.getScheduler().cancelTask(task);
        Bukkit.getScheduler().cancelTask(scoreboardId);
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
        this.sign.setLine(0, ChatColor.BLUE + "[" + ChatColor.GREEN + "VG" + ChatColor.BLUE + "] - " + this.id);
        this.sign.setLine(1, state.getText());
        this.sign.setLine(2, ChatColor.RED + "" + data.size() + "/" + numPlayers);
        this.sign.setLine(3, ChatColor.BLUE + name);
        this.sign.update(true);
    }

    public void setWinner(Team team) {
        for (PlayerData pd : data) {
            if (pd.getTeam() != team) continue;
            try {
                MySQL.addStars(pd.getPlayer(), 50);
                VirusGames.getMessageManager().message(pd.getPlayer(), PrefixType.INFO, "You got 50 stars for winning the game!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        stop(team);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void runCountdown(int seconds) {
        task = Bukkit.getScheduler().scheduleSyncDelayedTask(VirusGames.getInstance(), new Runnable() {
            @Override
            public void run() {
                setWinner(Team.PLAYER);
            }
        }, seconds * 20);
    }

}

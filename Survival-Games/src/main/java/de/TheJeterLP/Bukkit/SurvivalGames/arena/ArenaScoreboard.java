package de.TheJeterLP.Bukkit.SurvivalGames.arena;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author TheJeterLP
 */
public class ArenaScoreboard {

    private final Game game;
    private final HashMap<Player, Integer> kills = new HashMap<>();

    public ArenaScoreboard(Game game) {
        this.game = game;
    }

    public Scoreboard getScoreboard() {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective main = board.registerNewObjective("time", "remain");
        main.setDisplaySlot(DisplaySlot.SIDEBAR);
        main.setDisplayName("§5§lKills");
        if (!kills.isEmpty()) {
            for (Player op : kills.keySet()) {
                Score s = main.getScore(op.getDisplayName());
                s.setScore(kills.get(op));
            }
            return board;
        } else {
            for (Player p : game.getAllPlayers()) {
                kills.put(p, 0);
            }
            return getScoreboard();
        }
    }

    public void setSB(Player p) {
        p.setScoreboard(getScoreboard());
    }

    public void handleKill(Player killer) {
        if (!kills.containsKey(killer)) {
            kills.put(killer, 1);
        } else {
            int now = kills.get(killer);
            now++;
            kills.remove(killer);
            kills.put(killer, now);
        }

        for (Player p : game.getAllPlayers()) {
            setSB(p);
        }
    }
}

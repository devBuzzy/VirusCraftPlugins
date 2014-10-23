package de.TheJeterLP.Bukkit.VirusGames.Arena;

import de.TheJeterLP.Bukkit.VirusGames.Bukkit.VirusGames;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author TheJeterLP
 */
public class ArenaScoreboard {

    private final Scoreboard board;
    private int remain = 180, id;
    private final Objective main;

    public ArenaScoreboard() {
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        main = board.registerNewObjective("time", "remain");
        main.setDisplaySlot(DisplaySlot.SIDEBAR);
        main.setDisplayName("§5§lVirusGames");
        Score score2 = main.getScore("§6Remaining:");
        score2.setScore(remain);
    }

    public Scoreboard getScoreboard() {
        return board;
    }

    public int startCountdown() {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(VirusGames.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (remain == 0) {
                    Bukkit.getScheduler().cancelTask(id);
                }
                Score score2 = main.getScore("§6Remaining:");
                score2.setScore(remain);
                remain--;
            }
        }, 0, 20);
        return id;
    }
}

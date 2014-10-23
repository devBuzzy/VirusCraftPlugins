package de.TheJeterLP.Bukkit.StarShop;

import de.TheJeterLP.Bukkit.StarShop.Database.MySQL;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author TheJeterLP
 */
public class ScoreboardFactory {

    private static Scoreboard getScoreboard(Player player) throws SQLException {
        if (MySQL.getStars(player) == -1) MySQL.addPlayer(player);
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective main = board.registerNewObjective("main", "main");
        main.setDisplaySlot(DisplaySlot.SIDEBAR);
        main.setDisplayName("§5✦ VcNetwork ✦");

        Score s1 = main.getScore("§a");
        s1.setScore(8);

        Score s2 = main.getScore("§b");
        s2.setScore(7);

        Score s3 = main.getScore("§6★ Stars ★");
        s3.setScore(6);

        String stars = "" + MySQL.getStars(player);
        if (stars.length() > 16) stars = stars.substring(0, 16);

        Score s4 = main.getScore(stars);
        s4.setScore(5);

        Score s5 = main.getScore("§c");
        s5.setScore(4);

        Score s6 = main.getScore("§a● Players ●");
        s6.setScore(3);

        Score s7 = main.getScore(Bukkit.getOnlinePlayers().length + "");
        s7.setScore(2);

        Score s8 = main.getScore("§d");
        s8.setScore(1);

        Score s9 = main.getScore("§b✹ Have fun ✹");
        s9.setScore(0);
        return board;
    }

    public static boolean hasBoard(Player p) {
        if (p.getScoreboard() == null || p.getScoreboard().getObjective("main") == null) return false;
        Objective main = p.getScoreboard().getObjective("main");
        return main.getDisplayName().equalsIgnoreCase("§5✦ VcNetwork ✦");

    }

    public static void updateBoard(final Player p) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Starshop.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    p.setScoreboard(getScoreboard(p));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}

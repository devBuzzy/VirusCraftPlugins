package de.TheJeterLP.Bukkit.SurvivalGames.arena;

import de.TheJeterLP.Bukkit.SurvivalGames.util.SettingsManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageUtil;
import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class LobbyWall {

    private final ArrayList<Sign> signs = new ArrayList<>();
    private final ArrayList<String> msgqueue = new ArrayList<>();
    private final int gameid;

    public LobbyWall(int gid) {
        gameid = gid;
    }

    public boolean loadSign(World w, int x1, int x2, int z1, int z2, int y1) {
        boolean usingx = (x1 != x2);
        int dir = new Location(w, x1, y1, z1).getBlock().getData();
        if (usingx) {
            for (int a = Math.max(x1, x2); a >= Math.min(x1, x2); a--) {
                Location l = new Location(w, a, y1, z1);
                BlockState b = l.getBlock().getState();
                if (b instanceof Sign) {
                    signs.add((Sign) b);
                } else {
                    return false;
                }
            }
        } else {
            for (int a = Math.min(z1, z2); a <= Math.max(z1, z2); a++) {
                Location l = new Location(w, x1, y1, a);
                BlockState b = l.getBlock().getState();
                if (b instanceof Sign) {
                    signs.add((Sign) b);
                } else {
                    return false;
                }
            }
        }
        if (dir == 3 || dir == 5) {
            Collections.reverse(signs);
        }
        update();
        return true;
    }

    public void update() {      
        clear();
        Game game = GameManager.getInstance().getGame(gameid);
        Sign s0 = signs.get(0);
        Sign s1 = signs.get(1);

        //sign 0
        s0.setLine(0, "§9[SG]");
        s0.setLine(1, "§aClick to join");
        s0.setLine(2, "§aArena §7" + gameid);

        //sign 1
        s1.setLine(0, "§5Arena §7" + gameid);
        s1.setLine(1, "§7" + game.getMode() + "");
        String maxPlayers = "§r/" + SettingsManager.getInstance().getSpawnCount(gameid);
        if (game.getActivePlayers() == 0)
            s1.setLine(2, "§7" + game.getActivePlayers() + "§r/§7" + game.getInactivePlayers() + maxPlayers);
        else
            s1.setLine(2, "§a" + game.getActivePlayers() + "§r/§4" + game.getInactivePlayers() + maxPlayers);

        if (game.getMode() == Game.GameMode.STARTING) {
            s1.setLine(3, game.getCountdownTime() + "");
        } else if (game.getMode() == Game.GameMode.FINISHING) {
            s1.setLine(3, game.getRBStatus());
            if (game.getRBPercent() > 100) {
                s1.setLine(1, "Saving Queue");
                s1.setLine(3, (int) game.getRBPercent() + " left");
            } else s1.setLine(3, (int) game.getRBPercent() + "%");
        } else {
            s1.setLine(3, "");
        }

        //live player data
        ArrayList<String> display = new ArrayList<>();
        for (Player p : game.getAllPlayers()) {
            display.add((game.isPlayerActive(p) ? ChatColor.BLACK : ChatColor.GRAY) + MessageUtil.stylize(p, true, !game.isPlayerActive(p)));
        }

        try {
            int no = 2;
            int line = 0;
            for (String s : display) {
                signs.get(no).setLine(line, s);
                line++;
                if (line >= 4) {
                    line = 0;
                    no++;
                }
            }
        } catch (Exception e) {
        }
        for (Sign s : signs) {
            s.update();
        }
    }

    public void clear() {
        for (Sign s : signs) {
            for (int a = 0; a < 4; a++) {
                s.setLine(a, "");
            }
            s.update();
        }
    }
   
    int displaytid = 0;

    public void display() {
        int a = 0;
        while (msgqueue.size() > 0 && a < 4) {
            String s = msgqueue.get(0);
            for (int b = 0; b < s.length() / 16; b++) {
                try {
                    signs.get(b).setLine(a, s.substring(b * 16, (b + 1) * 16));

                    signs.get(b).update();
                } catch (Exception e) {
                }
            }
            a++;
            msgqueue.remove(0);
        }

    }

    int aniline = 0;
}

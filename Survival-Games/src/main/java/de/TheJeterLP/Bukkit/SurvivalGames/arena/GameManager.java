package de.TheJeterLP.Bukkit.SurvivalGames.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.Game.GameMode;
import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageManager.PrefixType;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.SettingsManager;
import de.TheJeterLP.Bukkit.SurvivalGames.SurvivalGames;
import de.TheJeterLP.Bukkit.VirusCraftTools.Party.Party;
import de.TheJeterLP.Bukkit.VirusCraftTools.Party.PartyManager;
import org.bukkit.Bukkit;

public class GameManager {

    private static final GameManager instance = new GameManager();
    private final ArrayList<Game> games = new ArrayList<>();
    private final SurvivalGames p = SurvivalGames.getInstance();
    private static final HashMap<Integer, HashSet<Block>> openedChest = new HashMap<>();

    public static HashMap<Integer, HashSet<Block>> getOpenedChest() {
        return openedChest;
    }
    private final MessageManager msgmgr = MessageManager.getInstance();

    public static GameManager getInstance() {
        return instance;
    }

    public void setup() {
        LoadGames();
        for (Game g : getGames()) {
            openedChest.put(g.getID(), new HashSet< Block>());
        }
    }

    public Plugin getPlugin() {
        return p;
    }

    public void reloadGames() {
        LoadGames();
    }

    public void LoadGames() {
        FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
        games.clear();
        int no = c.getInt("sg-system.arenano", 0);
        int loaded = 0;
        int a = 1;
        while (loaded < no) {
            if (c.isSet("sg-system.arenas." + a + ".x1")) {
                //c.set("sg-system.arenas."+a+".enabled",c.getBoolean("sg-system.arena."+a+".enabled", true));
                if (c.getBoolean("sg-system.arenas." + a + ".enabled")) {
                    //SurvivalGames.$(c.getString("sg-system.arenas."+a+".enabled"));
                    //c.set("sg-system.arenas."+a+".vip",c.getBoolean("sg-system.arenas."+a+".vip", false));
                    SurvivalGames.$("Loading Arena: " + a);
                    loaded++;
                    games.add(new Game(a));
                }
            }
            a++;

        }
        LobbyManager.getInstance().clearAllSigns();

    }

    public int getBlockGameId(Location v) {
        for (Game g : games) {
            if (g.isBlockInArena(v)) {
                return g.getID();
            }
        }
        return -1;
    }

    public int getPlayerGameId(Player p) {
        for (Game g : games) {
            if (g.isPlayerActive(p)) {
                return g.getID();
            }
        }
        return -1;
    }

    public int getPlayerSpectateId(Player p) {
        for (Game g : games) {
            if (g.isSpectator(p)) {
                return g.getID();
            }
        }
        return -1;
    }

    public boolean isPlayerActive(Player player) {
        for (Game g : games) {
            if (g.isPlayerActive(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerInactive(Player player) {
        for (Game g : games) {
            if (g.isPlayerActive(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSpectator(Player player) {
        for (Game g : games) {
            if (g.isSpectator(player)) {
                return true;
            }
        }
        return false;
    }

    public void removeFromOtherQueues(Player p, int id) {
        for (Game g : getGames()) {
            if (g.isInQueue(p) && g.getID() != id) {
                g.removeFromQueue(p);
                msgmgr.sendMessage(PrefixType.INFO, "Removed from the queue in arena " + g.getID(), p);
            }
        }
    }

    public int getGameCount() {
        return games.size();
    }

    public Game getGame(int a) {
        //int t = gamemap.get(a);
        for (Game g : games) {
            if (g.getID() == a) {
                return g;
            }
        }
        return null;
    }

    public void removePlayer(Player p, boolean b) {
        getGame(getPlayerGameId(p)).removePlayer(p, b);
    }

    public void removeSpectator(Player p) {
        getGame(getPlayerSpectateId(p)).removeSpectator(p);
    }

    public void disableGame(int id) {
        getGame(id).disable();
    }

    public void enableGame(int id) {
        getGame(id).enable();
    }

    public ArrayList< Game> getGames() {
        return games;
    }

    public GameMode getGameMode(int a) {
        for (Game g : games) {
            if (g.getID() == a) {
                return g.getMode();
            }
        }
        return null;
    }

    public void startGame(int a) {
        getGame(a).countdown(10);
    }

    public void addPlayer(Player p, int g) {
        Game game = getGame(g);
        if (game == null) {
            MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.input", p, "message-No game by this ID exist!");
            return;
        }

        Party pa = PartyManager.getInstance().getParty(p);

        if (pa == null) {
            game.addPlayer(p);
            return;
        }

        if (!pa.isCreator(p)) {
            MessageManager.getInstance().sendMessage(PrefixType.WARNING, "Only the creator of your party can choose a game.", p);
            return;
        }

        if ((SettingsManager.getInstance().getSpawnCount(game.getID()) - game.getAllPlayers().size()) < pa.size()) {
            MessageManager.getInstance().sendMessage(PrefixType.WARNING, "The arena does not has enough free slots for your whole party.", p);
        } else {
            for (String party : pa.getPlayers()) {
                if (Bukkit.getPlayerExact(party) == null) continue;
                game.addPlayer(Bukkit.getPlayerExact(party));
            }
        }
    }

    public void autoAddPlayer(Player pl) {
        if (PartyManager.getInstance().getParty(pl) != null) {
            MessageManager.getInstance().sendMessage(MessageManager.PrefixType.WARNING, "You cannot use this feature while being in a party.", pl);
            return;
        }

        ArrayList<Game> qg = new ArrayList<Game>(5);
        for (Game g : games) {
            if (g.getMode() == Game.GameMode.WAITING) qg.add(g);
        }
        //TODO: fancy auto balance algorithm
        if (qg.isEmpty()) {
            pl.sendMessage(ChatColor.RED + "No games to join");
            msgmgr.sendMessage(PrefixType.WARNING, "No games to join!", pl);
            return;
        }
        qg.get(0).addPlayer(pl);
    }

    public WorldEditPlugin getWorldEdit() {
        return p.getWorldEdit();
    }

    public void createArenaFromSelection(Player pl) {
        FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
        //SettingsManager s = SettingsManager.getInstance();

        WorldEditPlugin we = p.getWorldEdit();
        Selection sel = we.getSelection(pl);
        if (sel == null) {
            msgmgr.sendMessage(PrefixType.WARNING, "You must make a WorldEdit Selection first!", pl);
            return;
        }
        Location max = sel.getMaximumPoint();
        Location min = sel.getMinimumPoint();

        /* if(max.getWorld()!=SettingsManager.getGameWorld() || min.getWorld()!=SettingsManager.getGameWorld()){
         * pl.sendMessage(ChatColor.RED+"Wrong World!");
         * return;
         * } */
        int no = c.getInt("sg-system.arenano") + 1;
        c.set("sg-system.arenano", no);
        if (games.isEmpty()) no = 1;
        else no = games.get(games.size() - 1).getID() + 1;
        SettingsManager.getInstance().getSpawns().set(("spawns." + no), null);
        c.set("sg-system.arenas." + no + ".world", max.getWorld().getName());
        c.set("sg-system.arenas." + no + ".x1", max.getBlockX());
        c.set("sg-system.arenas." + no + ".y1", max.getBlockY());
        c.set("sg-system.arenas." + no + ".z1", max.getBlockZ());
        c.set("sg-system.arenas." + no + ".x2", min.getBlockX());
        c.set("sg-system.arenas." + no + ".y2", min.getBlockY());
        c.set("sg-system.arenas." + no + ".z2", min.getBlockZ());
        c.set("sg-system.arenas." + no + ".enabled", true);

        SettingsManager.getInstance().saveSystemConfig();
        hotAddArena(no);
        pl.sendMessage(ChatColor.GREEN + "Arena ID " + no + " Succesfully added");

    }

    private void hotAddArena(int no) {
        Game game = new Game(no);
        games.add(game);
    }

    public void hotRemoveArena(int no) {
        for (Game g : games.toArray(new Game[0])) {
            if (g.getID() == no) {
                games.remove(getGame(no));
            }
        }
    }

    public void gameEndCallBack(int id) {
        getGame(id).setRBStatus("clearing chest");
        openedChest.put(id, new HashSet< Block>());
    }

    public String getStringList(int gid) {
        Game g = getGame(gid);
        StringBuilder sb = new StringBuilder();
        Player[][] players = g.getPlayers();

        sb.append(ChatColor.GREEN).append("<---------------------[ Alive: ").append(players[0].length).append(" ]--------------------->\n").append(ChatColor.GREEN).append(" ");
        for (Player p : players[0]) {
            sb.append(p.getName()).append(",");
        }
        sb.append("\n\n");
        sb.append(ChatColor.RED).append("<---------------------[ Dead: ").append(players[1].length).append(" ]---------------------->\n").append(ChatColor.GREEN).append(" ");
        for (Player p : players[1]) {
            sb.append(p.getName()).append(",");
        }
        sb.append("\n\n");
        return sb.toString();
    }

}

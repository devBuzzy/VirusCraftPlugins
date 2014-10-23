package de.TheJeterLP.Bukkit.SurvivalGames.commands;

import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageManager;
import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageManager.PrefixType;
import java.util.Vector;
import net.minecraft.util.com.google.common.base.Joiner;

public class CommandHandler implements CommandExecutor {

    private final Plugin plugin;
    private final HashMap<String, SubCommand> commands;
    private final HashMap<String, Integer> helpinfo;
    private final MessageManager msgmgr = MessageManager.getInstance();

    public CommandHandler(Plugin plugin) {
        this.plugin = plugin;
        commands = new HashMap<>();
        helpinfo = new HashMap<>();
        loadCommands();
        loadHelpInfo();
    }

    private void loadCommands() {
        commands.put("createarena", new CreateArena());
        commands.put("join", new Join());
        commands.put("addwall", new AddWall());
        commands.put("setspawn", new SetSpawn());
        commands.put("getcount", new ListArenas());
        commands.put("disable", new Disable());
        commands.put("start", new ForceStart());
        commands.put("enable", new Enable());
        commands.put("vote", new Vote());
        commands.put("leave", new Leave());
        commands.put("setlobbyspawn", new SetLobbySpawn());
        commands.put("setlobbywall", new SetLobbyWall());
        commands.put("resetspawns", new ResetSpawns());
        commands.put("delarena", new DelArena());
        commands.put("spectate", new Spectate());
        commands.put("lq", new LeaveQueue());
        commands.put("leavequeue", new LeaveQueue());
        commands.put("list", new ListPlayers());
    }

    private void loadHelpInfo() {
        helpinfo.put("createarena", 3);
        helpinfo.put("join", 1);
        helpinfo.put("addwall", 3);
        helpinfo.put("setspawn", 3);
        helpinfo.put("getcount", 3);
        helpinfo.put("disable", 2);
        helpinfo.put("start", 2);
        helpinfo.put("enable", 2);
        helpinfo.put("vote", 1);
        helpinfo.put("leave", 1);
        helpinfo.put("setlobbyspawn", 3);
        helpinfo.put("resetspawns", 3);
        helpinfo.put("delarena", 3);
        helpinfo.put("spectate", 1);
        helpinfo.put("lq", 1);
        helpinfo.put("leavequeue", 1);
        helpinfo.put("list", 1);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd1, String commandLabel, String[] args) {
        PluginDescriptionFile pdfFile = plugin.getDescription();
        if (!(sender instanceof Player)) {
            msgmgr.logMessage(PrefixType.WARNING, "Only in-game players can use SurvivalGames commands! ");
            return true;
        }

        Player player = (Player) sender;

        if (cmd1.getName().equalsIgnoreCase("survivalgames")) {
            if (args == null || args.length < 1) {
                msgmgr.sendMessage(PrefixType.INFO, "Version " + pdfFile.getVersion() + " by " + Joiner.on(" ").join(pdfFile.getAuthors()), player);
                msgmgr.sendMessage(PrefixType.INFO, "Type /sg help <player | staff | admin> for command information", player);
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 1) {
                    help(player, 1);
                } else {
                    if (args[1].toLowerCase().startsWith("player")) {
                        help(player, 1);
                        return true;
                    }
                    if (args[1].toLowerCase().startsWith("staff")) {
                        help(player, 2);
                        return true;
                    }
                    if (args[1].toLowerCase().startsWith("admin")) {
                        help(player, 3);
                        return true;
                    } else {
                        msgmgr.sendMessage(PrefixType.WARNING, args[1] + " is not a valid page! Valid pages are Player, Staff, and Admin.", player);
                    }
                }
                return true;
            }
            String sub = args[0];
            Vector<String> l = new Vector<>();
            l.addAll(Arrays.asList(args));
            l.remove(0);
            args = l.toArray(new String[0]);
            if (!commands.containsKey(sub)) {
                msgmgr.sendMessage(PrefixType.WARNING, "Command doesn't exist.", player);
                msgmgr.sendMessage(PrefixType.INFO, "Type /sg help for command information", player);
                return true;
            }
            try {
                commands.get(sub).onCommand(player, args);
            } catch (Exception e) {
                e.printStackTrace();
                msgmgr.sendFMessage(PrefixType.ERROR, "error.command", player, "command-[" + sub + "] " + Arrays.toString(args));
                msgmgr.sendMessage(PrefixType.INFO, "Type /sg help for command information", player);
            }
            return true;
        }
        return false;
    }

    public void help(Player p, int page) {
        if (page == 1) {
            p.sendMessage(ChatColor.BLUE + "------------ " + msgmgr.getPrefix() + ChatColor.DARK_AQUA + " Player Commands" + ChatColor.BLUE + " ------------");
        }
        if (page == 2) {
            p.sendMessage(ChatColor.BLUE + "------------ " + msgmgr.getPrefix() + ChatColor.DARK_AQUA + " Staff Commands" + ChatColor.BLUE + " ------------");
        }
        if (page == 3) {
            p.sendMessage(ChatColor.BLUE + "------------ " + msgmgr.getPrefix() + ChatColor.DARK_AQUA + " Admin Commands" + ChatColor.BLUE + " ------------");
        }

        for (String command : commands.keySet()) {
            try {
                if (helpinfo.get(command) == page) {
                    msgmgr.sendMessage(PrefixType.INFO, commands.get(command).help(p), p);
                }
            } catch (Exception e) {
            }
        }
    }
}

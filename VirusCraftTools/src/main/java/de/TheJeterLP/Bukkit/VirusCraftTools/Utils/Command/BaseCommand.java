package de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command;

import de.TheJeterLP.Bukkit.VirusCraftTools.Addon.Addon;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public abstract class BaseCommand implements CommandExecutor {
    
    private final String permission;
    private String subPerm;
    private final String command;
    protected final List<CommandHelp> helpPages = new ArrayList<>();
    private final List<String> HELP_TEXT = new ArrayList<>();
    protected final HashMap<String, SubCommand> subcmds = new HashMap<>();
    
    protected BaseCommand(String command, String permission) {
        this.command = command;
        this.permission = permission;
    }
    
    protected BaseCommand(String command) {
        this.command = command;
        this.permission = "viruscmd." + command;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sendHelp(sender, args)) return true;
        CommandResult cr = null;
        if (!argsCheck(CommandArgs.getArgs(args, 0))) {
            cr = CommandResult.ERROR;
        } else {
            cr = execute(cmd, args, sender);
        }
        sendMessages(sender, cmd, cr);
        return true;
    }
    
    public abstract CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception;
    
    public abstract CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception;
    
    public abstract boolean argsCheck(CommandArgs args);
    
    public void prepare() {
        if (helpPages == null || helpPages.isEmpty()) return;
        HELP_TEXT.add(ChatColor.GREEN + "------------------------" + ChatColor.BLUE + "Help" + ChatColor.GREEN + "-------------------------");
        for (CommandHelp ch : helpPages) {
            HELP_TEXT.add(ch.getText());
        }
        HELP_TEXT.add(ChatColor.GREEN + "-----------------------------------------------------");
    }
    
    protected boolean hasPermission(Player player, boolean other) {
        if (subPerm == null || subPerm.isEmpty()) {
            if (permission == null || permission.isEmpty()) return true;
            String perm = (other ? permission + ".other" : "");
            return player.hasPermission(perm) || player.isOp();
        } else {
            String perm = (other ? subPerm + ".other" : "");
            return player.hasPermission(perm) || player.isOp();
        }
    }
    
    private PluginCommand getBukkitCommand() {
        if (command == null || command.isEmpty()) {
            return null;
        }
        return VCplugin.inst().getCommand(command);
    }
    
    private PluginCommand getBukkitCommand(Addon a) {
        if (command == null || command.isEmpty()) {
            return null;
        }
        return a.getCommand(command);
    }
    
    public void register(Addon pl) {
        getBukkitCommand(pl).setExecutor(this);
    }
    
    public void register() {
        getBukkitCommand().setExecutor(this);
    }
    
    protected boolean sendHelp(CommandSender s, String[] args) {
        if (args.length == 1 && (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) && !HELP_TEXT.isEmpty()) {
            for (String string : HELP_TEXT) {
                s.sendMessage(string);
            }
            return true;
        }
        return false;
    }
    
    private CommandResult execute(Command cmd, String[] args, CommandSender sender) {
        subPerm = null;
        CommandResult cr;
        try {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length >= 1) {
                    if (subcmds.containsKey(args[0].toLowerCase())) {
                        SubCommand sub = subcmds.get(args[0].toLowerCase());
                        subPerm = sub.getPermission();
                        if (!hasPermission(p, false)) {
                            cr = CommandResult.NO_PERMISSION;
                        } else {
                            cr = sub.executePlayer(p, CommandArgs.getArgs(args, 1));
                            subPerm = sub.getPermission();
                        }
                    } else {
                        if (!hasPermission(p, false)) {
                            cr = CommandResult.NO_PERMISSION;
                        } else {
                            cr = onPlayerCommand(p, cmd, CommandArgs.getArgs(args, 0));
                        }
                    }
                } else {
                    if (!hasPermission(p, false)) {
                        cr = CommandResult.NO_PERMISSION;
                    } else {
                        cr = onPlayerCommand(p, cmd, CommandArgs.getArgs(args, 0));
                    }
                }
            } else {
                if (args.length >= 1) {
                    if (subcmds.containsKey(args[0].toLowerCase())) {
                        SubCommand sub = subcmds.get(args[0].toLowerCase());
                        cr = sub.executeConsole(sender, CommandArgs.getArgs(args, 1));
                    } else {
                        cr = onServerCommand(sender, cmd, CommandArgs.getArgs(args, 0));
                    }
                } else {
                    cr = onServerCommand(sender, cmd, CommandArgs.getArgs(args, 0));
                }
            }
        } catch (Exception e) {
            Utils.sendMessage(MessageType.ERROR, sender, "The command generatoed an exception.");
            e.printStackTrace();
            cr = CommandResult.SUCCESS;
        }
        return cr;
    }
    
    private void sendMessages(CommandSender sender, Command cmd, CommandResult cr) {
        if (cr != null && cr.getMessage() != null) {
            String perm;
            if (subPerm != null) {
                perm = (cr == CommandResult.NO_PERMISSION_OTHER ? subPerm + ".other" : subPerm);
            } else if (permission != null) {
                perm = (cr == CommandResult.NO_PERMISSION_OTHER ? permission + ".other" : permission);
            } else {
                perm = "";
            }
            sender.sendMessage(cr.getMessage().replace("%cmd%", cmd.getName()).replace("%perm%", perm));
        }
    }
    
    public String getPermission() {
        return permission;
    }
}

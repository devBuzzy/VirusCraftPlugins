package de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TheJeterLP
 */
public abstract class Executor implements CommandExecutor {

    protected String permission;
    protected String command;
    protected final List<CommandHelp> helpPages = new ArrayList<>();
    private final List<String> HELP_TEXT = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1 && (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) && !HELP_TEXT.isEmpty()) {
            for (String s : HELP_TEXT) {
                sender.sendMessage(s);
            }
            return true;
        } else {
            CommandResult cr = null;
            if (Utils.isPlayer(sender)) {
                Player p = Utils.getPlayer(sender);
                if (!hasPermission(p, false)) {
                    cr = CommandResult.NO_PERMISSION;
                } else {
                    try {
                        cr = onPlayerCommand(p, cmd, new CommandArgs(args));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                try {
                    cr = onServerCommand(sender, cmd, new CommandArgs(args));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (cr != null && cr.getMessage() != null) {
                if (permission != null)
                    sender.sendMessage(cr.getMessage().replace("%cmd%", cmd.getName()).replace("%perm%", permission));
                else sender.sendMessage(cr.getMessage().replace("%cmd%", cmd.getName()).replace("%perm%", ""));
            }
            return true;
        }

    }

    public abstract CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception;

    public abstract CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception;

    public void prepare() {
        if (helpPages == null || helpPages.isEmpty()) {
            return;
        }
        HELP_TEXT.add(ChatColor.GREEN + "------------------------" + ChatColor.BLUE + "Help" + ChatColor.GREEN + "-------------------------");
        for (CommandHelp ch : helpPages) {
            HELP_TEXT.add(ch.getText());
        }
        HELP_TEXT.add(ChatColor.GREEN + "-----------------------------------------------------");
    }

    protected boolean hasPermission(Player player, boolean other) {
        if (permission == null || permission.isEmpty()) return true;
        String perm = permission;
        if (other) {
            perm = perm + ".other";
        }
        return player.hasPermission(perm) || player.hasPermission("tools.*") || player.isOp();
    }

    public PluginCommand getBukkitCommand() {
        if (command == null || command.isEmpty()) {
            return null;
        }
        return VCplugin.inst().getCommand(command);
    }

    public PluginCommand getBukkitCommand(JavaPlugin pl) {
        if (command == null || command.isEmpty()) {
            return null;
        }
        return pl.getCommand(command);
    }

}

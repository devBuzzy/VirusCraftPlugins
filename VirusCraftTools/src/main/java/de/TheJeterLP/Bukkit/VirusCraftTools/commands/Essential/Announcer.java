package de.TheJeterLP.Bukkit.VirusCraftTools.commands.Essential;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Config;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Announcer extends BaseCommand {

    public Announcer() {
        super("announcer", "tools.announcer");
        helpPages.add(new CommandHelp("/announcer list", "Lists all messages from the Announcer"));
        helpPages.add(new CommandHelp("/announcer add <message>", "Adds <message> to the Announcer"));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) {
        return onServerCommand(p, cmd, args);
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) {
        if (args.getString(0).equalsIgnoreCase("list")) {
            Utils.sendMessage(MessageType.INFO, sender, "Announcer messages:");
            for (String s : Config.ANNOUNCER_MESSAGES.getStringList()) {
                sender.sendMessage(s);
                sender.sendMessage(" ");
            }
            return CommandResult.SUCCESS;
        } else if (args.getString(0).equalsIgnoreCase("add") && args.getLength() > 1) {
            String msg = "";
            for (String s : args.getArgs()) {
                if (s.equalsIgnoreCase("add")) continue;
                if (msg.isEmpty()) {
                    msg += s;
                } else {
                    msg += " " + s;
                }
            }
            msg = Utils.replaceColors(msg);
            List<String> curr = Config.ANNOUNCER_MESSAGES.getStringList();
            curr.add(msg);
            Config.ANNOUNCER_MESSAGES.set(curr);
            Utils.sendMessage(MessageType.INFO, sender, "Added " + msg + " to the list.");
            return CommandResult.SUCCESS;
        }
        return CommandResult.ERROR;
    }
    
    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() >= 1;
    }

}

package de.TheJeterLP.Bukkit.Nametags;

import com.google.common.base.Joiner;
import de.TheJeterLP.Bukkit.Nametags.Utils.ConfigManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class CommandHandler extends BaseCommand {

    public CommandHandler() {
        super("nametags", "nametags.mod");
        helpPages.add(new CommandHelp("/nametags <user> prefix <prefix>", "Sets the prefix for <user> to <prefix>"));
        helpPages.add(new CommandHelp("/nametags <user> suffix <suffix>", "Sets the suffix for <user> to <suffix>"));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) {
        return onServerCommand(p, cmd, args);
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) {
        if (!args.isPlayer(0)) return CommandResult.NOT_ONLINE;
        Player target = args.getPlayer(0);
        if (args.getString(1).equalsIgnoreCase("prefix")) {
            String prefix = Joiner.on(" ").join(CommandArgs.getArgs(args.getArgs(), 2).getArgs());
            ConfigManager.setPrefix(target, prefix);
            Main.setNametag(target);
            Utils.sendMessage(MessageType.INFO, sender, "The prefix of " + target.getDisplayName() + " was set to " + prefix);
            return CommandResult.SUCCESS;
        } else if (args.getString(1).equalsIgnoreCase("suffix")) {
            String suffix = Joiner.on(" ").join(CommandArgs.getArgs(args.getArgs(), 2).getArgs());
            ConfigManager.setSuffix(target, suffix);
            Main.setNametag(target);
            Utils.sendMessage(MessageType.INFO, sender, "The suffix of " + target.getDisplayName() + " was set to " + suffix);
            return CommandResult.SUCCESS;
        } else {
            return CommandResult.ERROR;
        }
    }

    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() >= 3;
    }
}

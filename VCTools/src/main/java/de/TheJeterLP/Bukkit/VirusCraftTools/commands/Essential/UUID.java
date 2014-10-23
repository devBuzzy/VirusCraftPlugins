package de.TheJeterLP.Bukkit.VirusCraftTools.commands.Essential;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Executor;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class UUID extends Executor {

    public UUID() {
        command = "uuid";
        permission = "tools.uuid";
        helpPages.add(new CommandHelp("/uuid <player>", "Gets the uuid of the given Player."));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        return onServerCommand(p, cmd, args);
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception {
        if (args.getLength() != 1) return CommandResult.ERROR;

        if (!args.isPlayer(0)) return CommandResult.NOT_ONLINE;
        Player target = args.getPlayer(0);
        String id = target.getUniqueId().toString();
        Utils.sendMessage(MessageType.INFO, sender, "Bukkit UUID: \n" + id);

        return CommandResult.SUCCESS;
    }

}

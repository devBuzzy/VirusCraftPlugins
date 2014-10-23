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
public class CW extends Executor {

    public CW() {
        permission = "tools.cw";
        command = "cw";
        helpPages.add(new CommandHelp("/cw", "Enables CommandWatcher for you"));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) {
        if (!args.isEmpty()) return CommandResult.ERROR;

        if (Utils.hasCWEnabled(p)) {
            Utils.disableCW(p);
            Utils.sendMessage(MessageType.INFO, p, "You now have CommandWatcher disabled.");
        } else {
            Utils.enableCW(p);
            Utils.sendMessage(MessageType.INFO, p, "You now have CommandWatcher enabled.");
        }

        return CommandResult.SUCCESS;
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) {
        return CommandResult.ONLY_PLAYER;
    }

}

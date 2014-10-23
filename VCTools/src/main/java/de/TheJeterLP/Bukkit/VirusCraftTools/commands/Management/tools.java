package de.TheJeterLP.Bukkit.VirusCraftTools.commands.Management;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Executor;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class tools extends Executor {

    private final VCplugin main = VCplugin.inst();

    public tools() {
        permission = "tools.reload";
        command = "tools";
        helpPages.add(new CommandHelp("/tools reload", "Reloads the plugin."));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) {
        return onServerCommand(p, cmd, args);
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) {
        if (args.getLength() != 1) {
            return CommandResult.ERROR;
        }

        if (args.getString(0).equalsIgnoreCase("reload")) {
            main.getServer().getPluginManager().disablePlugin(main);
            main.getServer().getPluginManager().enablePlugin(main);
            Utils.sendMessage(MessageType.INFO, sender, "The Plugin was successfully reloaded!");
            return CommandResult.SUCCESS;
        }
        return CommandResult.ERROR;
    }
}

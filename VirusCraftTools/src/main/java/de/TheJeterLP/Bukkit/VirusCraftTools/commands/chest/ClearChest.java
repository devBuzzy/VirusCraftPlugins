package de.TheJeterLP.Bukkit.VirusCraftTools.commands.chest;

import de.TheJeterLP.Bukkit.VirusCraftTools.Chest.VirtualChestManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
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
public class ClearChest extends BaseCommand {

    private final VirtualChestManager chestManager = VCplugin.inst().getChestManager();

    public ClearChest() {
        super("clearchest", "chest.clearchest");
        helpPages.add(new CommandHelp("/clearchest [player]", "Clear yours or [player]s chest."));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) {
        if (args.isEmpty()) {
            chestManager.removeChest(p);
            Utils.sendMessage(MessageType.INFO, p, "Successfully cleared your chest.");
            return CommandResult.SUCCESS;
        } else {
            if (!hasPermission(p, true)) return CommandResult.NO_PERMISSION_OTHER;
            if (!args.isPlayer(0)) return CommandResult.NOT_ONLINE;
            Player target = args.getPlayer(0);
            chestManager.removeChest(target);
            Utils.sendMessage(MessageType.INFO, p, "Successfully cleared " + target.getName() + "'s chest.");
            return CommandResult.SUCCESS;
        }
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() <= 1;
    }
}

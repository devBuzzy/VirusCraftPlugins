package de.thejeterlp.bukkit.viruscmd.commands.player;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.thejeterlp.bukkit.viruscmd.player.PlayerManager;
import de.thejeterlp.bukkit.viruscmd.player.VCPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_UUID extends BaseCommand {

    public Command_UUID() {
        super("uuid");
        helpPages.add(new CommandHelp("/uuid <player>", "Gets the uuid of the given Player."));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        return onServerCommand(p, cmd, args);
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception {
        if (!args.isPlayer(0)) return CommandResult.NOT_ONLINE;

        Player target = args.getPlayer(0);
        VCPlayer vctarget = PlayerManager.getVCPlayer(target);

        Utils.sendMessage(MessageType.INFO, sender, "Bukkit UUID: \n" + target.getUniqueId().toString());
        return Utils.sendMessage(MessageType.INFO, sender, "Database UUID: \n" + vctarget.getUUID().toString());
    }
    
    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() == 1;
    }

}

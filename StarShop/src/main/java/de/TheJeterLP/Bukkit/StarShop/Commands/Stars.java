package de.TheJeterLP.Bukkit.StarShop.Commands;

import de.TheJeterLP.Bukkit.StarShop.Database.MySQL;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import java.sql.SQLException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Stars extends BaseCommand {

    public Stars() {
        super("stars", null);
        helpPages.add(new CommandHelp("/stars", "See your stars"));
        helpPages.add(new CommandHelp("/stars <player>", "See <player>'s stars"));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws SQLException {
        if (args.isEmpty()) {
            Utils.sendMessage(MessageType.INFO, p, "You have " + MySQL.getStars(p) + " stars");
            return CommandResult.SUCCESS;
        } else  {
            if (!args.isPlayer(0)) return CommandResult.NOT_ONLINE;
            Player target = args.getPlayer(0);
            Utils.sendMessage(MessageType.INFO, p, target.getName() + " has " + MySQL.getStars(target) + " stars");
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

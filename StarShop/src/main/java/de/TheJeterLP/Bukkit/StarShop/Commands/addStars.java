package de.TheJeterLP.Bukkit.StarShop.Commands;

import de.TheJeterLP.Bukkit.StarShop.Database.MySQL;
import de.TheJeterLP.Bukkit.StarShop.ScoreboardFactory;
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
public class addStars extends BaseCommand {

    public addStars() {
        super("addstars", "tools.addstars");
        helpPages.add(new CommandHelp("/addstars <player> <number>", "adds <number> to <player>"));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws SQLException {
        return onServerCommand(p, cmd, args);
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws SQLException {       
        if (!args.isPlayer(0)) {
            return CommandResult.NOT_ONLINE;
        }

        if (!args.isInteger(1)) {
            return CommandResult.NOT_A_NUMBER;
        }

        Player target = args.getPlayer(0);

        int stars = args.getInt(1);
        MySQL.addStars(target, stars);
        Utils.sendMessage(MessageType.INFO, sender, target.getName() + " now has " + MySQL.getStars(target) + " stars!");
        ScoreboardFactory.updateBoard(target);
        return CommandResult.SUCCESS;
    }
    
    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() == 2;
    }

}

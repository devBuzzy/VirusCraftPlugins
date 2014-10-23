package de.TheJeterLP.Bukkit.StarShop.Commands;

import de.TheJeterLP.Bukkit.StarShop.Database.MySQL;
import de.TheJeterLP.Bukkit.StarShop.ScoreboardFactory;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Config;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import java.sql.SQLException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * @author TheJeterLP
 */
public class PromoteWithStars extends BaseCommand {

    public PromoteWithStars() {
        super("buyrank", null);
        helpPages.add(new CommandHelp("/buyrank <player+|donator|elite|ultimate>", "Buys the given rank with stars if you have enough."));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws SQLException {
        String group = args.getString(0).toLowerCase();
        int prize = 0;
        String target = "";
        switch (group) {
            case "player+":
                prize = Config.GROUP_PLAYERPLUS.getInt();
                target = "player+";
                break;
            case "donator":
                prize = Config.GROUP_DONATOR.getInt();
                target = "donator";
                break;
            case "elite":
                prize = Config.GROUP_ELITE.getInt();
                target = "elite";
                break;
            case "ultimate":
                prize = Config.GROUP_ULTIMATE.getInt();
                target = "ultimate";
                break;
            default:
                Utils.sendMessage(MessageType.ERROR, p, "The group does not exist!");
                return CommandResult.SUCCESS;
        }

        int s = MySQL.getStars(p);

        if (s >= prize) {
            MySQL.removeStars(p, prize);
            PermissionsEx.getUser(p).setGroups(new String[]{target});
            Utils.sendMessage(MessageType.INFO, p, "You got promoted!");
            ScoreboardFactory.updateBoard(p);
            return CommandResult.SUCCESS;
        } else {
            int needed = prize - s;
            Utils.sendMessage(MessageType.ERROR, p, "You need " + needed + " more stars!");
            return CommandResult.SUCCESS;
        }
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) {
        return CommandResult.ONLY_PLAYER;
    }
    
    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() == 1;
    }

}

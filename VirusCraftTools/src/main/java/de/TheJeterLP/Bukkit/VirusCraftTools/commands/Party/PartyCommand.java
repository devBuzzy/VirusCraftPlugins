package de.TheJeterLP.Bukkit.VirusCraftTools.commands.Party;

import de.TheJeterLP.Bukkit.VirusCraftTools.Party.Party;
import de.TheJeterLP.Bukkit.VirusCraftTools.Party.PartyManager;
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
public class PartyCommand extends BaseCommand {

    public PartyCommand() {
        super("party", null);
        helpPages.add(new CommandHelp("/party", "Shows if you are in a party."));
        helpPages.add(new CommandHelp("/party create", "Create a new party."));
        helpPages.add(new CommandHelp("/party leave", "Leave your current party."));
        helpPages.add(new CommandHelp("/party join", "Accepts your party invition."));
        helpPages.add(new CommandHelp("/party list", "Lists all palyers in your party."));
        helpPages.add(new CommandHelp("/party invite <player>", "Invites <player> to your party."));
        helpPages.add(new CommandHelp("/party uninvite <player>", "Uninvites <player> from your party"));
        helpPages.add(new CommandHelp("/party remove <player>", "Kicks <player> out of your party."));
        subcmds.put("create", new Create());
        subcmds.put("leave", new Leave());
        subcmds.put("join", new Join());
        subcmds.put("list", new List());
        subcmds.put("invite", new Invite());
        subcmds.put("remove", new Remove());
        subcmds.put("uninvite", new Uninvite());
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) {
        if (!args.isEmpty()) return CommandResult.ERROR;
        Party party = PartyManager.getInstance().getParty(p);
        if (party == null) {
            Utils.sendMessage(MessageType.PARTY, p, "You are not in a party.");
            return CommandResult.SUCCESS;
        }

        String rang;

        if (party.isCreator(p)) {
            rang = "creator";
        } else {
            rang = "member";
        }

        Utils.sendMessage(MessageType.PARTY, p, "You are in a party. You are " + rang + " of it.");
        return CommandResult.SUCCESS;
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) {
        return CommandResult.ONLY_PLAYER;
    }
    
    @Override
    public boolean argsCheck(CommandArgs args) {
        return true;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.TheJeterLP.Bukkit.VirusCraftTools.commands.Party;

import de.TheJeterLP.Bukkit.VirusCraftTools.Party.Party;
import de.TheJeterLP.Bukkit.VirusCraftTools.Party.PartyManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.SubCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class List extends SubCommand {

    public List() {
        super("");
    }

    @Override
    public CommandResult executeConsole(CommandSender sender, CommandArgs args) {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public CommandResult executePlayer(Player p, CommandArgs args) {
        if (PartyManager.getInstance().getParty(p) == null) {
            Utils.sendMessage(MessageType.PARTY, p, "You are not in a party.");
            return CommandResult.SUCCESS;
        }

        Utils.sendMessage(MessageType.PARTY, p, "Players in your current party:");
        Party party = PartyManager.getInstance().getParty(p);
        for (String member : party.getPlayers()) {
            String rang;
            if (party.isCreator(Bukkit.getPlayerExact(member))) {
                rang = "creator";
            } else {
                rang = "member";
            }
            p.sendMessage("§e" + member + " - §5Rank: " + rang);
        }

        Utils.sendMessage(MessageType.PARTY, p, "\nInvited Players: ");
        for (String invited : party.getInvitions()) {
            p.sendMessage("§e- " + invited);
        }
        return CommandResult.SUCCESS;
    }

}

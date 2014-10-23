/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.TheJeterLP.Bukkit.VirusCraftTools.commands.Party;

import de.TheJeterLP.Bukkit.VirusCraftTools.Party.PartyManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.SubCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Join extends SubCommand {

    public Join() {
        super("");
    }

    @Override
    public CommandResult executeConsole(CommandSender sender, CommandArgs args) {
        return CommandResult.ERROR;
    }

    @Override
    public CommandResult executePlayer(Player p, CommandArgs args) {
        if (PartyManager.getInstance().getParty(p) != null) {
            Utils.sendMessage(MessageType.PARTY, p, "You are already in a party. You have to leave it first.");
            return CommandResult.SUCCESS;
        }

        if (!PartyManager.getInstance().isInvited(p)) {
            Utils.sendMessage(MessageType.PARTY, p, "You don't have an invition.");
            return CommandResult.SUCCESS;
        }

        PartyManager.getInstance().getPartyForInvition(p).add(p);
        PartyManager.getInstance().getParty(p).uninvite(p);
        Utils.sendMessage(MessageType.PARTY, p, "You joined the party.");
        return CommandResult.SUCCESS;
    }

}

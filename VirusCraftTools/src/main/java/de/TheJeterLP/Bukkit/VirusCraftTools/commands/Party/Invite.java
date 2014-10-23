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
public class Invite extends SubCommand {

    public Invite() {
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

        if (!args.isPlayer(1)) return CommandResult.NOT_ONLINE;
        Player target = args.getPlayer(0);
        if (target.getName().equals(p.getName())) {
            Utils.sendMessage(MessageType.PARTY, p, "You cannot invite yourself.");
            return CommandResult.SUCCESS;
        }
        PartyManager.getInstance().getParty(p).invite(target, p);
        return CommandResult.SUCCESS;
    }

}

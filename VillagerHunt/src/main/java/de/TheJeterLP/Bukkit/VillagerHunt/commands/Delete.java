/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.TheJeterLP.Bukkit.VillagerHunt.commands;

import de.TheJeterLP.Bukkit.VillagerHunt.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VillagerHunt.Bukkit.VillagerHunt;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.SubCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Delete extends SubCommand {

    public Delete() {
        super("villagerhunt.delete");
    }

    @Override
    public CommandResult executeConsole(CommandSender sender, CommandArgs args) {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public CommandResult executePlayer(Player p, CommandArgs args) {
        if (args.getLength() != 1) return CommandResult.ERROR;
        if (!args.isInteger(0)) return CommandResult.NOT_A_NUMBER;

        int id = args.getInt(0);
        if (!ArenaManager.exsist(id)) {
            VillagerHunt.getMessageManager().message(p, MessageManager.PrefixType.BAD, id + "does not exist.");
            return CommandResult.SUCCESS;
        }

        if (ArenaManager.delete(id))
            VillagerHunt.getMessageManager().message(p, MessageManager.PrefixType.GOOD, "The arena " + id + " was deleted.");
        else
            VillagerHunt.getMessageManager().message(p, MessageManager.PrefixType.BAD, "The arena " + id + " was NOT deleted.");
        return CommandResult.SUCCESS;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.TheJeterLP.Bukkit.VirusSpleef.commands;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.SubCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
import de.TheJeterLP.Bukkit.VirusSpleef.Arena.Arena;
import de.TheJeterLP.Bukkit.VirusSpleef.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VirusSpleef.Bukkit.VirusSpleef;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Restart extends SubCommand {

    public Restart() {
        super("virusspleef.restart");
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
        Arena a = ArenaManager.getArena(id);
        if (a == null) {
            VirusSpleef.getMessageManager().message(p, MessageManager.PrefixType.BAD, id + " is not existing.");
            return CommandResult.SUCCESS;
        }
        a.stop(null);
        VirusSpleef.getMessageManager().message(p, MessageManager.PrefixType.INFO, "Arena " + id + " is now restarted.");
        return CommandResult.SUCCESS;
    }

}

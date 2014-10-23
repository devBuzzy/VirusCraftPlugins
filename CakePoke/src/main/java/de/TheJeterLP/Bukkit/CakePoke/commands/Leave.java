/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.TheJeterLP.Bukkit.CakePoke.commands;

import de.TheJeterLP.Bukkit.CakePoke.Arena.Arena;
import de.TheJeterLP.Bukkit.CakePoke.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.CakePoke.Bukkit.CakePoke;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.SubCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Leave extends SubCommand {

    public Leave() {
        super(null);
    }

    @Override
    public CommandResult executeConsole(CommandSender sender, CommandArgs args) throws Exception {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public CommandResult executePlayer(Player p, CommandArgs args) throws Exception {
        Arena a = ArenaManager.getArena(p);
        if (a == null) {
            CakePoke.getMessageManager().message(p, MessageManager.PrefixType.BAD, "You are not in an arena.");
            return CommandResult.SUCCESS;
        }
        a.removePlayer(p);
        return CommandResult.SUCCESS;
    }

}

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
public class Join extends SubCommand {

    public Join() {
        super(null);
    }

    @Override
    public CommandResult executeConsole(CommandSender cs, CommandArgs ca) throws Exception {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public CommandResult executePlayer(Player player, CommandArgs ca) throws Exception {
        if(ArenaManager.getArenas().isEmpty()) {
            CakePoke.getMessageManager().message(player, MessageManager.PrefixType.INFO, "There are no arenas to join.");
            return CommandResult.SUCCESS;
        }
        
        Arena a = ArenaManager.getArenas().get(0);
       
        player.teleport(a.getSpawn());
        return CommandResult.SUCCESS;
    }

}

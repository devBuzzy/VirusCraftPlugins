/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.TheJeterLP.Bukkit.VirusGames.commands;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.SubCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
import de.TheJeterLP.Bukkit.VirusGames.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VirusGames.Bukkit.VirusGames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Create extends SubCommand {

    public Create() {
        super("vg.create");
    }

    @Override
    public CommandResult executeConsole(CommandSender sender, CommandArgs args) {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public CommandResult executePlayer(Player p, CommandArgs args) {
        if (args.getLength() != 3) return CommandResult.ERROR;
        if (!args.isInteger(0) || !args.isInteger(1)) return CommandResult.NOT_A_NUMBER;

        int id = args.getInt(0);
        int maxplayers = args.getInt(1);
        String map = args.getString(2);

        if (ArenaManager.exsist(id)) {
            VirusGames.getMessageManager().message(p, MessageManager.PrefixType.BAD, id + " is already existing.");
            return CommandResult.SUCCESS;
        }

        ArenaManager.create(id, maxplayers, map);
        VirusGames.getMessageManager().message(p, MessageManager.PrefixType.INFO, "Successfully created. Please set the spawns and the sign now. After that you can do /arena finish");
        return CommandResult.SUCCESS;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.TheJeterLP.Bukkit.CakePoke.commands;

import de.TheJeterLP.Bukkit.CakePoke.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.CakePoke.Bukkit.CakePoke;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.SubCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Setspawn extends SubCommand {

    public Setspawn() {
        super("CakePoke.setspawn");
    }

    @Override
    public CommandResult executeConsole(CommandSender sender, CommandArgs args) {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public CommandResult executePlayer(Player p, CommandArgs args) throws SQLException {
        if (args.getLength() != 2) return CommandResult.ERROR;
        if (!args.isInteger(0)) return CommandResult.NOT_A_NUMBER;

        int id = args.getInt(0);
        
        if (!ArenaManager.exsist(id)) {
            CakePoke.getMessageManager().message(p, MessageManager.PrefixType.BAD, id + " does not exist.");
            return CommandResult.SUCCESS;
        }

        String spawn;
        if (args.getString(1).equalsIgnoreCase("spawn")) {
            spawn = "spawn";
        } else if (args.getString(1).equalsIgnoreCase("lobby")) {
            spawn = "lobby";
        } else {
            return CommandResult.ERROR;
        }

        PreparedStatement st = CakePoke.getDB().getPreparedStatement("UPDATE `maps` SET `" + spawn + "` = ? WHERE `id` = ?;");
        st.setString(1, Utils.serialLocation(p.getLocation()));
        st.setInt(2, id);
        st.executeUpdate();
        st.close();
        CakePoke.getMessageManager().message(p, MessageManager.PrefixType.GOOD, "The " + spawn + " spawn was set to your position.");
        return CommandResult.SUCCESS;

    }

}

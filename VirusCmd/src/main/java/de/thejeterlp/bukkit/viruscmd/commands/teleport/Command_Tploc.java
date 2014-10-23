/*
 * Copyright 2014 TheJeterLP.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.thejeterlp.bukkit.viruscmd.commands.teleport;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.thejeterlp.bukkit.viruscmd.player.PlayerManager;
import de.thejeterlp.bukkit.viruscmd.player.VCPlayer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_Tploc extends BaseCommand {

    public Command_Tploc() {
        super("tploc");
        helpPages.add(new CommandHelp("/tploc x y z", "Teleports yourself to the coordinates."));
        helpPages.add(new CommandHelp("/tploc <player> x y z", "Teleports <player> to the coordinates."));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        if (args.getLength() == 3) {
            if (!args.isInteger(0) || !args.isInteger(1) || !args.isInteger(2)) return CommandResult.NOT_A_NUMBER;
            int x = args.getInt(0);
            int y = args.getInt(1);
            int z = args.getInt(2);
            Location loc = new Location(p.getWorld(), x, y, z);
            p.teleport(loc);
            return Utils.sendMessage(MessageType.INFO, p, "You have been teleported.");
        } else {
            if (!hasPermission(p, true)) return CommandResult.NO_PERMISSION_OTHER;
            if (!args.isPlayer(0)) return CommandResult.NOT_ONLINE;
            Player target = args.getPlayer(0);
            VCPlayer vctarget = PlayerManager.getVCPlayer(target);
            if (!args.isInteger(1) || !args.isInteger(2) || !args.isInteger(3)) return CommandResult.NOT_A_NUMBER;
            int x = args.getInt(1);
            int y = args.getInt(2);
            int z = args.getInt(3);
            Location loc = new Location(target.getWorld(), x, y, z);
            target.teleport(loc);
            Utils.sendMessage(MessageType.INFO, target, "You have been teleported.");
            return Utils.sendMessage(MessageType.INFO, p, vctarget.getDisplayName() + " has been teleported.");
        }
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() == 3 || args.getLength() == 4;
    }

}

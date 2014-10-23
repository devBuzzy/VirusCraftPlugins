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
package de.thejeterlp.bukkit.viruscmd.commands.world;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.thejeterlp.bukkit.viruscmd.world.VCWorld;
import de.thejeterlp.bukkit.viruscmd.world.WorldManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_Settime extends BaseCommand {

    public Command_Settime() {
        super("time");
        helpPages.add(new CommandHelp("/time day|night", "Changes the time of the world you are in."));
        helpPages.add(new CommandHelp("/time pause|unpause", "Pauses or unpauses the time of the world you are in."));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        if (args.getString(0).equalsIgnoreCase("day")) {
            long time = 0;
            p.getLocation().getWorld().setTime(time);
            return Utils.sendMessage(MessageType.INFO, p, "Time has been set to " + args.getString(0).toLowerCase());
        } else if (args.getString(0).equalsIgnoreCase("night")) {
            long time = 13100;
            p.getLocation().getWorld().setTime(time);
            return Utils.sendMessage(MessageType.INFO, p, "Time has been set to " + args.getString(0).toLowerCase());
        } else if (args.getString(0).equalsIgnoreCase("pause")) {
            VCWorld w = WorldManager.getWorld(p.getWorld());

            if (w.isTimePaused()) {
                return Utils.sendMessage(MessageType.ERROR, p, "Time is already paused.");
            }

            w.pauseTime();
            return Utils.sendMessage(MessageType.INFO, p, "Time has been paused.");
        } else if (args.getString(0).equalsIgnoreCase("unpause")) {
            VCWorld w = WorldManager.getWorld(p.getWorld());

            if (!w.isTimePaused()) {
                return Utils.sendMessage(MessageType.ERROR, p, "Time is not paused.");
            }

            w.unPauseTime();
            return Utils.sendMessage(MessageType.INFO, p, "Time has been unpaused.");
        } else {
            return CommandResult.ERROR;
        }

    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() == 1;
    }

}

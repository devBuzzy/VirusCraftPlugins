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
package de.thejeterlp.bukkit.viruscmd.commands.player;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_Lightning extends BaseCommand {

    public Command_Lightning() {
        super("lightning");
        helpPages.add(new CommandHelp("/lightning", "Strike a lightning!"));
        helpPages.add(new CommandHelp("/lightning <player>", "Strike a lightning at <player>'s location!"));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        if (args.isEmpty()) {
            p.getWorld().strikeLightning(Utils.getLocationLooking(p, 50));
            return Utils.sendMessage(MessageType.INFO, p, "Boom.");
        } else {
            if (!args.isPlayer(0)) return CommandResult.NOT_ONLINE;
            Player target = args.getPlayer(0);
            target.getWorld().strikeLightning(target.getLocation());
            return Utils.sendMessage(MessageType.INFO, p, "Boom.");
        }
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() <= 1;
    }

}

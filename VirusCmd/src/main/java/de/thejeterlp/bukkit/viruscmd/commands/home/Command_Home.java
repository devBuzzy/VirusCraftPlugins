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
package de.thejeterlp.bukkit.viruscmd.commands.home;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.thejeterlp.bukkit.viruscmd.home.Home;
import de.thejeterlp.bukkit.viruscmd.home.HomeManager;
import net.minecraft.util.com.google.common.base.Joiner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_Home extends BaseCommand {

    public Command_Home() {
        super("home");
        helpPages.add(new CommandHelp("/home", "Lists all homes"));
        helpPages.add(new CommandHelp("/home <homename>", "Teleports you to <homename>"));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        if (args.isEmpty()) {
            String homes = "Homes (" + HomeManager.getHomes(p).size() + "): §r" + Joiner.on(", ").join(HomeManager.getHomes(p).keySet());
            return Utils.sendMessage(MessageType.INFO, p, homes);
        } else {
            Home h = HomeManager.getHome(p, args.getString(0));
            if (h != null) {
                h.teleport();
                return CommandResult.SUCCESS;
            } else {
                return Utils.sendMessage(MessageType.ERROR, p, "That home does not exist.");
            }
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

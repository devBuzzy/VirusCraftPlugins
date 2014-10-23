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
package de.thejeterlp.bukkit.viruscmd.commands.spawn;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.thejeterlp.bukkit.viruscmd.player.PlayerManager;
import de.thejeterlp.bukkit.viruscmd.spawn.Spawn;
import de.thejeterlp.bukkit.viruscmd.spawn.SpawnManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_Spawn extends BaseCommand {

    public Command_Spawn() {
        super("spawn");
        helpPages.add(new CommandHelp("/spawn <group>", "Teleports yourself to the default spawn or the spawn of <group>"));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        if (args.isEmpty()) {
            Spawn s = SpawnManager.getSpawn(PlayerManager.getVCPlayer(p));
            s.teleport(p);
            return Utils.sendMessage(MessageType.INFO, p, "You got teleported to the spawn.");
        } else {
            String group = args.getString(0);
            if (!p.hasPermission(getPermission() + "." + group)) {
                return Utils.sendMessage(MessageType.NOTHING, p, CommandResult.NO_PERMISSION.getMessage().replaceAll("%perm%", getPermission() + "." + group));
            }
            Spawn s = SpawnManager.getGroupSpawn(group);
            if (s == null) {
                return Utils.sendMessage(MessageType.INFO, p, "That group does not have a special spawn.");
            }
            s.teleport(p);
            return Utils.sendMessage(MessageType.INFO, p, "You got teleported to the spawn of the group " + s.getGroup());
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

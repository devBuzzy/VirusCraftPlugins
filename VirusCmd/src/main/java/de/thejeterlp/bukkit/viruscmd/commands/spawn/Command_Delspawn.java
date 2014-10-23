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
import de.thejeterlp.bukkit.viruscmd.spawn.Spawn;
import de.thejeterlp.bukkit.viruscmd.spawn.SpawnManager;
import de.thejeterlp.bukkit.viruscmd.spawn.SpawnType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_Delspawn extends BaseCommand {

    public Command_Delspawn() {
        super("delspawn");      
        helpPages.add(new CommandHelp("/delspawn <group>", "Deletes a group spawn."));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        Spawn s = SpawnManager.getSpawn(args.getString(0));

        if (s == null) {
            return Utils.sendMessage(MessageType.INFO, p, "That group does not have a special spawn.");
        }

        if (s.getType() == SpawnType.DEFAULT) return Utils.sendMessage(MessageType.ERROR, p, "You cannot delete the global spawn!");

        SpawnManager.deleteSpawn(s);
        return Utils.sendMessage(MessageType.INFO, p, "The spawn was deleted.");
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

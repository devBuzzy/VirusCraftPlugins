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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_Down extends BaseCommand {
    
    public Command_Down() {
        super("down");
        helpPages.add(new CommandHelp("/down", "Teleports you down."));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        Location loc = p.getLocation();
        Location target = loc.clone();
        Location target2 = loc.clone();

        boolean found = false;

        Location underFeet = loc.getBlock().getRelative(BlockFace.DOWN, 2).getLocation();

        boolean air = (underFeet.getBlock().getType() == Material.AIR);

        if (air) {
            for (int y = underFeet.getBlockY() - 1; y >= 0; y--) {
                target.setY(y);
                target2.setY(y + 1);
                Location target3 = target2.clone();
                target3.setY(y + 2);

                if (target.getBlock().getType() != Material.AIR && target2.getBlock().getType() == Material.AIR && target3.getBlock().getType() == Material.AIR) {
                    found = true;
                    target.setY(y + 1);
                    break;
                }
            }
        } else {

            for (int y = loc.getBlockY() - 2; y >= 0; y--) {
                target.setY(y);
                target2.setY(y + 1);

                if (target.getBlock().getType() == Material.AIR && target2.getBlock().getType() == Material.AIR) {
                    found = true;
                    break;
                }
            }
        }

        if (found) {
            p.teleport(target);
            return Utils.sendMessage(MessageType.INFO, p, "You have been teleported down!");
        } else {
            return Utils.sendMessage(MessageType.INFO, p, "There is no free block under you!");
        }
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.isEmpty();
    }

}

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
package de.thejeterlp.bukkit.viruscmd.commands.mob;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_Killall extends BaseCommand {

    public Command_Killall() {
        super("killall");
        helpPages.add(new CommandHelp("/killall <all>", "Kills all living entities in your world or in all worlds."));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        if (args.isEmpty()) {
            int killed = 0;
            for (Entity e : p.getWorld().getEntities()) {
                if (e instanceof LivingEntity) {
                    if (e instanceof Player) continue;
                    LivingEntity l = (LivingEntity) e;
                    l.setHealth(0.0);
                    killed++;
                }
            }
            return Utils.sendMessage(MessageType.INFO, p, killed + " entities have been killed.");
        } else {
            if (args.getString(0).equalsIgnoreCase("all")) {
                int killed = 0;
                for (World w : p.getServer().getWorlds()) {
                    for (Entity e : w.getEntities()) {
                        if (e instanceof LivingEntity) {
                            if (e instanceof Player) continue;
                            LivingEntity l = (LivingEntity) e;
                            l.setHealth(0.0);
                            killed++;
                        }
                    }
                }
                return Utils.sendMessage(MessageType.INFO, p, killed + " entities have been killed.");
            } else {
                return CommandResult.ERROR;
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

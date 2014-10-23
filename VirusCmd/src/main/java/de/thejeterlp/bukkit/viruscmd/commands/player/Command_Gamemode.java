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
import de.thejeterlp.bukkit.viruscmd.player.PlayerManager;
import de.thejeterlp.bukkit.viruscmd.player.VCPlayer;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_Gamemode extends BaseCommand {

    public Command_Gamemode() {
        super("gamemode");
        helpPages.add(new CommandHelp("/gamemode", "Toggles your gamemode"));
        helpPages.add(new CommandHelp("/gamemode <0|1|2>", "Changes your gamemode"));
        helpPages.add(new CommandHelp("/gamemode <player>", "Toggles <player>'s gamemode"));
        helpPages.add(new CommandHelp("/gamemode <player> <0|1|2>", "Changes <player>'s gamemode"));        
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {       
        if (args.isEmpty()) {
            GameMode gm = p.getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL;
            p.setGameMode(gm);
            return Utils.sendMessage(MessageType.INFO, p, "Your gamemode has changed to " + gm.toString().toLowerCase());
        } else {
            if (args.isInteger(0)) {
                int num = args.getInt(0);
                if (num > 2) return CommandResult.ERROR;
                GameMode gm;
                switch (num) {
                    case 0:
                        gm = GameMode.SURVIVAL;
                        break;
                    case 1:
                        gm = GameMode.CREATIVE;
                        break;
                    case 2:
                        gm = GameMode.ADVENTURE;
                        break;
                    default:
                        gm = GameMode.SURVIVAL;
                        break;
                }
                p.setGameMode(gm);
                return Utils.sendMessage(MessageType.INFO, p, "Your gamemode has changed to " + gm.toString().toLowerCase());
            } else {
                if (!hasPermission(p, true)) {
                    return CommandResult.NO_PERMISSION_OTHER;
                }
                if (!args.isPlayer(0)) return CommandResult.NOT_ONLINE;
                Player target = args.getPlayer(0);
                VCPlayer vctarget = PlayerManager.getVCPlayer(target);
                if (args.getLength() == 1) {
                    GameMode gm = target.getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL;
                    target.setGameMode(gm);
                    Utils.sendMessage(MessageType.INFO, p, vctarget.getDisplayName() + "'s gamemode has changed to " + gm.toString().toLowerCase());
                    return Utils.sendMessage(MessageType.INFO, target, "Your gamemode has changed to " + gm.toString().toLowerCase());
                } else {
                    if (!args.isInteger(1)) return CommandResult.NOT_A_NUMBER;
                    int num = args.getInt(1);
                    if (num > 2) return CommandResult.ERROR;
                    GameMode gm;
                    switch (num) {
                        case 0:
                            gm = GameMode.SURVIVAL;
                            break;
                        case 1:
                            gm = GameMode.CREATIVE;
                            break;
                        case 2:
                            gm = GameMode.ADVENTURE;
                            break;
                        default:
                            gm = GameMode.SURVIVAL;
                            break;
                    }
                    target.setGameMode(gm);
                    Utils.sendMessage(MessageType.INFO, p, vctarget.getDisplayName() + "'s gamemode has changed to " + gm.toString().toLowerCase());
                    return Utils.sendMessage(MessageType.INFO, target, "Your gamemode has changed to " + gm.toString().toLowerCase());

                }
            }
        }
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() <= 2;
    }

}

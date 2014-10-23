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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author TheJeterLP
 */
public class Command_Slap extends BaseCommand {

    public Command_Slap() {
        super("slap");
        helpPages.add(new CommandHelp("/slap <player> <1-9>", "Slaps <player> with the given power"));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        if (!args.isPlayer(0)) return CommandResult.NOT_ONLINE;
        if (!args.isInteger(1)) return CommandResult.NOT_A_NUMBER;

        int power = args.getInt(1);

        if (power < 1 || power > 9) {
            return Utils.sendMessage(MessageType.ERROR, p, "The power has to be between 1 and 9!");
        }

        final Player target = args.getPlayer(0);
        VCPlayer vctarget = PlayerManager.getVCPlayer(target);
        Vector dir = target.getLocation().getDirection().multiply(power);
        target.setVelocity(dir.setY(power));
        target.setFallDistance(-1000.0F);
        return Utils.sendMessage(MessageType.INFO, p, vctarget.getDisplayName() + " was slapped.");
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception {
        return CommandResult.ONLY_PLAYER;
    }
    
    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() == 2;
    }

}

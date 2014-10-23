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

/**
 * @author TheJeterLP
 */
public class Command_Vanish extends BaseCommand {

    public Command_Vanish() {
        super("vanish");
        helpPages.add(new CommandHelp("/vanish <player>", "Toggles vanish for you or <player>"));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        if (args.isEmpty()) {
            VCPlayer vcp = PlayerManager.getVCPlayer(p);
            if (vcp == null) return Utils.sendMessage(MessageType.ERROR, p, "Could not get the VCPlayer object!");
            vcp.setInvisible(!vcp.invisible());
            String s = vcp.invisible() ? "vanished" : "visible";
            return Utils.sendMessage(MessageType.INFO, p, "You have been become " + s);
        } else {
            if (!hasPermission(p, true)) return CommandResult.NO_PERMISSION_OTHER;
            if (!args.isPlayer(0)) return CommandResult.NOT_ONLINE;
            Player target = args.getPlayer(0);
            VCPlayer vctarget = PlayerManager.getVCPlayer(target);
            if (vctarget == null) return Utils.sendMessage(MessageType.ERROR, p, "Could not get the VCPlayer object!");
            vctarget.setInvisible(!vctarget.invisible());
            String s = vctarget.invisible() ? "vanished" : "visible";
            Utils.sendMessage(MessageType.INFO, p, vctarget.getDisplayName() + " has been become " + s);
            return Utils.sendMessage(MessageType.INFO, target, "You have been become " + s);
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

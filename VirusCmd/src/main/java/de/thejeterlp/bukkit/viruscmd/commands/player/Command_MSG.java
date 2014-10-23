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
import de.thejeterlp.bukkit.viruscmd.player.PlayerManager;
import de.thejeterlp.bukkit.viruscmd.player.VCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_MSG extends BaseCommand {

    public Command_MSG() {
        super("msg");
        helpPages.add(new CommandHelp("/msg <player> <msg>", "Sends <player> <msg>"));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        if (!args.isPlayer(0)) return CommandResult.NOT_ONLINE;
        Player target = args.getPlayer(0);
        VCPlayer vctarget = PlayerManager.getVCPlayer(target);
        VCPlayer vcp = PlayerManager.getVCPlayer(p);

        if (vcp.isMuted()) {
            return vcp.sendMessage(MessageType.ERROR, "You are muted!");
        }

        String msg = "";
        for (int i = 1; i < args.getLength(); i++) {
            msg += args.getString(i) + " ";
        }
        target.sendMessage("§c[" + vctarget.getPrefix() + vctarget.getDisplayName() + vctarget.getSuffix() + "§7 <- §c" + vcp.getPrefix() + vcp.getDisplayName() + vcp.getSuffix() + "]§r " + msg);
        p.sendMessage("§c[" + vcp.getPrefix() + vcp.getDisplayName() + vcp.getSuffix() + "§7 -> §c" + vctarget.getPrefix() + vctarget.getDisplayName() + vctarget.getSuffix()  + "]§r " + msg);

        for (Player op : Bukkit.getOnlinePlayers()) {
            VCPlayer ovcp = PlayerManager.getVCPlayer(op);
            if (ovcp.getID() == vcp.getID()) continue;

            if (ovcp.spy()) {
                ovcp.getPlayer().sendMessage("§c[" + vcp.getPrefix() + vcp.getDisplayName() + vcp.getSuffix() + "§7 -> §c" + vctarget.getPrefix() + vctarget.getDisplayName() + vctarget.getSuffix() + "]§r " + msg);
            }
        }
        return CommandResult.SUCCESS;
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception {
        return CommandResult.ONLY_PLAYER;
    }
    
    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() >= 2;
    }

}

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
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_Whois extends BaseCommand {
    
    public Command_Whois() {
        super("whois");
        helpPages.add(new CommandHelp("/whois <player>", "Checkout <player>. (Has support for offline players)"));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        OfflinePlayer op = args.getOfflinePlayer(0);
        VCPlayer vctarget = PlayerManager.getVCPlayer(op);
        if (vctarget == null) return Utils.sendMessage(MessageType.ERROR, p, "That player hasn't played yet.");
        Utils.sendMessage(MessageType.INFO, p, "Name: " + vctarget.getOfflinePlayer().getName());
        Utils.sendMessage(MessageType.INFO, p, "Nickname: " + vctarget.getDisplayName());
        Utils.sendMessage(MessageType.INFO, p, "CommandWatcher: " + (vctarget.commandwatcher() ? "enabled" : "disabled"));
        Utils.sendMessage(MessageType.INFO, p, "Fly: " + (vctarget.fly() ? "enabled" : "disabled"));
        Utils.sendMessage(MessageType.INFO, p, "God: " + (vctarget.god() ? "enabled" : "disabled"));
        Utils.sendMessage(MessageType.INFO, p, "Spy: " + (vctarget.spy() ? "enabled" : "disabled"));
        Utils.sendMessage(MessageType.INFO, p, "Invisibility: " + (vctarget.invisible() ? "enabled" : "disabled"));
        Utils.sendMessage(MessageType.INFO, p, "Player ID: " + vctarget.getID());
        if (op.isOnline()) {
            Utils.sendMessage(MessageType.INFO, p, "Gamemode: " + vctarget.getPlayer().getGameMode().toString().toLowerCase());
            Utils.sendMessage(MessageType.INFO, p, "IP: " + vctarget.getPlayer().getAddress().getAddress().toString());
            Utils.sendMessage(MessageType.INFO, p, "Group: " + vctarget.getGroup());
            Utils.sendMessage(MessageType.INFO, p, "Prefix: " + vctarget.getPrefix());
            Utils.sendMessage(MessageType.INFO, p, "Suffix: " + vctarget.getSuffix());
        }
        return CommandResult.SUCCESS;
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

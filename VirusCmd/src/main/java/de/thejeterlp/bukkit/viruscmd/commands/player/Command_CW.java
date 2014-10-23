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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Command_CW extends BaseCommand {

    public Command_CW() {
        super("cw");
        helpPages.add(new CommandHelp("/cw", "Toggles Commandwatcher for you."));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        VCPlayer vcp = PlayerManager.getVCPlayer(p);
        vcp.setCommandWatcher(!vcp.commandwatcher());
        String s = vcp.commandwatcher() ? "enabled" : "disabled";
        return vcp.sendMessage(MessageType.INFO, "Your commandwatcher was " + s);
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

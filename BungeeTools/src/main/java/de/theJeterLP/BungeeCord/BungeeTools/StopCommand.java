package de.theJeterLP.BungeeCord.BungeeTools;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

/**
 * @author TheJeterLP
 */
public class StopCommand extends Command {

    public StopCommand() {
        super("stop");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("§4You can only use this command from the console!");
            return;
        }
        BungeeCord.getInstance().stop();
    }

}

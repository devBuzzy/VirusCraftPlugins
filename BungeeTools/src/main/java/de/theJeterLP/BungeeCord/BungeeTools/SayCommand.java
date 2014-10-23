package de.theJeterLP.BungeeCord.BungeeTools;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author TheJeterLP
 */
public class SayCommand extends Command {

    public SayCommand() {
        super("say", "commands.say");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String msg = "";
        for (String s : args) {
            msg += s + " ";
        }
        ProxyServer.getInstance().broadcast("§e[Broadcast] §7" + msg);
    }

}

package de.theJeterLP.BungeeCord.BungeeTools;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
* @author TheJeterLP
*/
public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("btools", "btools.reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            Main.reload();
        } catch (Exception ex) {
            Logger.getLogger(ReloadCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

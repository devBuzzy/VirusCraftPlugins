package de.TheJeterLP.Bukkit.VirusCraftTools.commands.Essential;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.FileUpdate;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TheJeterLP
 */
public class Reload extends BaseCommand {

    public Reload() {
        super("load", "tools.reload");
        helpPages.add(new CommandHelp("/load <plugin>", "Reloads a plugin."));
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        return onServerCommand(p, cmd, args);
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception {
        Plugin pl = VCplugin.inst().getServer().getPluginManager().getPlugin(args.getString(0));
        if (pl == null) {
            return Utils.sendMessage(MessageType.ERROR, sender, "The plugin  " + args.getString(0) + " was not found.");
        }

        if (pl instanceof JavaPlugin) {
            if (pl instanceof VCplugin) {
                return Utils.sendMessage(MessageType.ERROR, sender, "VirusCraftTools cannot be reloaded from the disk because it uses the Addon system.");
            }
            
            JavaPlugin p = (JavaPlugin) pl;
            FileUpdate.reload(p);
            return Utils.sendMessage(MessageType.INFO, sender, "The plugin " + p.getName() + " was reloaded.");
        } else {
            return Utils.sendMessage(MessageType.ERROR, sender, "Plugin is not a JavaPlugin!");
        }
    }

    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() == 1;
    }
}

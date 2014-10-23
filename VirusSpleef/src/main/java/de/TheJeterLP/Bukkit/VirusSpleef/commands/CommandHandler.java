package de.TheJeterLP.Bukkit.VirusSpleef.commands;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class CommandHandler extends BaseCommand {

    public CommandHandler() {
        super("spleef", null);
        helpPages.add(new CommandHelp("Step 1: /spleef create <id> <maxplayers>", "Creates a new arena."));
        helpPages.add(new CommandHelp("Step 2: /spleef setspawn <id> <spawn|lobby>", "Sets the given spawn point to your location."));
        helpPages.add(new CommandHelp("Step 3: /spleef setsign <id>", "Sets the Sign to the location you are looking to."));
        helpPages.add(new CommandHelp("Step 4: /spleef finish <id>", "Finishes the arena."));
        helpPages.add(new CommandHelp("Optional: /spleef delete <id>", "Deletes the arena."));
        helpPages.add(new CommandHelp("Optional: /spleef restart <id>", "Restarts the arena."));
        helpPages.add(new CommandHelp("Optional: /spleef leave", "Leave an arena."));
        helpPages.add(new CommandHelp("Optional: /spleef join", "Join a game."));
        subcmds.put("leave", new Leave());
        subcmds.put("create", new Create());
        subcmds.put("delete", new Delete());
        subcmds.put("finish", new Finish());
        subcmds.put("restart", new Restart());
        subcmds.put("setsign", new Setsign());
        subcmds.put("setspawn", new Setspawn());
        subcmds.put("join", new Join());
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) {
        return CommandResult.ERROR;
    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public boolean argsCheck(CommandArgs args) {
        return !args.isEmpty();
    }

}

package de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    private final String perm;

    public SubCommand(String permission) {
        this.perm = permission;
    }

    public String getPermission() {
        return perm;
    }

    public abstract CommandResult executeConsole(CommandSender sender, CommandArgs args) throws Exception;

    public abstract CommandResult executePlayer(Player p, CommandArgs args) throws Exception;

    protected boolean hasPermission(Player player, boolean other) {
        if (perm == null || perm.isEmpty()) return true;
        String permi = perm + (other ? ".other" : "");
        return player.hasPermission(permi) || player.isOp();
    }

}

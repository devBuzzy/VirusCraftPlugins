package de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command;

import org.bukkit.ChatColor;

/**
 * @author TheJeterLP
 */
public enum CommandResult {

    SUCCESS(null),
    NO_PERMISSION(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GRAY + "You don't have permission for this! " + ChatColor.RED + "(%perm%)"),
    ERROR(ChatColor.RED + "[ERROR] " + ChatColor.GRAY + "Wrong usage! Please type " + ChatColor.GOLD + "/%cmd% help " + ChatColor.GRAY + "!"),
    ONLY_PLAYER(ChatColor.RED + "This command is only for players!"),
    NOT_ONLINE(ChatColor.RED + "That player is not online."),
    NOT_A_NUMBER(ChatColor.RED + "It has to be a number!");
    private final String msg;

    CommandResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }
}

package de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command;

import org.bukkit.ChatColor;

/**
 * @author TheJeterLP
 */
public class CommandHelp {

    private final String FULL_TEXT;

    public CommandHelp(String cmd, String description) {
        this.FULL_TEXT = ChatColor.GOLD + cmd + ChatColor.GRAY + " - " + description;
    }

    public String getText() {
        return FULL_TEXT;
    }

}

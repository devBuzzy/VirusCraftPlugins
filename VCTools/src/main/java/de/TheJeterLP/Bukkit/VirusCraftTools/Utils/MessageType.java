package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import org.bukkit.ChatColor;

/**
 *
 * @author TheJeterLP
 */
public enum MessageType {

    INFO(ChatColor.GOLD + "[INFO] " + ChatColor.GREEN),
    ERROR(ChatColor.RED + "[ERROR] " + ChatColor.GRAY),
    DEBUG(ChatColor.GOLD + "[DEBUG] " + ChatColor.AQUA),
    NOTHING(null);
    String msg;

    MessageType(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        if (msg == null) {
            return "";
        }
        return this.msg;
    }
}

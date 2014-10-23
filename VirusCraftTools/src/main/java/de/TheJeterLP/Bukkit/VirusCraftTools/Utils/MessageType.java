package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import org.bukkit.ChatColor;

/**
 *
 * @author TheJeterLP
 */
public enum MessageType {

    INFO("§b[INFO]§7 "),
    ERROR(ChatColor.RED + "[ERROR]§7 "),
    PARTY(ChatColor.LIGHT_PURPLE + "[Party]§e "),
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

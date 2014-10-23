package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageManager {

    private final Game g;

    public MessageManager(Game g) {
        this.g = g;
    }

    public enum PrefixType {

        INFO(ChatColor.GRAY),
        GOOD(ChatColor.GOLD),
        BAD(ChatColor.RED),
        NORMAL(ChatColor.RESET);

        private final ChatColor color;

        PrefixType(ChatColor color) {
            this.color = color;
        }

        public ChatColor getColor() {
            return color;
        }
    }

    public enum LogType {

        INFO(ChatColor.YELLOW),
        GOOD(ChatColor.BLUE),
        BAD(ChatColor.RED),
        SEVERE(ChatColor.DARK_RED);

        private final ChatColor color;

        LogType(ChatColor color) {
            this.color = color;
        }

        public ChatColor getColor() {
            return color;
        }

    }

    public void message(CommandSender sender, PrefixType type, String... messages) {
        for (String m : messages) {
            sender.sendMessage(g.getPrefix() + type.getColor() + m);
        }
    }

    public void log(LogType type, String... messages) {
        for (String m : messages) {
            Bukkit.getConsoleSender().sendMessage(g.getPrefix() + type.getColor() + m);
        }
    }

}

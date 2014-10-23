package de.TheJeterLP.Bukkit.VirusSpleef.Arena;

import org.bukkit.ChatColor;

/**
 * @author TheJeterLP
 */
public enum ArenaState {

    WAITING(ChatColor.GREEN + "Lobby"),
    COUNTDING_DOWN(ChatColor.GREEN + "Countdown"),
    STARTED(ChatColor.AQUA + "Ingame"),
    WINNED(ChatColor.AQUA + "Winner!"),
    RESETTING(ChatColor.RED + "Resetting");

    private final String text;

    private ArenaState(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

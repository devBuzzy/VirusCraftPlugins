package de.TheJeterLP.Bukkit.VirusGames.Arena;

import org.bukkit.ChatColor;

/**
 * @author TheJeterLP
 */
public enum Team {

    PLAYER("Player", ChatColor.RED),
    VIRUS("Virus", ChatColor.DARK_BLUE),
    LOBBY("Lobby", ChatColor.WHITE);
    private final String name;
    private final ChatColor color;

    Team(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public Team getOtherTeam() {
        if (name.equalsIgnoreCase(PLAYER.getName())) return VIRUS;
        else return PLAYER;
    }

}

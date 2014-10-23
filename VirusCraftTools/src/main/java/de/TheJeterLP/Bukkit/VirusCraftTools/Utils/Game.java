package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import org.bukkit.ChatColor;

/**
 * @author TheJeterLP
 */
public enum Game {

    SPLEEF("§6[§9Spleef§6] "),
    VIRUSGAMES("§6[§9VirusGames§6] "),
    VILLAGERHUNT("§6[§9VillagerHunt§6] "),
    CAKEPOKE("§6[§9CakePoke§6] ");

    private final String prefix;

    private Game(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix + ChatColor.RESET;
    }

    public String getDBName() {
        return toString().toLowerCase() + ".db";
    }
}

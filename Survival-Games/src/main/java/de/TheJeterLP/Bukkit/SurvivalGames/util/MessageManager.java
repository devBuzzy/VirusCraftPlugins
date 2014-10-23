package de.TheJeterLP.Bukkit.SurvivalGames.util;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager {

    private static final MessageManager instance = new MessageManager();
    private final String pre = ChatColor.BLUE + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "Survival-Games" + ChatColor.BLUE + "" + ChatColor.BOLD + "] " + ChatColor.RESET;
    private final HashMap<PrefixType, String> prefix = new HashMap<>();

    public enum PrefixType {

        MAIN,
        INFO,
        WARNING,
        ERROR;

    }

    public static MessageManager getInstance() {
        return instance;
    }

    public String getPrefix() {
        return pre;
    }

    public void setup() {
        prefix.put(PrefixType.MAIN, "§6[§5Survival-Games§6]");
        prefix.put(PrefixType.INFO, "§a[§5Survival-Games§a]");
        prefix.put(PrefixType.WARNING, "§c[§5Survival-Games§c]");
        prefix.put(PrefixType.ERROR, "§4[§5Survival-Games§4]");

    }

    public void sendFMessage(PrefixType type, String input, Player player, String... args) {
        String msg = SettingsManager.getInstance().getMessageConfig().getString("messages." + input);
        if (args != null && args.length != 0) {
            msg = MessageUtil.replaceVars(msg, args);
        }
        msg = MessageUtil.replaceColors(msg);
        player.sendMessage(prefix.get(type) + " " + msg);

    }

    public void sendMessage(PrefixType type, String msg, Player player) {
        player.sendMessage(prefix.get(type) + " " + msg);
    }

    public void logMessage(PrefixType type, String msg) {
        Logger logger = Bukkit.getServer().getLogger();
        switch (type) {
            case INFO:
                logger.info(prefix.get(type) + msg);
                break;
            case WARNING:
                logger.warning(prefix.get(type) + msg);
                break;
            case ERROR:
                logger.severe(prefix.get(type) + msg);
                break;
            default:
                break;
        }
    }

    public void broadcastFMessage(PrefixType type, String input, String... args) {
        String msg = SettingsManager.getInstance().getMessageConfig().getString("messages." + input);
        if (args != null && args.length != 0) {
            msg = MessageUtil.replaceVars(msg, args);
        }
        msg = MessageUtil.replaceColors(msg);
        Bukkit.broadcastMessage(prefix.get(type) + " " + msg);
    }

    public void broadcastMessage(PrefixType type, String msg, Player player) {
        Bukkit.broadcastMessage(prefix.get(type) + " " + msg);
    }

}

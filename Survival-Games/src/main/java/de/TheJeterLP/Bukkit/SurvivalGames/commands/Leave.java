package de.TheJeterLP.Bukkit.SurvivalGames.commands;

import org.bukkit.entity.Player;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.GameManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.SettingsManager;

public class Leave implements SubCommand {

    public boolean onCommand(Player player, String[] args) {
        if (GameManager.getInstance().getPlayerGameId(player) == -1) {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.notinarena", player);
        } else {
            GameManager.getInstance().removePlayer(player, false);
        }
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg leave - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.leave", "Leaves the game");
    }

    @Override
    public String permission() {
        return null;
    }
}

package de.TheJeterLP.Bukkit.SurvivalGames.commands;

import org.bukkit.entity.Player;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.GameManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.SettingsManager;

public class LeaveQueue implements SubCommand {

    @Override
    public boolean onCommand(Player player, String[] args) {
        GameManager.getInstance().removeFromOtherQueues(player, -1);
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg lq - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.leavequeue", "Leave the queue for any queued games");
    }

    @Override
    public String permission() {
        return null;
    }

}

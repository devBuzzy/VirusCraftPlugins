package de.TheJeterLP.Bukkit.SurvivalGames.commands;

import org.bukkit.entity.Player;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.GameManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.SettingsManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageManager.PrefixType;

public class Join implements SubCommand {

    public boolean onCommand(Player player, String[] args) {
        if (args.length == 1) {
            try {
                int a = Integer.parseInt(args[0]);
                GameManager.getInstance().addPlayer(player, a);
            } catch (NumberFormatException e) {
                MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.notanumber", player, "input-" + args[0]);
            }

        } else {
            if (GameManager.getInstance().getPlayerGameId(player) != -1) {
                MessageManager.getInstance().sendMessage(PrefixType.ERROR, "error.alreadyingame", player);
                return true;
            }
            player.teleport(SettingsManager.getInstance().getLobbySpawn());
            return true;
        }
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg join - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.join", "Join the lobby");
    }

    @Override
    public String permission() {
        return "sg.arena.join";
    }
}

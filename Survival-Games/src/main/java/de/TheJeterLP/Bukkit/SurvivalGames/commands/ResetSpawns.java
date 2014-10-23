package de.TheJeterLP.Bukkit.SurvivalGames.commands;

import org.bukkit.entity.Player;
import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.SettingsManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageManager.PrefixType;

public class ResetSpawns implements SubCommand {

    public boolean onCommand(Player player, String[] args) {

        if (!player.hasPermission(permission()) && !player.isOp()) {
            MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.nopermission", player);
            return true;
        }
        try {
            SettingsManager.getInstance().getSpawns().set("spawns." + Integer.parseInt(args[0]), null);
            return true;
        } catch (NumberFormatException e) {
            MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.notanumber", player, "input-Arena");
        } catch (NullPointerException e) {
            MessageManager.getInstance().sendMessage(MessageManager.PrefixType.ERROR, "error.gamenoexist", player);
        }
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg resetspawns <id> - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.resetspawns", "Resets spawns for Arena <id>");
    }

    @Override
    public String permission() {
        return "sg.admin.resetspawns";
    }
}

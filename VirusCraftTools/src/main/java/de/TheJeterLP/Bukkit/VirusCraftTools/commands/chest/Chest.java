package de.TheJeterLP.Bukkit.VirusCraftTools.commands.chest;

import de.TheJeterLP.Bukkit.VirusCraftTools.Chest.VirtualChestManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandHelp;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author TheJeterLP
 */
public class Chest extends BaseCommand {
    
    private final VirtualChestManager chestManager = VCplugin.inst().getChestManager();
    
    public Chest() {
        super("chest", "chest.chest");
        helpPages.add(new CommandHelp("/chest", "Open your chest."));
        helpPages.add(new CommandHelp("/chest <player>", "Opens <player>'s chest."));
    }
    
    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) {        
        if (p.getGameMode().equals(GameMode.CREATIVE) && !p.hasPermission(getPermission() + ".creative")) {
            return CommandResult.NO_PERMISSION;
        }
        if (args.isEmpty()) {
            Inventory chest = chestManager.getChest(p);
            p.openInventory(chest);
            return CommandResult.SUCCESS;
        } else {
            if (hasPermission(p, true)) {
                if (!args.isPlayer(0)) return CommandResult.NOT_ONLINE;
                Player target = args.getPlayer(0);
                Inventory chest = chestManager.getChest(target);
                p.openInventory(chest);
                Utils.sendMessage(MessageType.INFO, p, "Opening " + target.getName() + "'s chest...");
                return CommandResult.SUCCESS;
            } else {
                return CommandResult.NO_PERMISSION;
            }
        }
    }
    
    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) {
        return CommandResult.ONLY_PLAYER;
    }
    
    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() <= 1;
    }
    
}

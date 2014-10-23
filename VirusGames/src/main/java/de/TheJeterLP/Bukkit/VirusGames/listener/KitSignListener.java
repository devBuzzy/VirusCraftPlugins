package de.TheJeterLP.Bukkit.VirusGames.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager.PrefixType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VirusGames.Arena.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import de.TheJeterLP.Bukkit.VirusGames.Arena.Class;
import de.TheJeterLP.Bukkit.VirusGames.Arena.Team;
import de.TheJeterLP.Bukkit.VirusGames.Bukkit.VirusGames;

public class KitSignListener extends VClistener {

    @EventHandler
    public void onKitSign(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
            Sign s = (Sign) e.getClickedBlock().getState();
            String one = Utils.removeColors(s.getLine(0));
            String two = Utils.removeColors(s.getLine(1));
            String three = Utils.removeColors(s.getLine(2));
            if (!one.equalsIgnoreCase("[VG]")) return;
            if (two.equalsIgnoreCase("[CLASS]")) {
                if (ArenaManager.getArena(e.getPlayer()) == null) {
                    e.setCancelled(true);
                    return;
                }

                Arena a = ArenaManager.getArena(e.getPlayer());
                
                Class c = null;
                Utils.clearInventory(e.getPlayer());
                c = Class.valueOf(three.toUpperCase());
                if (c == null) return;
                if ((c == Class.VIRUS || c == Class.SUPER_VIRUS) && a.getViruses() > 5) {
                    VirusGames.getMessageManager().message(e.getPlayer(), PrefixType.INFO, "There are already five viruses. Please choose a player class.");
                    e.setCancelled(true);
                    return;
                }
                if (c == Class.SUPER_VIRUS && !e.getPlayer().hasPermission("virusgames.super")) {
                    VirusGames.getMessageManager().message(e.getPlayer(), PrefixType.BAD, "If you want to play the super_virus class, you have to donate to us!");
                    e.setCancelled(true);
                    return;
                }
                a.getPlayer(e.getPlayer()).setClass(c);
                if (c == Class.VIRUS || c == Class.SUPER_VIRUS) {
                    a.getPlayer(e.getPlayer()).setTeam(Team.VIRUS);
                } else {
                    a.getPlayer(e.getPlayer()).setTeam(Team.PLAYER);
                }
                VirusGames.getMessageManager().message(e.getPlayer(), PrefixType.NORMAL, ChatColor.GREEN + "You chose to play as a " + ChatColor.GOLD + c.toString().toLowerCase());
                if (a.getState() == ArenaState.COUNTDING_DOWN) {
                    VirusGames.getMessageManager().message(e.getPlayer(), PrefixType.INFO, "The game starts in " + e.getPlayer().getLevel() + " seconds.");
                }
            }
        }

    }
}

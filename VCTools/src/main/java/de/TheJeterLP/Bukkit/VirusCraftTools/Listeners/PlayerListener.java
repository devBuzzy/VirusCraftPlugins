package de.TheJeterLP.Bukkit.VirusCraftTools.Listeners;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.*;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

/**
 * @author TheJeterLP
 */
public class PlayerListener extends VClistener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onDeath(final PlayerDeathEvent e) {
        if (Config.AUTORESPAWN.getBoolean())
            Utils.respawn(e.getEntity());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        String message;
        if (!event.getPlayer().hasPlayedBefore()) {
            message = Utils.formatWithPlayer(Config.MESSAGES_FIRSTJOIN.getString(), event.getPlayer());
        } else {
            message = Utils.formatWithPlayer(Config.MESSAGES_JOIN.getString(), event.getPlayer());
        }
        event.setJoinMessage(message);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void setQuitMessage(PlayerQuitEvent event) throws SQLException {
        String message = Utils.formatWithPlayer(Config.MESSAGES_QUIT.getString(), event.getPlayer());
        event.setQuitMessage(message);
    }

    @EventHandler
    public void setKickMessage(PlayerKickEvent event) throws SQLException {
        String message = Utils.formatWithPlayer(Config.MESSAGES_KICK.getString(), event.getPlayer());
        event.setLeaveMessage(message);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(final PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) {
            if (e.getClickedBlock().getType() == Material.STONE_PLATE || e.getClickedBlock().getType() == Material.WOOD_PLATE) {
                Block b = e.getClickedBlock();
                Sign s = null;

                if (b.getRelative(BlockFace.DOWN, 2).getState() instanceof Sign) {
                    s = (Sign) b.getRelative(BlockFace.DOWN, 2).getState();
                }

                if (s != null) {
                    if (s.getLine(0).equalsIgnoreCase("[launch]")) {
                        final Player player = e.getPlayer();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(VCplugin.inst(), new Runnable() {
                            @Override
                            public void run() {
                                Utils.launchPlayer(player);
                            }
                        });

                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignChange(final SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[launch]") && !e.getPlayer().hasPermission("tools.launchpad")) {
            e.getBlock().breakNaturally();
        }
    }
}

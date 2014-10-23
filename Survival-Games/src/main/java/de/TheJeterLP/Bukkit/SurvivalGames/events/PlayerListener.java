/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.TheJeterLP.Bukkit.SurvivalGames.events;

import de.TheJeterLP.Bukkit.SurvivalGames.arena.Game;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.GameManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.SettingsManager;
import de.TheJeterLP.Bukkit.SurvivalGames.SurvivalGames;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author TheJeterLP
 */
public class PlayerListener extends VClistener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String m = event.getMessage();

        if (!GameManager.getInstance().isPlayerActive(event.getPlayer()) && !GameManager.getInstance().isPlayerInactive(event.getPlayer()) && !GameManager.getInstance().isSpectator(event.getPlayer()))
            return;
        if (m.equalsIgnoreCase("/list")) {
            event.getPlayer().sendMessage(
                    GameManager.getInstance().getStringList(
                            GameManager.getInstance().getPlayerGameId(event.getPlayer())));
            return;
        }
        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("sg.staff.nocmdblock"))
            return;
        else if (m.startsWith("/sg") || m.startsWith("/survivalgames") || m.startsWith("/hg") || m.startsWith("/hungergames") || m.startsWith("/msg")) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        int pid = GameManager.getInstance().getPlayerGameId(p);

        if (pid == -1 || p.isOp()) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDieEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
        } else
            return;
        Player player = (Player) event.getEntity();
        int gameid = GameManager.getInstance().getPlayerGameId(player);
        if (gameid == -1)
            return;
        if (!GameManager.getInstance().isPlayerActive(player))
            return;
        Game game = GameManager.getInstance().getGame(gameid);
        if (game.getMode() != Game.GameMode.INGAME) {
            event.setCancelled(true);
            return;
        }
        if (game.isProtectionOn()) {
            event.setCancelled(true);
            return;
        }
        if (player.getHealth() <= event.getDamage()) {
            event.setCancelled(true);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setFireTicks(0);
            PlayerInventory inv = player.getInventory();
            Location l = player.getLocation();

            for (ItemStack i : inv.getContents()) {
                if (i != null)
                    l.getWorld().dropItemNaturally(l, i);
            }
            for (ItemStack i : inv.getArmorContents()) {
                if (i != null && i.getTypeId() != 0)
                    l.getWorld().dropItemNaturally(l, i);
            }

            GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(player)).killPlayer(player, false);

        }
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        if (GameManager.getInstance().getBlockGameId(p.getLocation()) != -1) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(SurvivalGames.getInstance(), new Runnable() {
                @Override
                public void run() {
                    p.teleport(SettingsManager.getInstance().getLobbySpawn());
                }
            });
        }
    }

    @EventHandler
    public void PlayerLoggout(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        GameManager.getInstance().removeFromOtherQueues(p, -1);
        int id = GameManager.getInstance().getPlayerGameId(p);
        if (GameManager.getInstance().isSpectator(p))
            GameManager.getInstance().removeSpectator(p);
        if (id == -1) return;
        if (GameManager.getInstance().getGameMode(id) == Game.GameMode.INGAME)
            GameManager.getInstance().getGame(id).killPlayer(p, true);
        else
            GameManager.getInstance().getGame(id).removePlayer(p, true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void frozenSpawnHandler(PlayerMoveEvent e) {
        int gid = GameManager.getInstance().getPlayerGameId(e.getPlayer());

        if (gid == -1) return;

        Game g = GameManager.getInstance().getGame(gid);
        if (g.getMode() == Game.GameMode.INGAME) return;

        if (GameManager.getInstance().isPlayerActive(e.getPlayer())) {
            e.getPlayer().teleport(e.getFrom());
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        int id = GameManager.getInstance().getPlayerGameId(p);

        if (id == -1 || p.isOp()) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void clickHandler(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || !(e.getClickedBlock() instanceof Sign)) return;
        Sign thisSign = (Sign) e.getClickedBlock().getState();
        String[] lines = thisSign.getLines();
        if (lines.length < 3) return;
        if (Utils.removeColors(lines[0]).equalsIgnoreCase("[SG]")) {
            e.setCancelled(true);
            String game = Utils.removeColors(lines[2]).replace("Arena ", "");
            int gameno = Integer.parseInt(game);
            GameManager.getInstance().addPlayer(e.getPlayer(), gameno);
        }
    }

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent event) {
        Player p = event.getPlayer();
        int id = GameManager.getInstance().getPlayerGameId(p);
        if (id == -1) return;
        if (GameManager.getInstance().getGame(id).isPlayerActive(p) && event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
            p.sendMessage(ChatColor.RED + " Cannot teleport while ingame!");
            event.setCancelled(true);
        }
    }

}

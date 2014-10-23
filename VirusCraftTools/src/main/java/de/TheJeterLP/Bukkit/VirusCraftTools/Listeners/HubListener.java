package de.TheJeterLP.Bukkit.VirusCraftTools.Listeners;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author TheJeterLP
 */
public class HubListener extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (p.isOp()) return;
            if (Utils.isAtHub(p)) {
                e.setCancelled(true);
                p.updateInventory();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryThrow(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (p.isOp()) return;
        if (Utils.isAtHub(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPickup(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        if (p.isOp()) return;
        if (Utils.isAtHub(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void giveItems(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (!Utils.isAtHub(player)) return;
        player.getInventory().clear();
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName("§9Menu");
        book.setItemMeta(meta);
        player.getInventory().addItem(book);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (!Utils.isAtHub(player)) return;
        e.getDrops().clear();
    }

    @EventHandler(ignoreCancelled = true)
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (!Utils.isAtHub(player)) return;
        player.getInventory().clear();
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName("§9Menu");
        book.setItemMeta(meta);
        player.getInventory().addItem(book);
    }

}

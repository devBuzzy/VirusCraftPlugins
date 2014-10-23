/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thejeterlp.bukkit.ironore;

import java.util.Random;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author TheJeterLP
 */
public class BlockListener implements Listener {

    private final Random r = new Random();

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;

        int m = 1;
        Material drop;

        if (e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.GOLD_ORE) {
            if (e.getPlayer().getItemInHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
                int level = e.getPlayer().getItemInHand().getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS);
                int plus = r.nextInt(level);
                m += plus;
            }

            drop = e.getBlock().getType() == Material.IRON_ORE ? Material.IRON_INGOT : Material.GOLD_INGOT;

            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);
            ItemStack stack = new ItemStack(drop, m);
            e.getPlayer().getInventory().addItem(stack);
        } else if (isOreBlock(e.getBlock())) {
            m = Math.max(1, r.nextInt(7));
            drop = e.getBlock().getType();

            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);
            ItemStack stack = new ItemStack(drop, m);
            e.getPlayer().getInventory().addItem(stack);
        }

    }

    private boolean isOreBlock(Block b) {
        return b.getType() == Material.IRON_BLOCK || b.getType() == Material.GOLD_BLOCK || b.getType() == Material.REDSTONE_BLOCK || b.getType() == Material.LAPIS_BLOCK || b.getType() == Material.DIAMOND_BLOCK || b.getType() == Material.EMERALD_BLOCK;
    }

}

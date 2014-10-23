package de.TheJeterLP.Bukkit.SurvivalGames.events;

import java.util.HashSet;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.Game;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.GameManager;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.Game.GameMode;
import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageManager;
import de.TheJeterLP.Bukkit.SurvivalGames.util.MessageManager.PrefixType;
import de.TheJeterLP.Bukkit.SurvivalGames.util.ItemGenerator;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;

public class ChestListener extends VClistener {

    private final Random rand = new Random();
    private final String chestName = "Survival-Games chest";

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void ChestListener(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clicked = e.getClickedBlock();
            int gameid = GameManager.getInstance().getPlayerGameId(e.getPlayer());
            if (clicked.getType() == Material.NOTE_BLOCK && gameid != -1) {
                Game game = GameManager.getInstance().getGame(gameid);
                if (game.getMode() == GameMode.INGAME) {
                    HashSet<Block> openedChest = GameManager.getOpenedChest().get(gameid);
                    openedChest = (openedChest == null) ? new HashSet<Block>() : openedChest;
                    if (!openedChest.contains(e.getClickedBlock())) {
                        Inventory inv = Bukkit.createInventory(null, 27, chestName);
                        int level = ItemGenerator.getInstance().getLevel();
                        inv.setContents(new ItemStack[inv.getContents().length]);
                        for (ItemStack i : ItemGenerator.getInstance().getItems(level)) {
                            int l = rand.nextInt(inv.getSize());
                            while (inv.getItem(l) != null) {
                                l = rand.nextInt(inv.getSize());
                            }
                            inv.setItem(l, i);
                        }
                        game.getChestStorage().addInventory(clicked, inv);
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CHEST_OPEN, 1, 1);
                        e.getPlayer().openInventory(game.getChestStorage().getInventory(clicked));
                        openedChest.add(e.getClickedBlock());
                        GameManager.getOpenedChest().put(gameid, openedChest);
                    } else {
                        playSound(e.getPlayer().getLocation(), false);
                        e.getPlayer().openInventory(game.getChestStorage().getInventory(clicked));
                        e.setCancelled(true);
                    }
                    e.setCancelled(true);
                } else {
                    e.setCancelled(true);
                }
            } else if (gameid != -1 && (clicked.getState() instanceof Chest || clicked.getState() instanceof DoubleChest)) {
                MessageManager.getInstance().sendMessage(PrefixType.INFO, "VirusCraft uses Noteblocks as chests to prevent using ChestFinder", e.getPlayer());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClose(final InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();
        int gameid = GameManager.getInstance().getPlayerGameId(p);
        if (gameid != -1) {
            Game game = GameManager.getInstance().getGame(gameid);
            if (game.getMode() == GameMode.INGAME) {
                if (e.getInventory().getTitle().equals(chestName)) {
                    playSound(p.getLocation(), true);
                }
            }
        }
    }

    private void playSound(Location l, boolean close) {
        Sound s;
        if (close) s = Sound.CHEST_CLOSE;
        else s = Sound.CHEST_OPEN;
        l.getWorld().playSound(l, s, 1, 1);
    }
}

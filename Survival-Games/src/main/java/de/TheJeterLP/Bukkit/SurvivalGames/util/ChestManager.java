package de.TheJeterLP.Bukkit.SurvivalGames.util;

import de.TheJeterLP.Bukkit.SurvivalGames.arena.Arena;
import de.TheJeterLP.Bukkit.SurvivalGames.arena.Game;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

/**
 * @author TheJeterLP
 */
public class ChestManager {

    private final HashMap<Location, Inventory> storage = new HashMap<>();
    private final ArrayList<Location> blocks = new ArrayList<>();
    private final Game g;

    public ChestManager(Game g) {
        this.g = g;
    }

    public Inventory getInventory(Block b) {
        return storage.get(b.getLocation());
    }

    public void addInventory(Block b, Inventory inv) {
        if (!storage.containsKey(b.getLocation())) storage.put(b.getLocation(), inv);
    }

    public void removeInventory(Block b) {
        if (storage.containsKey(b.getLocation())) storage.remove(b.getLocation());
    }

    public void load() {
        Arena a = g.getArena();

        ArrayList<Location> temp = new ArrayList<>();

        for (int i = 0; i < 375; i++) {
            Location random = a.getRandomLocation();

            random.getBlock().setType(Material.NOTE_BLOCK);
            temp.add(random);
        }

        blocks.clear();
        blocks.addAll(temp);

    }

    public void unload() {
        for (Location loc : blocks) {
            loc.getBlock().setType(Material.AIR);
        }
        blocks.clear();
    }

}

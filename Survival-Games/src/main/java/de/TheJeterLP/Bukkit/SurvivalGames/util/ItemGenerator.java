package de.TheJeterLP.Bukkit.SurvivalGames.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class ItemGenerator {

    private final HashMap<Integer, ArrayList<ItemStack>> lvlstore = new HashMap<>();
    private static final ItemGenerator instance = new ItemGenerator();

    public static ItemGenerator getInstance() {
        return instance;
    }

    public void load() {
        //level 1
        ItemStack l1s1 = new ItemStack(Material.WOOD_SWORD, 1);
        ItemStack l1s2 = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemStack l1s3 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        ItemStack l1s4 = new ItemStack(Material.LEATHER_BOOTS, 1);
        ItemStack l1s5 = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack l1s6 = new ItemStack(Material.RAW_BEEF, 3);
        ItemStack l1s7 = new ItemStack(Material.APPLE, 2);
        ArrayList<ItemStack> l1items = new ArrayList<>();
        l1items.add(l1s1);
        l1items.add(l1s2);
        l1items.add(l1s3);
        l1items.add(l1s4);
        l1items.add(l1s5);
        l1items.add(l1s6);
        l1items.add(l1s7);
        lvlstore.put(1, l1items);

        //level 2
        ItemStack l2s1 = new ItemStack(Material.STONE_SWORD, 1);
        ItemStack l2s2 = new ItemStack(Material.GOLD_HELMET, 1);
        ItemStack l2s3 = new ItemStack(Material.GOLD_CHESTPLATE, 1);
        ItemStack l2s4 = new ItemStack(Material.GOLD_BOOTS, 1);
        ItemStack l2s5 = new ItemStack(Material.GOLD_LEGGINGS, 1);
        ItemStack l2s6 = new ItemStack(Material.COAL, 1);
        ItemStack l2s7 = new ItemStack(Material.STICK, 1);
        ArrayList<ItemStack> l2items = new ArrayList<>();
        l2items.add(l2s1);
        l2items.add(l2s2);
        l2items.add(l2s3);
        l2items.add(l2s4);
        l2items.add(l2s5);
        l2items.add(l2s6);
        l2items.add(l2s7);
        lvlstore.put(2, l2items);

        //level 3
        ItemStack l3s1 = new ItemStack(Material.IRON_SWORD, 1);
        ItemStack l3s2 = new ItemStack(Material.IRON_HELMET, 1);
        ItemStack l3s3 = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemStack l3s4 = new ItemStack(Material.IRON_BOOTS, 1);
        ItemStack l3s5 = new ItemStack(Material.IRON_LEGGINGS, 1);
        ItemStack l3s6 = new ItemStack(Material.COOKED_BEEF, 1);
        ArrayList<ItemStack> l3items = new ArrayList<>();
        l3items.add(l3s1);
        l3items.add(l3s2);
        l3items.add(l3s3);
        l3items.add(l3s4);
        l3items.add(l3s5);
        l3items.add(l3s6);
        lvlstore.put(3, l3items);

        //level 4
        ItemStack l4s1 = new ItemStack(Material.DIAMOND, 1);
        ItemStack l4s2 = new ItemStack(Material.GOLDEN_APPLE, 1);
        ArrayList<ItemStack> l4items = new ArrayList<>();
        l4items.add(l4s1);
        l4items.add(l4s2);
        lvlstore.put(4, l4items);
    }

    public int getLevel() {
        int level;
        Random rand = new Random();
        
        int randomNumber = rand.nextInt(4000);
        if (randomNumber < 250) {
            level = 4;
        } else if (randomNumber > 250 && randomNumber < 1500) {
            level = 3;
        } else if (randomNumber > 1500 && randomNumber < 3000) {
            level = 1;
        } else {
            level = 2;
        }

        return level;
    }

    public ArrayList<ItemStack> getItems(int level) {
        Random r = new Random();
        ArrayList<ItemStack> items = new ArrayList<>();
        int length = Math.max(r.nextInt(lvlstore.get(level).size()), 1);
        List<ItemStack> litems = lvlstore.get(level);
        for (int i = 0; i < length; i++) {
            ItemStack random;
            try {
                random = litems.get(r.nextInt(litems.size()));
            } catch (Exception e) {
                random = litems.get(0);
                e.printStackTrace();
            }
            items.add(random);
        }
        return items;
    }
}

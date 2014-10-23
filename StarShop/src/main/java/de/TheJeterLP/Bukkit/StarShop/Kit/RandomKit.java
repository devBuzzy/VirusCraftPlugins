package de.TheJeterLP.Bukkit.StarShop.Kit;

import de.TheJeterLP.Bukkit.StarShop.Starshop;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author TheJeterLP
 */
public class RandomKit implements NormalKit {

    private final Random r = new Random();
    private final List<Material> possible;
    private final int menge, row;

    public RandomKit() {
        possible = new ArrayList<>();
        for (String s : Starshop.getInstance().getRandomConfig().getStringList("items")) {
            Material mat = Material.valueOf(s);
            possible.add(mat);
        }
        menge = Starshop.getInstance().getRandomConfig().getInt("maxamount");
        row = Starshop.getInstance().getRandomConfig().getInt("selectedItems");
    }

    private List<ItemStack> generate() {
        List<ItemStack> ret = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            Material random = possible.get(r.nextInt(possible.size()));
            ret.add(new ItemStack(random, r.nextInt(menge)));
        }
        return ret;
    }

    @Override
    public void equip(final Player p) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Starshop.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (ItemStack stack : generate()) {
                    p.getInventory().addItem(stack);
                }
                p.updateInventory();
            }
        });

    }

    @Override
    public String getName() {
        return "random";
    }

    @Override
    public int getID() {
        return 9999999;
    }

}

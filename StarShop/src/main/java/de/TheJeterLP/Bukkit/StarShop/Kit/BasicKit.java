package de.TheJeterLP.Bukkit.StarShop.Kit;

import de.TheJeterLP.Bukkit.StarShop.Starshop;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author TheJeterLP
 */
public class BasicKit implements NormalKit {

    private final String name;
    private final int id;
    private final List<ItemStack> armors = new ArrayList<>();
    private final List<ItemStack> items = new ArrayList<>();
    private final File kit_folder = new File(Starshop.getInstance().getDataFolder(), "Kits");
    private final YamlConfiguration cfg;

    public BasicKit(File file) {
        this.id = Integer.valueOf(file.getName().split("\\.")[0]);
        this.kit_folder.mkdirs();
        this.cfg = YamlConfiguration.loadConfiguration(new File(this.kit_folder, file.getName()));
        this.name = cfg.getString("name");
        for (String s : cfg.getStringList("items")) {
            String[] strings = s.split(";");
            Material m = Material.getMaterial(Integer.valueOf(strings[0]));
            items.add(new ItemStack(m, Integer.valueOf(strings[1])));
        }

        for (String s : cfg.getStringList("armor")) {
            String[] strings = s.split(";");
            Material m = Material.getMaterial(Integer.valueOf(strings[0]));
            armors.add(new ItemStack(m, Integer.valueOf(strings[1])));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void equip(final Player p) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Starshop.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (ItemStack s : items) {
                    p.getInventory().addItem(s);
                }
                for (ItemStack a : armors) {
                    if (a.getType() == Material.LEATHER_BOOTS || a.getType() == Material.GOLD_BOOTS || a.getType() == Material.CHAINMAIL_BOOTS || a.getType() == Material.DIAMOND_BOOTS || a.getType() == Material.IRON_BOOTS) {
                        p.getInventory().setBoots(a);
                    } else if (a.getType() == Material.LEATHER_LEGGINGS || a.getType() == Material.GOLD_LEGGINGS || a.getType() == Material.CHAINMAIL_LEGGINGS || a.getType() == Material.DIAMOND_LEGGINGS || a.getType() == Material.IRON_LEGGINGS) {
                        p.getInventory().setLeggings(a);
                    } else if (a.getType() == Material.LEATHER_CHESTPLATE || a.getType() == Material.GOLD_CHESTPLATE || a.getType() == Material.CHAINMAIL_CHESTPLATE || a.getType() == Material.DIAMOND_CHESTPLATE || a.getType() == Material.IRON_CHESTPLATE) {
                        p.getInventory().setChestplate(a);
                    } else if (a.getType() == Material.LEATHER_HELMET || a.getType() == Material.GOLD_HELMET || a.getType() == Material.CHAINMAIL_HELMET || a.getType() == Material.DIAMOND_HELMET || a.getType() == Material.IRON_HELMET) {
                        p.getInventory().setHelmet(a);
                    }
                }
                p.updateInventory();
            }
        });

    }

    @Override
    public int getID() {
        return this.id;
    }

}

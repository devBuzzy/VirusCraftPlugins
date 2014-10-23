package de.TheJeterLP.Bukkit.VirusCraftTools.Chest;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VirtualChestManager {

    private final File dataFolder = new File(VCplugin.inst().getDataFolder(), "Chests");
    private final HashMap<UUID, Inventory> chests = new HashMap<>();
      
    public void load() {
        this.dataFolder.mkdirs();
        for (File chestFile : this.dataFolder.listFiles()) {
            if (!chestFile.getName().endsWith(".chest.yml")) continue;
            String chestFileName = chestFile.getName();
            try {
                String uuid = chestFileName.replace(".chest.yml", "");
                this.chests.put(UUID.fromString(uuid), loadFromYaml(chestFile));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int save() {
        int savedChests = 0;
        this.dataFolder.mkdirs();
        Iterator<Map.Entry<UUID, Inventory>> chestIterator = this.chests.entrySet().iterator();
        while (chestIterator.hasNext()) {
            Map.Entry<UUID, Inventory> entry = (Map.Entry) chestIterator.next();
            UUID uuid = entry.getKey();
            Inventory chest = (Inventory) entry.getValue();

            File chestFile = new File(this.dataFolder, uuid.toString() + ".chest.yml");
            if (chest == null) {
                chestFile.delete();
                chestIterator.remove();
            } else {
                try {
                    saveToYaml(chest, chestFile);
                    savedChests++;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }               
            }
        }
        return savedChests;
    }

    public Inventory getChest(Player p) {
        Inventory chest = (Inventory) this.chests.get(p.getUniqueId());
        if (chest == null) {
            chest = Bukkit.getServer().createInventory(null, 54, ChatColor.RED + p.getName() + "'s" + ChatColor.GOLD + " chest.");
            this.chests.put(p.getUniqueId(), chest);
            return chest;
        }
        return chest;
    }

    public void removeChest(Player p) {
        this.chests.put(p.getUniqueId(), null);
    }

    public int getChestCount() {
        return this.chests.size();
    }
    
    private Inventory loadFromYaml(File file) throws IOException, InvalidConfigurationException {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        Inventory inventory = Bukkit.getServer().createInventory(null, 54, yaml.getString("chestName"));
        ConfigurationSection items = yaml.getConfigurationSection("items");
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            String slotString = String.valueOf(slot);
            if (items.isItemStack(slotString)) {
                ItemStack itemStack = items.getItemStack(slotString);
                inventory.setItem(slot, itemStack);
            }
        }
        return inventory;
    }

    private void saveToYaml(Inventory inventory, File file) throws IOException {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        int inventorySize = inventory.getSize();
        yaml.set("chestName", inventory.getTitle());
        ConfigurationSection items = yaml.createSection("items");
        for (int slot = 0; slot < inventorySize; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack != null) {
                items.set(String.valueOf(slot), stack);
            }
        }
        yaml.save(file);
    }
}

package de.TheJeterLP.Bukkit.StarShop.Kit;

import de.TheJeterLP.Bukkit.StarShop.Starshop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class UserData {

    private final File user_folder = new File(Starshop.getInstance().getDataFolder(), "user_data");
    private final File user_file;
    private final List<NormalKit> kits = new ArrayList<>();
    private NormalKit selected = null;

    public UserData(Player player) {
        this.user_file = new File(this.user_folder, player.getUniqueId().toString() + ".yml");
        if (user_file.exists()) {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(this.user_file);
            for (String kitName : cfg.getStringList("access")) {
                NormalKit kit = Starshop.getInstance().getKitManager().getKit(kitName);
                kits.add(kit);
            }
        }

    }

    public UserData(String uuid) {
        this.user_file = new File(this.user_folder, uuid + ".yml");
        if (user_file.exists()) {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(this.user_file);
            for (String kitName : cfg.getStringList("access")) {
                NormalKit kit = Starshop.getInstance().getKitManager().getKit(kitName);
                kits.add(kit);
            }
        }
    }

    public boolean hasAccessToKit(NormalKit kit) {
        if (kit instanceof RandomKit) return false;
        return kits.contains(kit);
    }

    public void addAccess(NormalKit kit) {
        if (kit instanceof RandomKit) return;
        if (!kits.contains(kit)) {
            kits.add(kit);
        }
    }

    public void selectKit(NormalKit kit) {
        this.selected = kit;
    }

    public NormalKit getSelectedKit() {
        return this.selected;
    }

    public void deselectKit() {
        this.selected = null;
    }

    public boolean save() throws IOException {
        if (!getAccessToKits().isEmpty()) {
            if (!user_file.exists()) {
                user_file.createNewFile();
            }
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(user_file);
            List<String> kitnames = new ArrayList<>();
            for (NormalKit kit : kits) {
                kitnames.add(kit.getName());
            }
            cfg.set("access", kitnames);
            return true;
        }
        return false;
    }

    public List<NormalKit> getAccessToKits() {
        return kits;
    }
}

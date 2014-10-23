package de.TheJeterLP.Bukkit.StarShop.Kit;

import de.TheJeterLP.Bukkit.StarShop.Starshop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class KitManager {

    private final HashMap<String, NormalKit> kits = new HashMap<>();
    private final HashMap<String, UserData> datas = new HashMap<>();

    private File kit_folder, user_folder;

    public void setUp() {
        kit_folder = new File(Starshop.getInstance().getDataFolder(), "Kits");
        kit_folder.mkdirs();
        user_folder = new File(Starshop.getInstance().getDataFolder(), "user_data");
        user_folder.mkdirs();
        File[] files = kit_folder.listFiles();
        for (File f : files) {
            NormalKit kit = new BasicKit(f);
            kits.put(kit.getName(), kit);
        }

        File[] userdatas = user_folder.listFiles();
        for (File f : userdatas) {
            String uuid = f.getName().split("\\.")[0];
            UserData userdata = new UserData(uuid);
            datas.put(uuid, userdata);
        }
    }

    public NormalKit getKit(int id) {
        if (id == new RandomKit().getID()) {
            return new RandomKit();
        }

        for (NormalKit kit : kits.values()) {
            if (kit.getID() == id) {
                return kit;
            }
        }
        return null;
    }

    public NormalKit getKit(String name) {
        if (name.equalsIgnoreCase(new RandomKit().getName())) {
            return new RandomKit();
        }
        return kits.get(name);
    }

    public boolean kitExists(String name) {
        return getKit(name) != null;
    }

    public UserData getUserData(Player p) {
        return this.datas.get(p.getUniqueId().toString());
    }

    public void selectKit(Player p, NormalKit k) {
        if (getUserData(p) == null) {
            UserData data = new UserData(p);
            datas.put(p.getUniqueId().toString(), data);
        }
        getUserData(p).selectKit(k);
    }

    public NormalKit getSelectedKit(Player p) {
        if (getUserData(p) == null) {
            UserData data = new UserData(p);
            datas.put(p.getUniqueId().toString(), data);
        }
        return getUserData(p).getSelectedKit();
    }

    public void deselectKit(Player p) {
        if (getUserData(p) == null) {
            UserData data = new UserData(p);
            datas.put(p.getName(), data);
        }
        getUserData(p).deselectKit();
    }

    public void save() {
        int saved = 0;
        for (UserData data : this.datas.values()) {
            try {
                if (data.save()) {
                    saved++;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Starshop.getInstance().getLogger().info("Saved " + saved + " UserDatas.");
    }

    public void createdata(Player p) {
        UserData data = new UserData(p);
        datas.put(p.getUniqueId().toString(), data);
    }

    public List<NormalKit> getKits(Player player) {
        if (getUserData(player) == null) {
            return new ArrayList<>();
        }

        return getUserData(player).getAccessToKits();
    }

    public void addKit(NormalKit k, Player player) {
        if (getUserData(player) == null) {
            createdata(player);
        }
        UserData data = getUserData(player);
        data.addAccess(k);
    }
}

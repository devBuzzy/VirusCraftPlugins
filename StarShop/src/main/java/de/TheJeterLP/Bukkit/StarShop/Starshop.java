package de.TheJeterLP.Bukkit.StarShop;

import de.TheJeterLP.Bukkit.StarShop.Commands.PromoteWithStars;
import de.TheJeterLP.Bukkit.StarShop.Commands.Setstars;
import de.TheJeterLP.Bukkit.StarShop.Commands.Stars;
import de.TheJeterLP.Bukkit.StarShop.Commands.addStars;
import de.TheJeterLP.Bukkit.StarShop.Commands.removeStars;
import de.TheJeterLP.Bukkit.StarShop.Database.MySQL;
import de.TheJeterLP.Bukkit.StarShop.Kit.KitListener;
import de.TheJeterLP.Bukkit.StarShop.Kit.KitManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Addon.Addon;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Manager;
import java.io.File;
import java.sql.SQLException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author TheJeterLP
 */
public class Starshop extends Addon {

    private static Starshop INSTANCE;

    public static Starshop getInstance() {
        return INSTANCE;
    }

    protected final KitManager kmanager = new KitManager();
    private YamlConfiguration randomConfig;

    @Override
    public void onEnable() {
        INSTANCE = this;
        loadRandomConfig();
        try {
            MySQL.init();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        kmanager.setUp();
        Manager.registerListener(PlayerListener.class);
        Manager.registerListener(KitListener.class);
        Manager.registerCommand(PromoteWithStars.class, this);
        Manager.registerCommand(Setstars.class, this);
        Manager.registerCommand(Stars.class, this);
        Manager.registerCommand(addStars.class, this);
        Manager.registerCommand(removeStars.class, this);
    }

    @Override
    public void onDisable() {
        kmanager.save();
    }

    public YamlConfiguration getRandomConfig() {
        return randomConfig;
    }

    public KitManager getKitManager() {
        return this.kmanager;
    }

    private void loadRandomConfig() {
        File rcfg = new File(getDataFolder(), "random.yml");
        if (!rcfg.exists()) saveResource("random.yml", false);
        this.randomConfig = YamlConfiguration.loadConfiguration(rcfg);
    }

}

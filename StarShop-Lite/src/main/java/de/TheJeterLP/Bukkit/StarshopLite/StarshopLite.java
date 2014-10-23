package de.TheJeterLP.Bukkit.StarshopLite;

import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TheJeterLP
 */
public class StarshopLite extends JavaPlugin {

    private static StarshopLite INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Config.load();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        try {
            Database.init();
        } catch (SQLException ex) {
            Bukkit.getPluginManager().disablePlugin(this);
            ex.printStackTrace();
        }
    }

    public static StarshopLite getInstance() {
        return INSTANCE;
    }

}

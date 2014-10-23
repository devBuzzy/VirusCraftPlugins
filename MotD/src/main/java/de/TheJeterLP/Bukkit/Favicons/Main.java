package de.TheJeterLP.Bukkit.Favicons;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author TheJeterLP
 */
public class Main extends Plugin {

    private static Main INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        ProxyServer.getInstance().getPluginManager().registerListener(this, new Events());
    }

    public static Main getInstance() {
        return INSTANCE;
    }

}

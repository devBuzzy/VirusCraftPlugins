package de.theJeterLP.BungeeCord.BungeeTools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.protocol.Protocol;

/**
 * @author TheJeterLP
 */
public class Main extends Plugin {

    private static Main INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Config.load();
        ProxyServer.getInstance().getPluginManager().registerCommand(null, new StopCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(null, new SayCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReloadCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PingEvent());
        try {
            reload();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public static void reload() throws Exception {
        Config.reload(true);
        List<Integer> version = (ArrayList<Integer>) Config.VERSION.getObject();

        INSTANCE.getLogger().info("Loading support for versions: ");
        INSTANCE.getLogger().info(Arrays.toString(version.toArray()));

        INSTANCE.getLogger().info("Actual versions: ");
        INSTANCE.getLogger().info(Arrays.toString(Protocol.supportedVersions.toArray()));

        Field f = Protocol.class.getField("supportedVersions");
        f.setAccessible(true);
        f.set(null, version);

        INSTANCE.getLogger().info("Loaded versions: ");
        INSTANCE.getLogger().info(Arrays.toString(Protocol.supportedVersions.toArray()));
    }

}

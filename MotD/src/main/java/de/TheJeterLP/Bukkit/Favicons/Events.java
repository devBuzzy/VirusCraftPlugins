package de.TheJeterLP.Bukkit.Favicons;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author TheJeterLP
 */
public class Events implements Listener {

    private final File faviconFolder = Main.getInstance().getDataFolder();
    private final Random r = new Random();

    public Events() {
        faviconFolder.mkdirs();
    }

    @EventHandler(priority = 1)
    public void onPing(final ProxyPingEvent e) throws IOException {
        List<File> list = new ArrayList<File>();
        for (File f : faviconFolder.listFiles()) {
            if (!f.isFile() || !f.getName().toLowerCase().endsWith(".png")) continue;
            list.add(f);
        }

        if (!list.isEmpty()) {
            File random = null;
            if (list.size() == 1) {
                random = list.get(0);
            } else {
                while (random == null) {
                    try {
                        random = list.get(r.nextInt(list.size() - 1));
                    } catch (Exception ex) {
                    }
                }

            }
            Favicon ico = Favicon.create(ImageIO.read(random));
            ServerPing ping = e.getResponse();
            ping.setFavicon(ico);
            e.setResponse(ping);
        }
    }
}

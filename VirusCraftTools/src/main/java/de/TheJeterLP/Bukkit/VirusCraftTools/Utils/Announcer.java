package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import java.util.Random;
import org.bukkit.Bukkit;

/**
 * @author TheJeterLP
 */
public class Announcer implements Runnable {

    private final Random r = new Random();
    private final String pre = "§6§l[]=====§4§lVirus§5§lCraft§1§lNetwork§6§l=====[]";

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().length == 0 && !Config.ANNOUNCER_NOPLAYER.getBoolean()) return;
        String msg = Config.ANNOUNCER_MESSAGES.getStringList().get(r.nextInt(Config.ANNOUNCER_MESSAGES.getStringList().size()));
        Bukkit.broadcastMessage(pre);
        Bukkit.broadcastMessage(msg);
        Bukkit.broadcastMessage(pre);
    }

}

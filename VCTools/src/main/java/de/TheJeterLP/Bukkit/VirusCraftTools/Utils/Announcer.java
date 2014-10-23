package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import java.util.Random;
import org.bukkit.Bukkit;

/**
 * @author TheJeterLP
 */
public class Announcer implements Runnable {

    private final Random r = new Random();
    private final String pre = Config.ANNOUNCER_PREFIX.getString();

    @Override
    public void run() {
        if ((Bukkit.getOnlinePlayers().length == 0 && !Config.ANNOUNCER_NOPLAYER.getBoolean()) || Config.ANNOUNCER_MESSAGES.getStringList().isEmpty()) return;
        final String msg = Config.ANNOUNCER_MESSAGES.getStringList().get(r.nextInt(Config.ANNOUNCER_MESSAGES.getStringList().size() - 1));

        Bukkit.broadcastMessage(pre);
        Bukkit.broadcastMessage(msg);
        Bukkit.broadcastMessage(pre);
    }

}

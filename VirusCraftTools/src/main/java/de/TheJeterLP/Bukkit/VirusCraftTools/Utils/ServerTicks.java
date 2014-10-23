package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

/**
 * @author TheJeterLP
 */
public class ServerTicks {

    private final int ticks;

    public ServerTicks(int seconds) {
        this.ticks = seconds * 20;
    }

    public int getTicks() {
        return ticks;
    }
}

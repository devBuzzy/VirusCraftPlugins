package de.TheJeterLP.Bukkit.VirusCraftTools.Bar;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Config;

/**
 * @author TheJeterLP
 */
public class Runner implements Runnable {

    private String currMessage;
    private int curr;
    private final OtherRunner running;

    public Runner(OtherRunner ot) {
        curr = 0;
        running = ot;
        currMessage = Config.BAR_MESSAGES.getStringList().get(curr);
    }

    public String getCurrentMessage() {
        return currMessage;
    }

    @Override
    public void run() {
        if (Config.BAR_MESSAGES.getStringList().isEmpty() || Config.BAR_MESSAGES.getStringList().size() == 1) {
            return;
        }
        curr++;
        if (curr > Config.BAR_MESSAGES.getStringList().size() - 1) curr = 0;
        currMessage = Config.BAR_MESSAGES.getStringList().get(curr);
        BarTool.setMessage(currMessage, running.getHealth());
    }

}

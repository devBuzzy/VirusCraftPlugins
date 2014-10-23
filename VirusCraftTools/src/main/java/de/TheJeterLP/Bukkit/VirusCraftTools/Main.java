package de.TheJeterLP.Bukkit.VirusCraftTools;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClogger;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;

/**
 * @author JeterLP
 */
public class Main extends VCplugin {

    @Override
    public void onStart() {
        VClogger.log(MessageType.INFO, "The plugin is now enabled!");
    }

    @Override
    public void onStop() {
        VClogger.log(MessageType.INFO, "The plugin is now disabled!");
    }
}

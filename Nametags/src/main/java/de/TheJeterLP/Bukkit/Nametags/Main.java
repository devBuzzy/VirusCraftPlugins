package de.TheJeterLP.Bukkit.Nametags;

import de.TheJeterLP.Bukkit.Nametags.Utils.ConfigManager;
import de.TheJeterLP.Bukkit.Nametags.Utils.NametagManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Addon.Addon;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Manager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;

public class Main extends Addon {

    private static Main INSTANCE = null;
    protected static Chat CHAT = null;

    @Override
    public void onEnable() {
        INSTANCE = this;
        CHAT = VCplugin.inst().getChat();
        ConfigManager.init();
        NametagManager.load();
        Manager.registerListener(NametagEventHandler.class, this);
        Manager.registerCommand(CommandHandler.class, this);
        load();
    }

    @Override
    public void onDisable() {
        ConfigManager.save();
        NametagManager.reset();
    }

    protected void load() {
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player p : getServer().getOnlinePlayers()) {
                    NametagManager.clear(p.getName());
                    setNametag(p);
                }
            }
        }, 2);
    }

    public static void setNametag(Player player) {
        String prefix = ConfigManager.getPrefix(player);
        if (prefix != null) {
            if (prefix.length() > 16) {
                prefix = prefix.substring(0, 16);
            }
            prefix = replaceColors(prefix);
        }

        String suffix = ConfigManager.getSuffix(player);
        if (suffix != null) {
            if (suffix.length() > 16) {
                suffix = suffix.substring(0, 16);
            }
            suffix = replaceColors(suffix);
        }
        NametagManager.overlap(player, prefix, suffix);

        String str = "§f" + player.getDisplayName();
        if (str.length() > 16) {
            str = str.substring(0, 16);
        }
        player.setPlayerListName(str);
    }

    public static String replaceColors(String message) {
        return message.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public static Chat getChat() {
        return CHAT;
    }

}

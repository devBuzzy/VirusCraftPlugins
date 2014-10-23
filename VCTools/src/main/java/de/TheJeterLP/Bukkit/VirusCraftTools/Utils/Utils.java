package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import de.TheJeterLP.Bukkit.VirusCraftTools.Main;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author TheJeterLP
 */
public class Utils {

    private static final List<String> cwEnabled = new ArrayList<>();

    public static boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    public static Player getPlayer(CommandSender sender) {
        if (!isPlayer(sender)) return null;
        return (Player) sender;
    }

    public static void broadcastMessage(MessageType type, String message) {
        Bukkit.broadcastMessage(type.getMessage() + message);
    }

    public static void sendMessage(MessageType type, CommandSender sender, String message) {
        sender.sendMessage(type.getMessage() + message);
    }

    public static String formatWithPlayer(String string, Player p) {
        String ret = Config.PLAYER_FORMAT.getString();
        ret = ret.replace("%player", p.getName());
        ret = ret.replace("%prefix", Main.inst().getChat().getPlayerPrefix(p));
        ret = ret.replace("%suffix", Main.inst().getChat().getPlayerSuffix(p));
        string = string.replaceAll("%player%", ret);
        string = replaceColors(string);
        return string;
    }

    public static String replaceColors(String string) {
        return string.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
    }

    public static String removeColors(String string) {
        return string.replaceAll("§((?i)[0-9a-fk-or])", "");
    }

    public static boolean hasCWEnabled(Player p) {
        return cwEnabled.contains(p.getName());
    }

    public static void enableCW(Player p) {
        if (cwEnabled.contains(p.getName())) return;
        cwEnabled.add(p.getName());
    }

    public static void disableCW(Player p) {
        if (!cwEnabled.contains(p.getName())) return;
        cwEnabled.remove(p.getName());
    }

    public static boolean isInteger(String s) {
        try {
            Integer.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int getInteger(String s) {
        return Integer.valueOf(s);
    }

    public static void respawn(final Player p) {
        VClogger.log(MessageType.DEBUG, "Performing respawn for Player " + p.getName());
        if (isSevenPlus()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCplugin.inst(), new Runnable() {
                @Override
                public void run() {
                    try {
                        Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
                        Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".PacketPlayInClientCommand").newInstance();
                        Class<?> enumClass = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EnumClientCommand");

                        for (Object o : enumClass.getEnumConstants()) {
                            if (o.toString().equals("PERFORM_RESPAWN")) {
                                packet = packet.getClass().getConstructor(enumClass).newInstance(o);
                                break;
                            }
                        }

                        Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                        con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
                        VClogger.log(MessageType.DEBUG, "Respawn for Player " + p.getName() + " is done!");
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            });
        }
    }

    public static void launchPlayer(Player p) {
        Vector dir = p.getLocation().getDirection().multiply(4);
        p.setVelocity(dir.setY(1));
        p.setFallDistance(-1000.0F);
        p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 3);
    }

    public static String getMinecraftVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static boolean isSevenPlus() {
        String mcver = Utils.getMinecraftVersion().replace("v", "");
        boolean seven = (Integer.valueOf(mcver.split("_")[1]) >= 7);
        return seven;
    }

}

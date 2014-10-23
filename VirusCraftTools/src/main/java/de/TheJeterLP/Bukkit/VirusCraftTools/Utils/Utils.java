package de.TheJeterLP.Bukkit.VirusCraftTools.Utils;

import de.JeterLP.MakeYourOwnCommands.Command.CommandManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import java.lang.reflect.Field;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.util.Vector;

/**
 * @author TheJeterLP
 */
public class Utils {

    public static void playEffect(Location loc, Effect e) {
        loc.getWorld().playEffect(loc, e, 0);
    }

    public static void playSound(Location loc, Sound s) {
        loc.getWorld().playSound(loc, s, 1F, 1F);
    }
   
    public static void broadcastMessage(MessageType type, String message) {
        Bukkit.broadcastMessage(type.getMessage() + message);
    }

    public static CommandResult sendMessage(MessageType type, CommandSender sender, String message) {
        sender.sendMessage(type.getMessage() + message);
        return CommandResult.SUCCESS;
    }

    public static String formatWithPlayer(String string, Player p) {
        boolean chat = VCplugin.inst().getChat() != null;
        String ret = "%prefix%player%suffix";
        ret = ret.replace("%player", p.getDisplayName());
        String prefix = chat ? VCplugin.inst().getChat().getPlayerPrefix(p) : "";
        ret = ret.replace("%prefix", prefix);
        String suffix = chat ? VCplugin.inst().getChat().getPlayerSuffix(p) : "";
        ret = ret.replace("%suffix", suffix);
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

    public static ItemStack getSkull(final String skullOwner) {
        final ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        final SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner(skullOwner);
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public static SimpleCommandMap getCommandMap() {
        try {
            final Field f = SimplePluginManager.class.getDeclaredField("commandMap");
            f.setAccessible(true);
            return (SimpleCommandMap) f.get(VCplugin.inst().getServer().getPluginManager());
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isCmdRegistered(String name) {
        if (VCplugin.inst().getServer().getPluginManager().isPluginEnabled("MakeYourOwnCommands")) {
            if (CommandManager.isRegistered("/" + name)) return true;
        }
        return getCommandMap().getCommand(name) != null;
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
                        }
                    }
                    Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                    con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
    }

    public static void launchPlayer(Player p) {
        Vector dir = p.getLocation().getDirection().multiply(4);
        p.setVelocity(dir.setY(1));
        p.setFallDistance(-1000.0F);
        playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT);
        playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES);
    }

    public static String serialLocation(Location l) {
        int pitch = Integer.valueOf(String.valueOf(l.getPitch()).split("\\.")[0]);
        int yaw = Integer.valueOf(String.valueOf(l.getYaw()).split("\\.")[0]);
        return l.getX() + ";" + l.getY() + ";" + l.getZ() + ";" + l.getWorld().getName() + ";" + yaw + ";" + pitch;
    }

    public static Location deserialLocation(String s) {
        String[] a = s.split(";");
        World w = Bukkit.getWorld(a[3]);
        if (w == null) {
            w = Bukkit.getWorlds().get(0);
        }
        double x = Double.parseDouble(a[0]);
        double y = Double.parseDouble(a[1]);
        double z = Double.parseDouble(a[2]);
        int yaw = Integer.parseInt(a[4]);
        int pitch = Integer.parseInt(a[5]);
        Location l = new Location(w, x, y, z, yaw, pitch);
        return l;
    }

    public static Block getBlockLooking(Player player, int range) {
        Block b = player.getTargetBlock(null, range);
        return b;

    }

    public static Location getLocationLooking(Player player, int range) {
        Block b = getBlockLooking(player, range);
        return b.getLocation();
    }
   
    public static void clearInventory(Player p) {
        p.getInventory().clear();
        p.getInventory().setBoots(new ItemStack(Material.AIR));
        p.getInventory().setChestplate(new ItemStack(Material.AIR));
        p.getInventory().setHelmet(new ItemStack(Material.AIR));
        p.getInventory().setLeggings(new ItemStack(Material.AIR));
        p.updateInventory();
    }
  
    public static boolean isAtHub(final Player p) {
        return p.getWorld().getName().equalsIgnoreCase(Config.SETTINGS_MAIN_WORLD.getString());
    }
}

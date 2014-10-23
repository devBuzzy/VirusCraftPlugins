package de.TheJeterLP.Bukkit.VirusCraftTools.Bar;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Manager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Config;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import java.util.HashMap;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author TheJeterLP
 */
public class BarTool extends VClistener {

    private static final HashMap<String, FakeDragon> players = new HashMap<>();
    private static final HashMap<String, Integer> timers = new HashMap<>();
    private static Runner runn;
    private static OtherRunner run;

    public static void init() {
        Manager.registerListener(BarTool.class);
        run = new OtherRunner();
        if (!Config.BAR_MESSAGES.getStringList().isEmpty()) {
            runn = new Runner(run);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(VCplugin.inst(), runn, 100 * 20, 100 * 20);
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VCplugin.inst(), run, 0, 20);

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void PlayerLoggout(PlayerQuitEvent event) {
        quit(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        quit(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        handleTeleport(event.getPlayer(), event.getTo().clone());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerRespawnEvent event) {
        handleTeleport(event.getPlayer(), event.getRespawnLocation().clone());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        setMessage(e.getPlayer(), runn.getCurrentMessage(), run.getHealth());
    }

    private void handleTeleport(final Player player, final Location loc) {
        if (!hasBar(player))
            return;
        Bukkit.getScheduler().runTaskLater(VCplugin.inst(), new Runnable() {
            @Override
            public void run() {
                if (!hasBar(player))
                    return;

                FakeDragon oldDragon = getDragon(player, "");

                float health = oldDragon.health;
                String message = oldDragon.name;

                Util.sendPacket(player, getDragon(player, "").getDestroyPacket());

                players.remove(player.getName());

                FakeDragon dragon = addDragon(player, loc, message);
                dragon.health = health;

                sendDragon(dragon, player);
            }

        }, 2L);
    }

    private void quit(Player player) {
        removeBar(player);
    }

    public static void setMessage(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setMessage(player, message);
        }
    }

    public static void setMessage(Player player, String message) {
        FakeDragon dragon = getDragon(player, message);
        dragon.name = cleanMessage(message);
        dragon.health = FakeDragon.MAX_HEALTH;
        cancelTimer(player);
        sendDragon(dragon, player);
    }

    public static void setMessage(String message, float percent) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setMessage(player, message, percent);
        }
    }

    public static void setMessage(Player player, String message, float percent) {
        Validate.isTrue(0F <= percent && percent <= 100F, "Percent must be between 0F and 100F, but was: ", percent);
        FakeDragon dragon = getDragon(player, message);
        dragon.name = cleanMessage(message);
        dragon.health = (percent / 100f) * FakeDragon.MAX_HEALTH;
        cancelTimer(player);
        sendDragon(dragon, player);
    }

    public static void setMessage(String message, int seconds) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setMessage(player, message, seconds);
        }
    }

    public static void setMessage(final Player player, String message, int seconds) {
        Validate.isTrue(seconds >= 0, "Seconds must be above 1 but was: ", seconds);
        FakeDragon dragon = getDragon(player, message);
        dragon.name = cleanMessage(message);
        dragon.health = FakeDragon.MAX_HEALTH;
        final float dragonHealthMinus = FakeDragon.MAX_HEALTH / seconds;
        cancelTimer(player);

        timers.put(player.getName(), Bukkit.getScheduler().runTaskTimer(VCplugin.inst(), new BukkitRunnable() {
            @Override
            public void run() {
                FakeDragon drag = getDragon(player, "");
                drag.health -= dragonHealthMinus;
                if (drag.health <= 0) {
                    removeBar(player);
                    cancelTimer(player);
                } else {
                    sendDragon(drag, player);
                }
            }

        }, 20L, 20L).getTaskId());
        sendDragon(dragon, player);
    }

    public static boolean hasBar(Player player) {
        return players.get(player.getName()) != null;
    }

    public static void removeBar(Player player) {
        if (!hasBar(player))
            return;
        Util.sendPacket(player, getDragon(player, "").getDestroyPacket());
        players.remove(player.getName());
        cancelTimer(player);
    }

    public static void setHealth(Player player, float percent) {
        if (!hasBar(player))
            return;
        FakeDragon dragon = getDragon(player, "");
        dragon.health = (percent / 100f) * FakeDragon.MAX_HEALTH;
        cancelTimer(player);
        sendDragon(dragon, player);
    }

    public static void setHealth(float percent) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!hasBar(player))
                return;
            FakeDragon dragon = getDragon(player, "");
            dragon.health = (percent / 100f) * FakeDragon.MAX_HEALTH;
            cancelTimer(player);
            sendDragon(dragon, player);
        }
    }

    public static float getHealth(Player player) {
        if (!hasBar(player))
            return -1;
        return getDragon(player, "").health;
    }

    public static String getMessage(Player player) {
        if (!hasBar(player))
            return "";
        return getDragon(player, "").name;
    }

    private static String cleanMessage(String message) {
        if (message.length() > 64)
            message = message.substring(0, 63);
        return message;
    }

    private static void cancelTimer(Player player) {
        Integer timerID = timers.remove(player.getName());
        if (timerID != null) {
            Bukkit.getScheduler().cancelTask(timerID);
        }
    }

    private static void sendDragon(FakeDragon dragon, Player player) {
        Util.sendPacket(player, dragon.getMetaPacket(dragon.getWatcher()));
        Util.sendPacket(player, dragon.getTeleportPacket(player.getLocation().add(0, -300, 0)));
    }

    private static FakeDragon getDragon(Player player, String message) {
        if (hasBar(player)) {
            return players.get(player.getName());
        } else
            return addDragon(player, cleanMessage(message));
    }

    private static FakeDragon addDragon(Player player, String message) {
        FakeDragon dragon = Util.newDragon(message, player.getLocation().add(0, -300, 0));
        Util.sendPacket(player, dragon.getSpawnPacket());
        players.put(player.getName(), dragon);
        return dragon;
    }

    private static FakeDragon addDragon(Player player, Location loc, String message) {
        FakeDragon dragon = Util.newDragon(message, loc.add(0, -300, 0));
        Util.sendPacket(player, dragon.getSpawnPacket());
        players.put(player.getName(), dragon);
        return dragon;
    }

}

package de.TheJeterLP.Bukkit.VillagerHunt.Arena;

import de.TheJeterLP.Bukkit.VillagerHunt.Bukkit.VillagerHunt;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerData {

    private final Player player;
    private final ItemStack[] inventory, armor;
    private final int level;
    private final float xp, hunger;
    private final GameMode gm;
    private final double maxhealth, health;
    private final boolean flying;

    public PlayerData(Player p) {
        this.player = p;
        inventory = p.getInventory().getContents();
        armor = p.getInventory().getArmorContents();
        gm = player.getGameMode();
        xp = p.getExp();
        level = p.getLevel();
        hunger = p.getExhaustion();
        maxhealth = ((Damageable) p).getMaxHealth();
        health = ((Damageable) p).getHealth();
        flying = p.getAllowFlight();

        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setSleepingIgnored(true);
        player.setMaxHealth(20.0);
        player.setHealth(20.0);
        player.setExhaustion(20.0F);
        player.setExp(0F);
        player.setLevel(0);
        Utils.clearInventory(player);
        player.updateInventory();
    }

    protected void restorePlayerData() {
        if (player == null) return;
        player.setAllowFlight(flying);
        player.setMaxHealth(maxhealth);
        player.setExhaustion(hunger);
        player.setHealth(health);
        player.setGameMode(gm);
        player.setExp(xp);
        player.setLevel(level);
        player.setSleepingIgnored(false);
        player.getServer().getScheduler().scheduleSyncDelayedTask(VillagerHunt.getInstance(), new Runnable() {

            @Override
            public void run() {
                player.getInventory().setArmorContents(armor);
                player.getInventory().setContents(inventory);
            }
        }, 3);

    }

    public boolean isForPlayer(Player p) {
        return getPlayer().getName().equals(p.getName());
    }

    public Player getPlayer() {
        return player;
    }
}

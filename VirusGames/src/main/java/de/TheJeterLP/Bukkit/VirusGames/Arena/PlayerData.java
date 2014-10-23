package de.TheJeterLP.Bukkit.VirusGames.Arena;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusGames.Bukkit.VirusGames;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerData {

    private final Player player;
    private final ItemStack[] inventory, armor;
    private final int level;
    private final float xp, hunger;
    private final GameMode gm;
    private final double maxhealth, health;
    private final boolean flying;
    private Team team;
    private Class c;

    public PlayerData(Player p, Team t) {
        this.player = p;
        inventory = p.getInventory().getContents();
        armor = p.getInventory().getArmorContents();
        gm = player.getGameMode();
        xp = p.getExp();
        level = p.getLevel();
        hunger = p.getExhaustion();
        maxhealth = p.getMaxHealth();
        health = p.getHealth();
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
        this.team = Team.LOBBY;
        this.c = Class.NO_CLASS;
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
        player.getServer().getScheduler().scheduleSyncDelayedTask(VirusGames.getInstance(), new Runnable() {

            @Override
            public void run() {
                player.getInventory().setArmorContents(armor);
                player.getInventory().setContents(inventory);
            }
        }, 3);
    }

    public boolean isForPlayer(Player p) {
        return player.getUniqueId().equals(p.getUniqueId());
    }

    public String getName() {
        return player.getName();
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team t) {
        this.team = t;
    }

    public Player getPlayer() {
        return player;
    }

    public Class getPlayerClass() {
        return c;
    }

    public void setClass(Class classes) {
        c = classes;
        Utils.clearInventory(getPlayer());
        if (classes != Class.NO_CLASS) {
            for (ItemStack i : classes.getContent()) {
                getPlayer().getInventory().addItem(i);
            }
            for (ItemStack a : classes.getArmor()) {
                if (a == null) continue;
                if (a.getType() == Material.LEATHER_BOOTS || a.getType() == Material.GOLD_BOOTS || a.getType() == Material.CHAINMAIL_BOOTS || a.getType() == Material.DIAMOND_BOOTS || a.getType() == Material.IRON_BOOTS) {
                    getPlayer().getInventory().setBoots(a);
                } else if (a.getType() == Material.LEATHER_LEGGINGS || a.getType() == Material.GOLD_LEGGINGS || a.getType() == Material.CHAINMAIL_LEGGINGS || a.getType() == Material.DIAMOND_LEGGINGS || a.getType() == Material.IRON_LEGGINGS) {
                    getPlayer().getInventory().setLeggings(a);
                } else if (a.getType() == Material.LEATHER_CHESTPLATE || a.getType() == Material.GOLD_CHESTPLATE || a.getType() == Material.CHAINMAIL_CHESTPLATE || a.getType() == Material.DIAMOND_CHESTPLATE || a.getType() == Material.IRON_CHESTPLATE) {
                    getPlayer().getInventory().setChestplate(a);
                } else if (a.getType() == Material.LEATHER_HELMET || a.getType() == Material.GOLD_HELMET || a.getType() == Material.CHAINMAIL_HELMET || a.getType() == Material.DIAMOND_HELMET || a.getType() == Material.IRON_HELMET) {
                    getPlayer().getInventory().setHelmet(a);
                }
            }

            for (PotionEffect e : classes.getEffects()) {
                getPlayer().addPotionEffect(e);
            }
        }
    }

}

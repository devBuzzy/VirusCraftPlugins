package de.TheJeterLP.Bukkit.VirusGames.util;

import java.util.ArrayList;
import org.bukkit.Color;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Kits {

    private static final Kits instance = new Kits();
    private final ArrayList<ItemStack> tank = new ArrayList<>(),
            runner = new ArrayList<>(),
            sniper = new ArrayList<>(),
            medic = new ArrayList<>(),
            virus = new ArrayList<>(),
            super_virus = new ArrayList<>();

    private final ArrayList<ItemStack> tankarmor = new ArrayList<>(),
            runnerarmor = new ArrayList<>(),
            sniperarmor = new ArrayList<>(),
            medicarmor = new ArrayList<>(),
            virusarmor = new ArrayList<>(),
            super_virusarmor = new ArrayList<>();

    private final ArrayList<PotionEffect> tankeffect = new ArrayList<>(),
            runnereffect = new ArrayList<>(),
            snipereffect = new ArrayList<>(),
            mediceffect = new ArrayList<>(),
            viruseffect = new ArrayList<>(),
            super_viruseffect = new ArrayList<>();

    public enum Kit {

        TANK,
        RUNNER,
        SNIPER,
        MEDIC,
        VIRUS,
        SUPER_VIRUS;
    }

    public static Kits getInstance() {
        return instance;
    }

    public void setup() {
        //MEDIC
        medicarmor.add(getColoredArmor(Color.BLUE, Material.LEATHER_BOOTS));
        medicarmor.add(getColoredArmor(Color.BLUE, Material.LEATHER_LEGGINGS));
        medicarmor.add(getColoredArmor(Color.BLUE, Material.LEATHER_CHESTPLATE));
        medicarmor.add(getColoredArmor(Color.BLUE, Material.LEATHER_HELMET));
        medic.add(new ItemStack(Material.WOOD_SWORD));
        medic.add(new ItemStack(373, 64, (short) 16389));
        //RUNNER
        runnerarmor.add(null);
        runnerarmor.add(null);
        runnerarmor.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        runnerarmor.add(null);
        runner.add(new ItemStack(Material.GOLD_SWORD));
        runnereffect.add(new PotionEffect(PotionEffectType.SPEED, 999999999, 1));
        //SNIPER
        sniperarmor.add(getColoredArmor(Color.RED, Material.LEATHER_BOOTS));
        sniperarmor.add(getColoredArmor(Color.RED, Material.LEATHER_LEGGINGS));
        sniperarmor.add(getColoredArmor(Color.RED, Material.LEATHER_CHESTPLATE));
        sniperarmor.add(getColoredArmor(Color.RED, Material.LEATHER_HELMET));
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
        sniper.add(bow);
        sniper.add(new ItemStack(Material.ARROW));
        //TANK
        tankarmor.add(new ItemStack(Material.IRON_BOOTS));
        tankarmor.add(new ItemStack(Material.IRON_LEGGINGS));
        tankarmor.add(new ItemStack(Material.IRON_CHESTPLATE));
        tankarmor.add(new ItemStack(Material.IRON_HELMET));
        tank.add(new ItemStack(Material.DIAMOND_SWORD));
        tankeffect.add(new PotionEffect(PotionEffectType.SLOW, 999999999, 0));
        tankeffect.add(new PotionEffect(PotionEffectType.REGENERATION, 999999999, 0));
        //VIRUS
        virusarmor.add(null);
        virusarmor.add(null);
        virusarmor.add(getColoredArmor(Color.BLACK, Material.LEATHER_CHESTPLATE));
        virusarmor.add(null);
        virus.add(new ItemStack(Material.IRON_SWORD));
        viruseffect.add(new PotionEffect(PotionEffectType.SPEED, 999999999, 2));
        viruseffect.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999999, 0));
        //SUPER_VIRUS
        super_virusarmor.add(getColoredArmor(Color.BLACK, Material.LEATHER_BOOTS));
        super_virusarmor.add(getColoredArmor(Color.BLACK, Material.LEATHER_LEGGINGS));
        super_virusarmor.add(getColoredArmor(Color.BLACK, Material.LEATHER_CHESTPLATE));
        super_virusarmor.add(getColoredArmor(Color.BLACK, Material.LEATHER_HELMET));
        super_virus.add(new ItemStack(Material.IRON_SWORD));
        super_viruseffect.add(new PotionEffect(PotionEffectType.SPEED, 999999999, 2));
        super_viruseffect.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999999, 0));
    }

    public ArrayList<ItemStack> getInventory(Kit kit) {
        switch (kit) {
            case MEDIC:
                return medic;
            case RUNNER:
                return runner;
            case SNIPER:
                return sniper;
            case SUPER_VIRUS:
                return super_virus;
            case TANK:
                return tank;
            case VIRUS:
                return virus;
        }
        return null;
    }

    public ArrayList<ItemStack> getArmor(Kit kit) {
        switch (kit) {
            case MEDIC:
                return medicarmor;
            case RUNNER:
                return runnerarmor;
            case SNIPER:
                return sniperarmor;
            case SUPER_VIRUS:
                return super_virusarmor;
            case TANK:
                return tankarmor;
            case VIRUS:
                return virusarmor;
        }
        return null;
    }

    public ArrayList<PotionEffect> getEffects(Kit kit) {
        switch (kit) {
            case MEDIC:
                return mediceffect;
            case RUNNER:
                return runnereffect;
            case SNIPER:
                return snipereffect;
            case SUPER_VIRUS:
                return super_viruseffect;
            case TANK:
                return tankeffect;
            case VIRUS:
                return viruseffect;
        }
        return null;
    }

    public ItemStack getColoredArmor(Color c, Material m) {
        ItemStack ret = new ItemStack(m);
        LeatherArmorMeta meta = (LeatherArmorMeta) ret.getItemMeta();
        meta.setColor(c);
        ret.setItemMeta(meta);
        return ret;
    }
}

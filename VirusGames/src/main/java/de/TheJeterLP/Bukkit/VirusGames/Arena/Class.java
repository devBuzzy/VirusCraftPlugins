package de.TheJeterLP.Bukkit.VirusGames.Arena;

import de.TheJeterLP.Bukkit.VirusGames.util.Kits;
import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * @author TheJeterLP
 */
public enum Class {

    MEDIC(Kits.getInstance().getInventory(Kits.Kit.MEDIC), Kits.getInstance().getArmor(Kits.Kit.MEDIC), Kits.getInstance().getEffects(Kits.Kit.MEDIC)),
    RUNNER(Kits.getInstance().getInventory(Kits.Kit.RUNNER), Kits.getInstance().getArmor(Kits.Kit.RUNNER), Kits.getInstance().getEffects(Kits.Kit.RUNNER)),
    SNIPER(Kits.getInstance().getInventory(Kits.Kit.SNIPER), Kits.getInstance().getArmor(Kits.Kit.SNIPER), Kits.getInstance().getEffects(Kits.Kit.SNIPER)),
    TANK(Kits.getInstance().getInventory(Kits.Kit.TANK), Kits.getInstance().getArmor(Kits.Kit.TANK), Kits.getInstance().getEffects(Kits.Kit.TANK)),
    VIRUS(Kits.getInstance().getInventory(Kits.Kit.VIRUS), Kits.getInstance().getArmor(Kits.Kit.VIRUS), Kits.getInstance().getEffects(Kits.Kit.VIRUS)),
    SUPER_VIRUS(Kits.getInstance().getInventory(Kits.Kit.SUPER_VIRUS), Kits.getInstance().getArmor(Kits.Kit.SUPER_VIRUS), Kits.getInstance().getEffects(Kits.Kit.SUPER_VIRUS)),
    NO_CLASS(null, null, null);

    private final ArrayList<ItemStack> inventory, armor;
    private final ArrayList<PotionEffect> effects;

    Class(ArrayList<ItemStack> inventory, ArrayList<ItemStack> armor, ArrayList<PotionEffect> effects) {
        this.inventory = inventory;
        this.effects = effects;
        this.armor = armor;
    }

    public ArrayList<ItemStack> getContent() {
        return inventory;
    }

    public ArrayList<ItemStack> getArmor() {
        return armor;
    }

    public ArrayList<PotionEffect> getEffects() {
        return effects;
    }

}

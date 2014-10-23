package de.TheJeterLP.Bukkit.VillagerHunt.Arena;

import de.TheJeterLP.Bukkit.VillagerHunt.Bukkit.VillagerHunt;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager.PrefixType;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Zombie;

/**
 * @author TheJeterLP
 */
public class SpawnThread implements Runnable {

    private int currZombies = 15, wave = 1;
    private final Arena a;
    private final List<Zombie> zombies = new ArrayList<>();

    public SpawnThread(final int id) {
        this.a = ArenaManager.getArena(id);
    }

    public void killZombies() {
        List<Zombie> zs = new ArrayList<>();
        zs.addAll(zombies);
        for (Zombie z : zs) {
            z.damage(z.getMaxHealth());
            zombies.remove(z);
        }
    }

    public void killZombie(Zombie z) {
        z.damage(z.getMaxHealth());
        if (zombies.contains(z)) zombies.remove(z);
    }

    @Override
    public void run() {
        for (PlayerData pd : a.getDatas()) {
            pd.getPlayer().setLevel(wave);
            VillagerHunt.getMessageManager().message(pd.getPlayer(), PrefixType.INFO, "Wave: " + wave + " \n spawning " + currZombies + " Zombies!");
        }

        if (wave >= 16) {
            a.stop("The players have won the game! They fighted " + wave + " waves!");
            return;
        }

        wave++;

        for (int i = 0; i < currZombies; i++) {
            Zombie z = a.spawnZombie();
            zombies.add(z);
        }

        currZombies += 15;
    }
}

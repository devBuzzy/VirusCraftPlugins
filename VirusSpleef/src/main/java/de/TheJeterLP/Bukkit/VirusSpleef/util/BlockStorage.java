package de.TheJeterLP.Bukkit.VirusSpleef.util;

import de.TheJeterLP.Bukkit.VirusSpleef.Arena.Arena;
import de.TheJeterLP.Bukkit.VirusSpleef.Arena.ArenaState;
import de.TheJeterLP.Bukkit.VirusSpleef.Bukkit.VirusSpleef;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * @author TheJeterLP
 */
public class BlockStorage {

    private final HashMap<Location, BlockContainer> blocks = new HashMap<>();
    private final Arena a;

    public BlockStorage(Arena a) {
        this.a = a;
    }

    public void breakBlock(final Block b) {
        Location loc = b.getLocation();
        Material mat = b.getType();
        if (mat == Material.STAINED_CLAY) {
            blocks.put(loc, new BlockContainer(b));
            Bukkit.getScheduler().scheduleSyncDelayedTask(VirusSpleef.getInstance(), new Runnable() {
                @Override
                public void run() {
                    b.setType(Material.AIR);
                }
            }, Math.max(new Random().nextInt(3), 1));
        }
    }

    public void resetArena() {
        a.updateStatusAndSign(ArenaState.RESETTING);
        for (Location loc : blocks.keySet()) {
            BlockContainer container = blocks.get(loc);
            Block b = loc.getBlock();
            b.setType(container.getType());
            b.setData(container.getData());
        }
        blocks.clear();
    }
}

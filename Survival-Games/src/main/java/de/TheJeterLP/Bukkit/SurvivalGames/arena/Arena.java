package de.TheJeterLP.Bukkit.SurvivalGames.arena;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

public class Arena {

    private final Location max, min;
    private final Random r = new Random();

    public Arena(Location min, Location max) {
        this.max = max;
        this.min = min;

    }

    public boolean containsBlock(Location v) {
        if (v.getWorld() != min.getWorld()) return false;
        final double x = v.getX();
        final double y = v.getY();
        final double z = v.getZ();
        return x >= min.getBlockX() && x < max.getBlockX() + 1 && y >= min.getBlockY() && y < max.getBlockY() + 1 && z >= min.getBlockZ() && z < max.getBlockZ() + 1;
    }

    public Location getMax() {
        return max;
    }

    public Location getMin() {
        return min;
    }

    public Location getRandomLocation() {
        int xr = getRandomInt(min.getBlockX(), max.getBlockX());
        int zr = getRandomInt(min.getBlockZ(), max.getBlockZ());
        int y = -1;

        for (int i = min.getBlockY(); i < max.getBlockY(); i++) {
            Location test = new Location(min.getWorld(), xr, i, zr);

            if (!onGround(test)) continue;

            y = i;
            break;
        }

        return new Location(min.getWorld(), xr, y, zr);
    }

    private boolean onGround(Location loc) {
        Block b = loc.getBlock();

        if (b.getType() != Material.AIR) return false;
        if (b.getRelative(BlockFace.DOWN).getType() == Material.AIR) return false;
        if (b.getRelative(BlockFace.DOWN).getType() == Material.NOTE_BLOCK) return false;
        if (b.getRelative(BlockFace.DOWN).getType() == Material.LONG_GRASS) return false;
        if (b.getRelative(BlockFace.DOWN).isLiquid()) return false;
        if (b.getRelative(BlockFace.DOWN).getType() == Material.LEAVES || b.getRelative(BlockFace.DOWN).getType() == Material.LEAVES_2) return false;
        if (b.getRelative(BlockFace.DOWN).getType() == Material.LEVER || b.getRelative(BlockFace.DOWN).getType() == Material.WOOD_BUTTON || b.getRelative(BlockFace.DOWN).getType() == Material.STONE_BUTTON || (b.getRelative(BlockFace.DOWN).getState() instanceof Sign) || b.getRelative(BlockFace.DOWN).getType() == Material.LADDER || b.getRelative(BlockFace.DOWN).getType() == Material.WOOD_PLATE || b.getRelative(BlockFace.DOWN).getType() == Material.STONE_PLATE) return false;
        if (b.getRelative(BlockFace.DOWN).getType() == Material.GLASS) return false;
        if (b.getRelative(BlockFace.DOWN).getType() == Material.CACTUS || b.getRelative(BlockFace.DOWN).getType() == Material.COCOA || b.getRelative(BlockFace.DOWN).getType() == Material.CROPS || b.getRelative(BlockFace.DOWN).getType() == Material.FIRE || b.getRelative(BlockFace.DOWN).getType() == Material.ITEM_FRAME || b.getRelative(BlockFace.DOWN).getType() == Material.MELON_STEM || b.getRelative(BlockFace.DOWN).getType() == Material.RED_ROSE || b.getRelative(BlockFace.DOWN).getType() == Material.YELLOW_FLOWER) return false;

        return true;
    }

    private int getRandomInt(int low, int high) {
        return r.nextInt(high - low) + low;
    }
}

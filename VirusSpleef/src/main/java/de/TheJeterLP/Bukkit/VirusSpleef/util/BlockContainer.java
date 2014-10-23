package de.TheJeterLP.Bukkit.VirusSpleef.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * @author TheJeterLP
 */
public class BlockContainer {

    private final byte data;
    private final Material mat;

    public BlockContainer(Block b) {
        data = b.getData();
        mat = b.getType();
    }

    public Material getType() {
        return mat;
    }

    @Deprecated
    public byte getData() {
        return data;
    }
}

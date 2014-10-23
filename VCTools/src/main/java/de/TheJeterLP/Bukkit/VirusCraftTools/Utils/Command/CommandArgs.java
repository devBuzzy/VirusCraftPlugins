package de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class CommandArgs {

    private final String[] args;

    public CommandArgs(String[] args) {
        this.args = args;
    }

    public String getString(int number) {
        return args[number];
    }

    public boolean isInteger(int number) {
        try {
            int i = Integer.valueOf(args[number]);
            i++;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isDouble(int number) {
        try {
            Double.valueOf(args[number]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isEmpty() {
        return args.length <= 0;
    }

    public int getLength() {
        return args.length;
    }

    public String[] getArgs() {
        return args;
    }

    public boolean isPlayer(int num) {
        Player p = Bukkit.getPlayer(args[num]);
        return p != null;
    }

    public Player getPlayer(int num) {
        return Bukkit.getPlayer(args[num]);
    }

    public int getInt(int num) {
        return Integer.valueOf(args[num]);
    }
}

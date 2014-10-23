package de.TheJeterLP.Bukkit.SurvivalGames.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import de.TheJeterLP.Bukkit.SurvivalGames.SurvivalGames;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtil {

    private static final HashMap<String, String> varcache = new HashMap<>();

    public static String replaceColors(String s) {
        return Utils.replaceColors(s);
    }

    public static String replaceVars(String msg, HashMap<String, String> vars) {
        boolean error = false;
        for (String s : vars.keySet()) {
            try {
                msg.replace("{$" + s + "}", vars.get(s));
            } catch (Exception e) {
                SurvivalGames.$(Level.WARNING, "Failed to replace string vars. Error on " + s);
                error = true;
            }
        }
        if (error) {
            SurvivalGames.$(Level.SEVERE, "Error replacing vars in message: " + msg);
            SurvivalGames.$(Level.SEVERE, "Vars: " + vars.toString());
            SurvivalGames.$(Level.SEVERE, "Vars Cache: " + varcache.toString());
        }
        return msg;
    }

    public static String replaceVars(String msg, String[] vars) {
        for (String str : vars) {
            String[] s = str.split("-");
            varcache.put(s[0], s[1]);
        }
        boolean error = false;
        for (String str : varcache.keySet()) {
            try {
                msg = msg.replace("{$" + str + "}", varcache.get(str));
            } catch (Exception e) {
                SurvivalGames.$(Level.WARNING, "Failed to replace string vars. Error on " + str);
                error = true;
            }
        }
        if (error) {
            SurvivalGames.$(Level.SEVERE, "Error replacing vars in message: " + msg);
            SurvivalGames.$(Level.SEVERE, "Vars: " + Arrays.toString(vars));
            SurvivalGames.$(Level.SEVERE, "Vars Cache: " + varcache.toString());
        }
        return msg;
    }
    
    public static String stylize(Player p, boolean s, boolean r) {
        String name = p.getDisplayName();
        if (p.isOp() && r) {
            name = ChatColor.DARK_RED + name;
        }
        if (p.isOp() && !r) {
            name = ChatColor.DARK_BLUE + name;
        }

        if (name.length() > 16) {
            name = name.substring(0, 16);
        }

        return name;
    }
}

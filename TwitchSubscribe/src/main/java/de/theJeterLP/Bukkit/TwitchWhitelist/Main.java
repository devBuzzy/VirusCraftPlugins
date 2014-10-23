/*
 * The MIT License
 *
 * Copyright 2014 TheJeterLP.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.theJeterLP.Bukkit.TwitchWhitelist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main INSTANCE;
    private GroupManager groupManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Config.load();
        
        if (Config.UNIQUE_ID.getString() == null || Config.UNIQUE_ID.getString().equalsIgnoreCase("CHANGEME")) {
            getLogger().info("Please set your unique ID in config.yml and restart your server.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupPerms()) {
            getLogger().info("GroupManager was not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    private List<String> getSubscriberList() {
        List<String> ret = new ArrayList<String>();
        String urlString = "http://whitelist.twitchapps.com/list.php?id=" + Config.UNIQUE_ID.getString();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null && !inputLine.trim().isEmpty()) {
                ret.add(inputLine.trim());
            }
            in.close();
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ret;
        }
    }

    protected boolean isSubscriber(Player p) {
        final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(p);
        
        String group = handler.getGroup(p.getName());

        if (group.equalsIgnoreCase(Config.GROUP_AFTER_PROMOTE.getString())) return false;

        for (String s : Config.BYPASS_GROUPS.getStringList()) {
            if (group.equalsIgnoreCase(s)) return false;
        }

        return getSubscriberList().contains(p.getName());
    }
    
    protected void promote(Player p) {
        final OverloadedWorldHolder ohandler = groupManager.getWorldsHolder().getWorldData(p);
        ohandler.getUser(p.getName()).setGroup(ohandler.getGroup(Config.GROUP_AFTER_PROMOTE.getString()));
        getLogger().info("Promoting " + p.getName() + " to group " + Config.GROUP_AFTER_PROMOTE.getString());
    }

    private boolean setupPerms() {
        if (!getServer().getPluginManager().isPluginEnabled("GroupManager")) return false;
        groupManager = (GroupManager) getServer().getPluginManager().getPlugin("GroupManager");
        return true;
    }
}

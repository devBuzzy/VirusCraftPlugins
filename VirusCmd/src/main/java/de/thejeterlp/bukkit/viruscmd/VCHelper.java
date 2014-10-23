/*
 * Copyright 2014 Joey.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.thejeterlp.bukkit.viruscmd;

import de.TheJeterLP.Bukkit.VirusCraftTools.Database.DataConnection;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.Manager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;
import de.thejeterlp.bukkit.viruscmd.commands.home.Command_Delhome;
import de.thejeterlp.bukkit.viruscmd.commands.home.Command_Edithome;
import de.thejeterlp.bukkit.viruscmd.commands.home.Command_Home;
import de.thejeterlp.bukkit.viruscmd.commands.home.Command_Sethome;
import de.thejeterlp.bukkit.viruscmd.commands.mob.Command_Killall;
import de.thejeterlp.bukkit.viruscmd.commands.mob.Command_Spawnmob;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_CW;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Enderchest;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Fly;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Gamemode;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_God;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Heal;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_IP;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Invsee;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Lightning;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_List;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Location;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_MSG;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Mute;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Nick;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Skull;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Slap;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Spy;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Sudo;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_UUID;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Unmute;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Vanish;
import de.thejeterlp.bukkit.viruscmd.commands.player.Command_Whois;
import de.thejeterlp.bukkit.viruscmd.commands.spawn.Command_Delspawn;
import de.thejeterlp.bukkit.viruscmd.commands.spawn.Command_Listspawns;
import de.thejeterlp.bukkit.viruscmd.commands.spawn.Command_Setspawn;
import de.thejeterlp.bukkit.viruscmd.commands.spawn.Command_Spawn;
import de.thejeterlp.bukkit.viruscmd.commands.teleport.Command_Down;
import de.thejeterlp.bukkit.viruscmd.commands.teleport.Command_Top;
import de.thejeterlp.bukkit.viruscmd.commands.teleport.Command_Tploc;
import de.thejeterlp.bukkit.viruscmd.commands.world.Command_Day;
import de.thejeterlp.bukkit.viruscmd.commands.world.Command_Night;
import de.thejeterlp.bukkit.viruscmd.commands.world.Command_Settime;
import de.thejeterlp.bukkit.viruscmd.commands.world.Command_Sun;
import de.thejeterlp.bukkit.viruscmd.listener.DoubleJumpListener;
import de.thejeterlp.bukkit.viruscmd.listener.PlayerChatListener;
import de.thejeterlp.bukkit.viruscmd.listener.PlayerCommandListener;
import de.thejeterlp.bukkit.viruscmd.listener.PlayerDamageListener;
import de.thejeterlp.bukkit.viruscmd.listener.PlayerDeathListener;
import de.thejeterlp.bukkit.viruscmd.listener.PlayerJoinListener;
import de.thejeterlp.bukkit.viruscmd.listener.SignListener;
import de.thejeterlp.bukkit.viruscmd.listener.WorldListener;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author TheJeterLP
 */
public class VCHelper {
    
    public static void initDatabase() throws SQLException {
        DataConnection conn = getDatabase();
        Statement s = conn.getStatement();
        String HOME_TABLE = "CREATE TABLE IF NOT EXISTS `vc_homes` ("
                + "`id` INTEGER PRIMARY KEY,"
                + "`playerid` INTEGER NOT NULL,"
                + "`location` varchar(320) NOT NULL,"
                + "`name` varchar(64) NOT NULL"
                + ");";
        String PLAYER_TABLE = "CREATE TABLE IF NOT EXISTS `vc_player` ("
                + "`id` INTEGER PRIMARY KEY,"
                + "`uuid` varchar(64) NOT NULL,"
                + "`displayname` varchar(64) NOT NULL,"
                + "`god` BOOLEAN,"
                + "`invisible` BOOLEAN,"
                + "`commandwatcher` BOOLEAN,"
                + "`spy` BOOLEAN,"
                + "`fly` BOOLEAN,"
                + "`muted` BOOLEAN"
                + ");";
        String SPAWN_TABLE = "CREATE TABLE IF NOT EXISTS `vc_spawns` ("
                + "`type` INTEGER NOT NULL,"
                + "`group` varchar(64) NOT NULL,"
                + "`location` varchar(320) NOT NULL"
                + ");";
        
        String WORLD_TABLE = "CREATE TABLE IF NOT EXISTS `vc_worlds` ("
                + "`name` varchar(64) PRIMARY KEY NOT NULL,"
                + "`paused` BOOLEAN NOT NULL,"
                + "`time` varchar(128) NOT NULL"
                + ");";
        s.executeUpdate(SPAWN_TABLE);
        s.executeUpdate(HOME_TABLE);
        s.executeUpdate(PLAYER_TABLE);
        s.executeUpdate(WORLD_TABLE);
        conn.closeStatment(s);
    }
    
    public static void registerCommands() {
        registerCommand(Command_Home.class);
        registerCommand(Command_Sethome.class);
        registerCommand(Command_Edithome.class);
        registerCommand(Command_Delhome.class);
        registerCommand(Command_Fly.class);
        registerCommand(Command_Gamemode.class);
        registerCommand(Command_Vanish.class);
        registerCommand(Command_Nick.class);
        registerCommand(Command_God.class);
        registerCommand(Command_CW.class);
        registerCommand(Command_Settime.class);
        registerCommand(Command_Day.class);
        registerCommand(Command_Night.class);
        registerCommand(Command_MSG.class);
        registerCommand(Command_Spy.class);
        registerCommand(Command_Heal.class);
        registerCommand(Command_Spawn.class);
        registerCommand(Command_Setspawn.class);
        registerCommand(Command_Delspawn.class);
        registerCommand(Command_Down.class);
        registerCommand(Command_Top.class);
        registerCommand(Command_Killall.class);
        registerCommand(Command_Tploc.class);
        registerCommand(Command_Invsee.class);
        registerCommand(Command_Enderchest.class);
        registerCommand(Command_Slap.class);
        registerCommand(Command_Spawnmob.class);
        registerCommand(Command_Lightning.class);
        registerCommand(Command_Skull.class);
        registerCommand(Command_Location.class);
        registerCommand(Command_UUID.class);
        registerCommand(Command_Mute.class);
        registerCommand(Command_Unmute.class);
        registerCommand(Command_IP.class);
        registerCommand(Command_Whois.class);
        registerCommand(Command_List.class);
        registerCommand(Command_Sun.class);
        registerCommand(Command_Listspawns.class);
        registerCommand(Command_Sudo.class);
    }
    
    public static void registerListener() {
        registerListener(PlayerJoinListener.class);
        registerListener(PlayerDamageListener.class);
        registerListener(PlayerCommandListener.class);
        registerListener(PlayerDeathListener.class);
        registerListener(WorldListener.class);
        registerListener(DoubleJumpListener.class);
        registerListener(PlayerChatListener.class);
        registerListener(SignListener.class);
    }
    
    public static void registerCommand(Class<? extends BaseCommand> clazz) {
        Manager.registerCommand(clazz, VirusCmd.getInstance());
    }
    
    public static void registerListener(Class<? extends VClistener> clazz) {
        Manager.registerListener(clazz, VirusCmd.getInstance());
    }
    
    public static DataConnection getDatabase() {
        return VCplugin.inst().getConnectionManager();
    }
    
}

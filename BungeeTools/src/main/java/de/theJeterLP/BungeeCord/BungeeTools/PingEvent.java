/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.theJeterLP.BungeeCord.BungeeTools;

import de.theJeterLP.BungeeCord.BungeeTools.Ping.MinecraftPing;
import de.theJeterLP.BungeeCord.BungeeTools.Ping.MinecraftPingReply;
import java.io.IOException;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author TheJeterLP
 */
public class PingEvent implements Listener {

    @EventHandler(priority = 2)
    public void onPing(final ProxyPingEvent e) throws IOException {
        ServerPing ping = e.getResponse();
        MinecraftPingReply reply = new MinecraftPing().getPing(Config.SERVER_IP.getString(), Config.SERVER_PORT.getInt());

        Players p = new Players(ping.getPlayers().getMax(), ping.getPlayers().getOnline() + reply.getOnlinePlayers(), ping.getPlayers().getSample());
        ping.setPlayers(p);
        e.setResponse(ping);
    }

}

package de.TheJeterLP.Bukkit.VirusCraftTools.Listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VCplugin;

/**
 * @author TheJeterLP
 */
public class TabListener {

    private ProtocolManager protocolManager;

    public void init() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(VCplugin.inst(),
                ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE)
                            try {
                                PacketContainer packet = event.getPacket();
                                String message = (String) packet.getSpecificModifier(String.class).read(0).toLowerCase();
                                if (message.equalsIgnoreCase("/")) event.setCancelled(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                }
        );
    }
}

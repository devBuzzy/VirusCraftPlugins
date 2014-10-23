package de.TheJeterLP.Bukkit.Nametags.Utils;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.entity.Player;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import org.bukkit.Bukkit;

public class PacketPlayOut {

    private final Object packet;
    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnection;
    private static String version = "";
    private static Class<?> packetType;

    static {
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            packetType = Class.forName("net.minecraft.server." + version + ".PacketPlayOutScoreboardTeam");
            Class<?> typeCraftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            Class<?> typeNMSPlayer = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
            Class<?> typePlayerConnection = Class.forName("net.minecraft.server." + version + ".PlayerConnection");
            getHandle = typeCraftPlayer.getMethod("getHandle");
            playerConnection = typeNMSPlayer.getField("playerConnection");
            sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet"));
        } catch (Exception e) {
            System.out.println("Failed to setup reflection for Packet209Mod!");
            e.printStackTrace();
        }
    }

    public PacketPlayOut(String name, String prefix, String suffix, Collection players, int paramInt) throws Exception {
        packet = packetType.newInstance();
        setField("a", name);
        setField("f", paramInt);
        if ((paramInt == 0) || (paramInt == 2)) {
            setField("b", name);
            setField("c", prefix);
            setField("d", suffix);
            setField("g", 1);
        }
        if (paramInt == 0) {
            addAll(players);
        }
    }

    public PacketPlayOut(String name, Collection players, int paramInt) throws Exception {
        packet = packetType.newInstance();
        if ((players == null) || (players.isEmpty())) {
            players = new ArrayList<>();
        }
        setField("a", name);
        setField("f", paramInt);
        addAll(players);
    }

    public void sendToPlayer(Player bukkitPlayer) throws Exception {
        Object player = getHandle.invoke(bukkitPlayer);
        Object connection = playerConnection.get(player);
        sendPacket.invoke(connection, packet);
    }

    private void setField(String field, Object value) throws Exception {
        Field f = packet.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(packet, value);

    }

    @SuppressWarnings("unchecked")
    private void addAll(Collection<?> col) throws Exception {
        Field f = packet.getClass().getDeclaredField("e");
        f.setAccessible(true);
        ((Collection) f.get(packet)).addAll(col);
    }

}

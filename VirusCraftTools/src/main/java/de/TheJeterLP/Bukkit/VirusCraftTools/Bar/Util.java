package de.TheJeterLP.Bukkit.VirusCraftTools.Bar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Util {

    public static String version;
    public static Class<?> fakeDragonClass = FakeDragonNMS.class;

    public static void init() {
        for (Package pa : Package.getPackages()) {
            if (pa.getName().startsWith("net.minecraft.server")) {
                version = pa.getName().split("\\.")[3] + ".";
            }
        }
    }

    public static FakeDragon newDragon(String message, Location loc) {
        FakeDragon fakeDragon = new FakeDragonNMS(message, loc);
        return fakeDragon;
    }

    public static void sendPacket(Player p, Object packet) {
        try {
            Object nmsPlayer = getHandle(p);
            Field con_field = nmsPlayer.getClass().getField("playerConnection");
            con_field.setAccessible(true);
            Object con = con_field.get(nmsPlayer);
            Method packet_method = getMethod(con.getClass(), "sendPacket");
            packet_method.setAccessible(true);
            packet_method.invoke(con, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getCraftClass(String ClassName) {
        String className = "net.minecraft.server." + version + ClassName;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static Object getHandle(World world) {
        Object nms_entity = null;
        Method entity_getHandle = getMethod(world.getClass(), "getHandle");
        entity_getHandle.setAccessible(true);
        try {
            nms_entity = entity_getHandle.invoke(world);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nms_entity;
    }

    public static Object getHandle(Entity entity) {
        Object nms_entity = null;
        Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
        entity_getHandle.setAccessible(true);
        try {
            nms_entity = entity_getHandle.invoke(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nms_entity;
    }

    public static Field getField(Class<?> cl, String field_name) {
        try {
            Field field = cl.getDeclaredField(field_name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> cl, String method, Class<?>[] args) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method) && ClassListEqual(args, m.getParameterTypes())) {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(Class<?> cl, String method, Integer args) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method) && args.equals(new Integer(m.getParameterTypes().length))) {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }

    public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;

        if (l1.length != l2.length)
            return false;
        for (int i = 0; i < l1.length; i++) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }

        return equal;
    }

}

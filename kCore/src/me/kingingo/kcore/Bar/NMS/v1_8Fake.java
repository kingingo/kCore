package me.kingingo.kcore.Bar.NMS;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;

import org.bukkit.Location;

public class v1_8Fake extends FakeDragon
{
  private Object dragon;
  private int id;

  public v1_8Fake(String name, Location loc)
  {
    super(name, loc);
  }

  public Object getSpawnPacket()
  {
    Class Entity = UtilReflection.getNMSClass("Entity");
    Class EntityLiving = UtilReflection.getNMSClass("EntityLiving");
    Class EntityEnderDragon = UtilReflection.getNMSClass("EntityEnderDragon");
    Object packet = null;
    try {
      this.dragon = EntityEnderDragon.getConstructor(new Class[] { UtilReflection.getNMSClass("World") }).newInstance(new Object[] { getWorld() });

      Method setLocation = UtilReflection.getMethod(EntityEnderDragon, "setLocation", new Class[] { Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE });
      setLocation.invoke(this.dragon, new Object[] { Integer.valueOf(getX()), Integer.valueOf(getY()), Integer.valueOf(getZ()), Integer.valueOf(getPitch()), Integer.valueOf(getYaw()) });

      Method setInvisible = UtilReflection.getMethod(EntityEnderDragon, "setInvisible", new Class[] { Boolean.TYPE });
      setInvisible.invoke(this.dragon, new Object[] { Boolean.valueOf(true) });

      Method setCustomName = UtilReflection.getMethod(EntityEnderDragon, "setCustomName", new Class[] { String.class });
      setCustomName.invoke(this.dragon, new Object[] { this.name });

      Method setHealth = UtilReflection.getMethod(EntityEnderDragon, "setHealth", new Class[] { Float.TYPE });
      setHealth.invoke(this.dragon, new Object[] { Float.valueOf(this.health) });

      Field motX = UtilReflection.getField(Entity, "motX");
      motX.set(this.dragon, Byte.valueOf(getXvel()));

      Field motY = UtilReflection.getField(Entity, "motY");
      motY.set(this.dragon, Byte.valueOf(getYvel()));

      Field motZ = UtilReflection.getField(Entity, "motZ");
      motZ.set(this.dragon, Byte.valueOf(getZvel()));

      Method getId = UtilReflection.getMethod(EntityEnderDragon, "getId", new Class[0]);
      this.id = ((Integer)getId.invoke(this.dragon, new Object[0])).intValue();

      Class PacketPlayOutSpawnEntityLiving = UtilReflection.getNMSClass("PacketPlayOutSpawnEntityLiving");

      packet = PacketPlayOutSpawnEntityLiving.getConstructor(new Class[] { EntityLiving }).newInstance(new Object[] { this.dragon });
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    return packet;
  }

  public Object getDestroyPacket()
  {
    Class PacketPlayOutEntityDestroy = UtilReflection.getNMSClass("PacketPlayOutEntityDestroy");

    Object packet = null;
    try {
      packet = PacketPlayOutEntityDestroy.newInstance();
      Field a = PacketPlayOutEntityDestroy.getDeclaredField("a");
      a.setAccessible(true);
      a.set(packet, new int[] { this.id });
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }

    return packet;
  }

  public Object getMetaPacket(Object watcher)
  {
    Class DataWatcher = UtilReflection.getNMSClass("DataWatcher");

    Class PacketPlayOutEntityMetadata = UtilReflection.getNMSClass("PacketPlayOutEntityMetadata");

    Object packet = null;
    try {
      packet = PacketPlayOutEntityMetadata.getConstructor(new Class[] { Integer.TYPE, DataWatcher, Boolean.TYPE }).newInstance(new Object[] { Integer.valueOf(this.id), watcher, Boolean.valueOf(true) });
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    return packet;
  }

  public Object getTeleportPacket(Location loc)
  {
//    Class PacketPlayOutEntityTeleport = UtilReflection.getNMSClass("PacketPlayOutEntityTeleport");
//    Object packet = null;
//    try
//    {
//      packet = PacketPlayOutEntityTeleport.getConstructor(new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE, Boolean.TYPE }).newInstance(new Object[] { Integer.valueOf(this.id), Integer.valueOf(loc.getBlockX() * 32), Integer.valueOf(loc.getBlockY() * 32), Integer.valueOf(loc.getBlockZ() * 32), Byte.valueOf((byte)((int)loc.getYaw() * 256 / 360)), Byte.valueOf((byte)((int)loc.getPitch() * 256 / 360)), Boolean.valueOf(false), Boolean.valueOf(false) });
//    } catch (IllegalArgumentException e) {
//      e.printStackTrace();
//    } catch (SecurityException e) {
//      e.printStackTrace();
//    } catch (InstantiationException e) {
//      e.printStackTrace();
//    } catch (IllegalAccessException e) {
//      e.printStackTrace();
//    } catch (InvocationTargetException e) {
//      e.printStackTrace();
//    } catch (NoSuchMethodException e) {
//      e.printStackTrace();
//    }
//
//    return packet;
	  PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
	  	UtilReflection.setValue("b", packet, MathHelper.floor(loc.getX() * 32.0D));
	  	UtilReflection.setValue("c", packet, MathHelper.floor(loc.getY() * 32.0D));
	  	UtilReflection.setValue("d", packet, MathHelper.floor(loc.getZ() * 32.0D));
	  	UtilReflection.setValue("e", packet, ((byte) ((int) (loc.getYaw() * 256.0F / 360.0F))));
	  	UtilReflection.setValue("f", packet, ((byte) ((int) (loc.getPitch() * 256.0F / 360.0F))));

	    return packet;
  }

  public Object getWatcher()
  {
    Class Entity = UtilReflection.getNMSClass("Entity");
    Class DataWatcher = UtilReflection.getNMSClass("DataWatcher");

    Object watcher = null;
    try {
      watcher = DataWatcher.getConstructor(new Class[] { Entity }).newInstance(new Object[] { this.dragon });
      Method a = UtilReflection.getMethod(DataWatcher, "a", new Class[] { Integer.TYPE, Object.class });

      a.invoke(watcher, new Object[] { Integer.valueOf(5), Byte.valueOf(isVisible() ? "0" : "32") });
      a.invoke(watcher, new Object[] { Integer.valueOf(6), Float.valueOf(this.health) });
      a.invoke(watcher, new Object[] { Integer.valueOf(7), Integer.valueOf(0) });
      a.invoke(watcher, new Object[] { Integer.valueOf(8), Byte.valueOf("0") });
      a.invoke(watcher, new Object[] { Integer.valueOf(10), this.name });
      a.invoke(watcher, new Object[] { Integer.valueOf(11), Byte.valueOf("1") });
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    return watcher;
  }

  public static boolean isUsable() {
    Class PacketPlayOutEntityTeleport = UtilReflection.getNMSClass("PacketPlayOutEntityTeleport");
    try
    {
      PacketPlayOutEntityTeleport.getConstructor(new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE, Boolean.TYPE });
    } catch (IllegalArgumentException e) {
      return false;
    } catch (SecurityException e) {
      return false;
    } catch (NoSuchMethodException e) {
      return false;
    }

    return true;
  }
}
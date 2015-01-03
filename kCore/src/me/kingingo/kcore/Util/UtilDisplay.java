package me.kingingo.kcore.Util;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class UtilDisplay {
 
    public static Map<String, UtilDisplay> dragonplayers = new HashMap<String, UtilDisplay>();
    public static final int MAX_HEALTH = 200;
    public boolean visible;
    public int EntityID;
    public int x;
    public int y;
    public int z;
    public int pitch = 0;
    public int head_pitch = 0;
    public int yaw = 0;
    public byte xvel = 0;
    public byte yvel = 0;
    public byte zvel = 0;
    public float health;
    public String name;
    public Location sloc;
    private Object dragon;
 
    public UtilDisplay(String name, int EntityID, Location loc){
        this(name, EntityID, (int) Math.floor(loc.getBlockX() * 32.0D), (int) Math.floor(loc.getBlockY() * 32.0D), (int) Math.floor(loc.getBlockZ() * 32.0D));
        this.sloc=loc;
    }
 
    public UtilDisplay(String name, int EntityID, Location loc, float health, boolean visible){
        this(name, EntityID, (int) Math.floor(loc.getBlockX() * 32.0D), (int) Math.floor(loc.getBlockY() * 32.0D), (int) Math.floor(loc.getBlockZ() * 32.0D), health, visible);
        this.sloc=loc;
    }
 
    public UtilDisplay(String name, int EntityID, int x, int y, int z){
        this(name, EntityID, x, y, z, MAX_HEALTH, false);
    }
 
    public UtilDisplay(String name, int EntityID, int x, int y, int z, float health, boolean visible){
        this.name=name;
        this.EntityID=EntityID;
        this.x=x;
        this.y=y;
        this.z=z;
        this.health=health;
        this.visible=visible;
    }
 
    public Object getSpawnPacket()
    {
      Class Entity = Util.getCraftClass("Entity");
      Class EntityLiving = Util.getCraftClass("EntityLiving");
      Class EntityEnderDragon = Util.getCraftClass("EntityEnderDragon");
      Object packet = null;
      try {
    	  CraftWorld w = (CraftWorld)sloc.getWorld();
        this.dragon = EntityEnderDragon.getConstructor(new Class[] { Util.getCraftClass("World") }).newInstance(new Object[] { w.getHandle() });

        Method setLocation = Util.getMethod(EntityEnderDragon, "setLocation", new Class[] { Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE });
        setLocation.invoke(this.dragon, new Object[] { Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z), Integer.valueOf(pitch), Integer.valueOf(yaw) });

        Method setInvisible = Util.getMethod(EntityEnderDragon, "setInvisible", new Class[] { Boolean.TYPE });
        setInvisible.invoke(this.dragon, new Object[] { Boolean.valueOf(visible) });

        Method setCustomName = Util.getMethod(EntityEnderDragon, "setCustomName", new Class[] { String.class });
        setCustomName.invoke(this.dragon, new Object[] { this.name });

        Method setHealth = Util.getMethod(EntityEnderDragon, "setHealth", new Class[] { Float.TYPE });
        setHealth.invoke(this.dragon, new Object[] { Float.valueOf(this.health) });

        Field motX = Util.getField(Entity, "motX");
        motX.set(this.dragon, Byte.valueOf(xvel));

        Field motY = Util.getField(Entity, "motX");
        motY.set(this.dragon, Byte.valueOf(yvel));

        Field motZ = Util.getField(Entity, "motX");
        motZ.set(this.dragon, Byte.valueOf(zvel));

        Method getId = Util.getMethod(EntityEnderDragon, "getId", new Class[0]);
        this.EntityID = ((Integer)getId.invoke(this.dragon, new Object[0])).intValue();

        Class PacketPlayOutSpawnEntityLiving = Util.getCraftClass("PacketPlayOutSpawnEntityLiving");

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
      Class PacketPlayOutEntityDestroy = Util.getCraftClass("PacketPlayOutEntityDestroy");

      Object packet = null;
      try {
        packet = PacketPlayOutEntityDestroy.newInstance();
        Field a = PacketPlayOutEntityDestroy.getDeclaredField("a");
        a.setAccessible(true);
        a.set(packet, new int[] { EntityID });
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
      Class DataWatcher = Util.getCraftClass("DataWatcher");

      Class PacketPlayOutEntityMetadata = Util.getCraftClass("PacketPlayOutEntityMetadata");

      Object packet = null;
      try {
        packet = PacketPlayOutEntityMetadata.getConstructor(new Class[] { Integer.TYPE, DataWatcher, Boolean.TYPE }).newInstance(new Object[] { Integer.valueOf(EntityID), watcher, Boolean.valueOf(true) });
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

    public Object getTeleportPacket(int entityid,Location loc)
    {
    	
    	PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
    	UtilReflection.setValue("a", packet, entityid);
    	UtilReflection.setValue("b", packet, MathHelper.floor(loc.getX() * 32.0D));
    	UtilReflection.setValue("c", packet, MathHelper.floor(loc.getY() * 32.0D));
    	UtilReflection.setValue("d", packet, MathHelper.floor(loc.getZ() * 32.0D));
    	UtilReflection.setValue("e", packet, ((byte) ((int) (loc.getYaw() * 256.0F / 360.0F))));
    	UtilReflection.setValue("f", packet, ((byte) ((int) (loc.getPitch() * 256.0F / 360.0F))));

      return packet;
    }
 
   
 
    public Object getWatcher()
    {
      Class Entity = Util.getCraftClass("Entity");
      Class DataWatcher = Util.getCraftClass("DataWatcher");

      Object watcher = null;
      try {
        watcher = DataWatcher.getConstructor(new Class[] { Entity }).newInstance(new Object[] { this.dragon });
        Method a = Util.getMethod(DataWatcher, "a", new Class[] { Integer.TYPE, Object.class });

        a.invoke(watcher, new Object[] { Integer.valueOf(0), Byte.valueOf(visible ? "0" : "32") });
        a.invoke(watcher, new Object[] { Integer.valueOf(6), Float.valueOf(this.health) });
        a.invoke(watcher, new Object[] { Integer.valueOf(7), Integer.valueOf(0) });
        a.invoke(watcher, new Object[] { Integer.valueOf(8), Byte.valueOf("0") });
        a.invoke(watcher, new Object[] { Integer.valueOf(10), this.name });
        a.invoke(watcher, new Object[] { Integer.valueOf(11), Byte.valueOf("1") });
      }
      catch (IllegalArgumentException e) {
        e.printStackTrace();
      }
      catch (SecurityException e) {
        e.printStackTrace();
      }
      catch (InstantiationException e) {
        e.printStackTrace();
      }
      catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      catch (InvocationTargetException e) {
        e.printStackTrace();
      }
      catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
      return watcher;
    }
    public static void setStatus(Player player, String text, int healthpercent){
        UtilDisplay dragon = null;
        if(dragonplayers.containsKey(player.getName())){
            dragon = dragonplayers.get(player.getName());
        }else if(!text.equals("")){
            dragon = new UtilDisplay(text, 6000, player.getLocation().add(0, -198, 0));
            dragonplayers.put(player.getName(), dragon);
            Object mobPacket = dragon.getSpawnPacket();
            Util.sendPacket(player, mobPacket);
        }
     
        if(text.equals("") && dragonplayers.containsKey(player.getName())){
            Object destroyPacket = dragon.getDestroyPacket();
            Util.sendPacket(player, destroyPacket);
         
            dragonplayers.remove(player.getName());
        }else{
        	
            dragon.name=text;
            dragon.health=(healthpercent/100f)*UtilDisplay.MAX_HEALTH;
            Object metaPacket = dragon.getMetaPacket(dragon.getWatcher());
            Object teleportPacket = dragon.getTeleportPacket(dragon.EntityID,player.getLocation().add(0, -200, 0));
            Util.sendPacket(player, metaPacket);
            Util.sendPacket(player, teleportPacket);
        }
    }
 
    public static void displayDragonTextBar(JavaPlugin plugin, String text, final Player player, long length){
        setStatus(player, text, 100);
 
        new BukkitRunnable(){
            @Override
            public void run(){
                setStatus(player, "", 100);
            }
        }.runTaskLater(plugin, length);
    }
 
    public static void displayDragonLoadingBar(final JavaPlugin plugin, final String text, final String completeText, final Player player, final int healthAdd, final long delay, final boolean loadUp){
        setStatus(player, "", (loadUp ? 1 : 100));
 
        new BukkitRunnable(){
            int health = (loadUp ? 1 : 100);
 
            @Override
            public void run(){
                if((loadUp ? health < 100 : health > 1)){
                    setStatus(player, text, health);
                    if(loadUp){
                        health += healthAdd;
                    } else {
                        health -= healthAdd;
                    }
                } else {
                    setStatus(player, completeText, (loadUp ? 100 : 1));
                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            setStatus(player, "", (loadUp ? 100 : 1));
                        }
                    }.runTaskLater(plugin, 20);
 
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, delay, delay);
    }
 
    public static void displayDragonLoadingBar(final JavaPlugin plugin, final String text, final String completeText, final Player player, final int secondsDelay, final boolean loadUp){
        final int healthChangePerSecond = 100 / secondsDelay / 4;
 
        displayDragonLoadingBar(plugin, text, completeText, player, healthChangePerSecond, 5L, loadUp);
    }
 
    
    public static void displayTextBarPercent(Player p,String text,int time){
    	try{
    	setStatus(p, "", time);
    	} catch (Exception error){}
    	setStatus(p, text, time);
    }
    
    
    public static void displayTextBar(Player p,String text){
		if(UtilPlayer.getVersion(p)>=47)return;
    	try{
    	setStatus(p, "", 100);
    	} catch (Exception error){}
    	setStatus(p, text, 100);
    }
    
    
    public static void displayTextBar(String text,Player p){
		if(UtilPlayer.getVersion(p)>=47)return;
    	try{
    	setStatus(p, "", 100);
    	} catch (Exception error){}
    	setStatus(p, text, 100);
    }
    
}






 class Util
{

  public static void sendPacket(Player p, Object packet)
  {
    try {
      Object nmsPlayer = getHandle(p);
      Field con_field = nmsPlayer.getClass().getField("playerConnection");
      Object con = con_field.get(nmsPlayer);
      Method packet_method = getMethod(con.getClass(), "sendPacket");
      packet_method.invoke(con, new Object[] { packet });
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  public static Class<?> getCraftClass(String ClassName) {
    String className = "net.minecraft.server." + "v1_7_R4." + ClassName;
    Class c = null;
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
    try {
      nms_entity = entity_getHandle.invoke(world, new Object[0]);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return nms_entity;
  }

  public static Object getHandle(Entity entity) {
    Object nms_entity = null;
    Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
    try {
      nms_entity = entity_getHandle.invoke(entity, new Object[0]);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return nms_entity;
  }

  public static Field getField(Class<?> cl, String field_name) {
    try {
      Field field = cl.getDeclaredField(field_name);
      return field;
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Method getMethod(Class<?> cl, String method, Class<?>[] args) {
    for (Method m : cl.getMethods()) {
      if ((m.getName().equals(method)) && (ClassListEqual(args, m.getParameterTypes()))) {
        return m;
      }
    }
    return null;
  }

  public static Method getMethod(Class<?> cl, String method, Integer args) {
    for (Method m : cl.getMethods()) {
      if ((m.getName().equals(method)) && (args.equals(new Integer(m.getParameterTypes().length)))) {
        return m;
      }
    }
    return null;
  }

  public static Method getMethod(Class<?> cl, String method) {
    for (Method m : cl.getMethods()) {
      if (m.getName().equals(method)) {
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

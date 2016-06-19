package eu.epicpvp.kcore.Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public enum UtilParticle
{
  HUGE_EXPLOSION(
    "hugeexplosion", 
    "EXPLOSION_HUGE"), 
//  SMOKE(
//	"smoke",
//    "SMOKE"),
  LARGE_EXPLODE(
    "largeexplode", 
    "EXPLOSION_LARGE"), 
  BUBBLE(
    "bubble", 
    "WATER_BUBBLE"), 
  SUSPEND(
    "suspended", 
    "SUSPENDED"), 
  DEPTH_SUSPEND(
    "depthsuspend", 
    "SUSPENDED_DEPTH"), 
  MAGIC_CRIT(
    "magicCrit", 
    "CRIT_MAGIC"), 
  MOB_SPELL(
    "mobSpell", 
    "SPELL_MOB", 
    true), 
  MOB_SPELL_AMBIENT(
    "mobSpellAmbient", 
    "SPELL_MOB_AMBIENT"), 
  INSTANT_SPELL(
    "instantSpell", 
    "SPELL_INSTANT"), 
  WITCH_MAGIC(
    "witchMagic", 
    "SPELL_WITCH"), 
  EXPLODE(
    "explode", 
    "EXPLOSION_NORMAL"), 
  SPLASH(
    "splash", 
    "WATER_SPLASH"), 
  LARGE_SMOKE(
    "largesmoke", 
    "SMOKE_LARGE"), 
  RED_DUST(
    "reddust", 
    "REDSTONE", 
    true), 
  SNOWBALL_POOF(
    "snowballpoof", 
    "SNOWBALL"), 
  ANGRY_VILLAGER(
    "angryVillager", 
    "VILLAGER_ANGRY"), 
  HAPPY_VILLAGER(
    "happyVillager", 
    "VILLAGER_HAPPY"), 
  EXPLOSION_NORMAL(
    EXPLODE.getName()), 
  EXPLOSION_LARGE(
    LARGE_EXPLODE.getName()), 
  EXPLOSION_HUGE(
    HUGE_EXPLOSION.getName()), 
  FIREWORKS_SPARK(
    "fireworksSpark"), 
  WATER_BUBBLE(
    BUBBLE.getName()), 
  WATER_SPLASH(
    SPLASH.getName()), 
  WATER_WAKE(
    "wake"), 
  SUSPENDED(
    SUSPEND.getName()), 
  SUSPENDED_DEPTH(
    DEPTH_SUSPEND.getName()), 
  CRIT(
    "crit"), 
  CRIT_MAGIC(
    MAGIC_CRIT.getName()), 
  SMOKE_NORMAL(
    "smoke"), 
  SMOKE_LARGE(
    LARGE_SMOKE.getName()), 
  SPELL(
    "spell"), 
  SPELL_INSTANT(
    INSTANT_SPELL.getName()), 
  SPELL_MOB(
    MOB_SPELL.getName(), 
    true), 
  SPELL_MOB_AMBIENT(
    MOB_SPELL_AMBIENT.getName()), 
  SPELL_WITCH(
    WITCH_MAGIC.getName()), 
  DRIP_WATER(
    "dripWater"), 
  DRIP_LAVA(
    "dripLava"), 
  VILLAGER_ANGRY(
    ANGRY_VILLAGER.getName()), 
  VILLAGER_HAPPY(
    HAPPY_VILLAGER.getName()), 
  TOWN_AURA(
    "townaura"), 
  NOTE(
    "note", 
    true), 
  PORTAL(
    "portal"), 
  ENCHANTMENT_TABLE(
    "enchantmenttable"), 
  FLAME(
    "flame"), 
  LAVA(
    "lava"), 
  FOOTSTEP(
    "footstep"), 
  CLOUD(
    "cloud"), 
  REDSTONE(
    "reddust", 
    true), 
  SNOWBALL(
    "snowballpoof"), 
  SNOW_SHOVEL(
    "snowshovel"), 
  SLIME(
    "slime"), 
  HEART(
    "heart"), 
  BARRIER(
    "barrier"), 
  ITEM_CRACK(
    "iconcrack_"), 
  BLOCK_CRACK(
    "blockcrack_"), 
  BLOCK_DUST(
    "blockdust_"), 
  WATER_DROP(
    "droplet"), 
  ITEM_TAKE(
    "take"), 
  MOB_APPEARANCE(
    "mobappearance");

  private String particleName;
  private String enumValue;
  private boolean hasColor;
  private static Class<?> nmsPacketPlayOutParticle;
  private static Class<?> nmsEnumParticle;
  private static int particleRange;
  private static Class<?> nmsPlayerConnection;
  private static Class<?> nmsEntityPlayer;
  private static Class<?> ioNettyChannel;
  private static Method nmsNetworkGetVersion;
  private static Field nmsFieldPlayerConnection;
  private static Field nmsFieldNetworkManager;
  private static Field nmsFieldNetworkManagerI;
  private static Field nmsFieldNetworkManagerM;

  static
  {
    nmsPacketPlayOutParticle = ReflectionUtilities.getNMSClass("PacketPlayOutWorldParticles");

    particleRange = 25;

    String ver = ReflectionUtilities.getVersion();
    try {
      nmsPlayerConnection = ReflectionUtilities.getNMSClass("PlayerConnection");
      nmsEntityPlayer = ReflectionUtilities.getNMSClass("EntityPlayer");
      ioNettyChannel = ver.contains("1_7") ? Class.forName("net.minecraft.util.io.netty.channel.Channel") : Class.forName("io.netty.channel.Channel");

      nmsFieldPlayerConnection = ReflectionUtilities.getField(nmsEntityPlayer, "playerConnection");
      nmsFieldNetworkManager = ReflectionUtilities.getField(nmsPlayerConnection, "networkManager");
      nmsFieldNetworkManagerI = ReflectionUtilities.getField(nmsFieldNetworkManager.getType(), "i");
      nmsFieldNetworkManagerM = ReflectionUtilities.getField(nmsFieldNetworkManager.getType(), "m");

      nmsNetworkGetVersion = ReflectionUtilities.getMethod(nmsFieldNetworkManager.getType(), "getVersion", new Class[] { ioNettyChannel });
    }
    catch (Exception e) {
      System.err.println("[ParticleLIB] Error while loading: " + e.getMessage());
      e.printStackTrace(System.err);
      Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("ParticleLIB"));
    }
  }

  private UtilParticle(String particleName, String enumValue, boolean hasColor)
  {
    this.particleName = particleName;
    this.enumValue = enumValue;
    this.hasColor = hasColor;
  }

  private UtilParticle(String particleName, String enumValue) {
    this(particleName, enumValue, false);
  }

  private UtilParticle(String particleName) {
    this(particleName, null);
  }

  private UtilParticle(String particleName, boolean hasColor) {
    this(particleName, null, hasColor);
  }

  private UtilParticle() {
    this(null, null);
  }

  public String getName() {
    return this.particleName;
  }

  public static void setRange(int range)
  {
    if (range < 0) throw new IllegalArgumentException("Range must be positive!");
    if (range > 2147483647) throw new IllegalArgumentException("Range is too big!");
    particleRange = range;
  }

  public static int getRange()
  {
    return particleRange;
  }

  private void sendToPlayer(Player player, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count, int[] extra)
    throws Exception
  {
    if (!isPlayerInRange(player, location)) return;

    if (ReflectionUtilities.getVersion().contains("v1_8"))
      try {
        if (nmsEnumParticle == null) {
          nmsEnumParticle = ReflectionUtilities.getNMSClass("EnumParticle");
        }
        if (this == BLOCK_CRACK) {
          int id = 0;
          int data = 0;
          if (extra.length > 0) {
            id = extra[0];
          }
          if (extra.length > 1) {
            data = extra[1];
          }
          extra = new int[] { id, id | data << 12 };
        }
        EnumParticle enumParticle = (EnumParticle) getEnum(nmsEnumParticle.getName() + "." + (this.enumValue != null ? this.enumValue : name().toUpperCase()));
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(enumParticle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, count, extra);
        ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.write(packet); //skips some checks and is faster because we don't flush 
      }
      catch (Exception e) {
        throw e;
      }
    else
      try {
        if (this.particleName == null) {
          this.particleName = name().toLowerCase();
        }
        String name = this.particleName;
        if ((this == BLOCK_CRACK) || (this == ITEM_CRACK) || (this == BLOCK_DUST)) {
          int id = 0;
          int data = 0;
          if (extra.length > 0) {
            id = extra[0];
          }
          if (extra.length > 1) {
            data = extra[1];
          }
          name = name + id + "_" + data;
        }
        Object packet = nmsPacketPlayOutParticle.getConstructor(new Class[] { String.class, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE }).newInstance(new Object[] { name, Float.valueOf((float)location.getX()), Float.valueOf((float)location.getY()), Float.valueOf((float)location.getZ()), Float.valueOf(offsetX), Float.valueOf(offsetY), Float.valueOf(offsetZ), Float.valueOf(speed), Integer.valueOf(count) });
        Object handle = ReflectionUtilities.getHandle(player);
        Object connection = ReflectionUtilities.getField(handle.getClass(), "playerConnection").get(handle);
        ReflectionUtilities.getMethod(connection.getClass(), "sendPacket", new Class[0]).invoke(connection, new Object[] { packet });
      }
      catch (Exception e) {
        throw e;
      }
  }

  public void display(float speed, int amount, Location center, double range) throws IllegalArgumentException {
	  	setRange(UtilNumber.toInt(range));
		try {
			for(Player player : UtilServer.getPlayers())sendToPlayer(player, center, 0, 0, 0, speed, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
  }

  public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) throws IllegalArgumentException {
	  setRange(UtilNumber.toInt(range));
		try {
			for(Player player : UtilServer.getPlayers())sendToPlayer(player, center, offsetX, offsetY, offsetZ, speed, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
  }

  public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) throws IllegalArgumentException {
		try {
			for(Player player : players)sendToPlayer(player, center, offsetX, offsetY, offsetZ, speed, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
  }
  
  public void sendToPlayer(Player player, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count)
    throws Exception
  {
    sendToPlayer(player, location, offsetX, offsetY, offsetZ, speed, count, new int[0]);
  }

  public void sendToPlayers(Collection<? extends Player> players, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count)
    throws Exception
  {
    for (Player p : players)
      sendToPlayer(p, location, offsetX, offsetY, offsetZ, speed, count);
  }

  public void sendToPlayers(Player[] players, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count)
    throws Exception
  {
    for (Player p : players)
      sendToPlayer(p, location, offsetX, offsetY, offsetZ, speed, count);
  }

  public void sendColor(Player p, Location location, org.bukkit.Color color)
    throws Exception
  {
    if (!this.hasColor) return;
    sendToPlayer(p, location, getColor(color.getRed()), getColor(color.getGreen()), getColor(color.getBlue()), 1.0F, 0);
  }

  public void sendColor(Player p, Location location, java.awt.Color color)
    throws Exception
  {
    if (!this.hasColor) return;
    sendToPlayer(p, location, getColor(color.getRed()), getColor(color.getGreen()), getColor(color.getBlue()), 1.0F, 0);
  }

  public void sendColor(Collection<? extends Player> players, Location location, java.awt.Color color)
    throws Exception
  {
    if (!this.hasColor) return;
    for (Player p : players)
      sendColor(p, location, color);
  }

  public void sendColor(Collection<? extends Player> players, Location location, org.bukkit.Color color)
    throws Exception
  {
    if (!this.hasColor) return;
    for (Player p : players)
      sendColor(p, location, color);
  }

  public void sendColor(Player[] players, Location location, org.bukkit.Color color)
    throws Exception
  {
    if (!this.hasColor) return;
    for (Player p : players)
      sendColor(p, location, color);
  }

  public void sendColor(Player[] players, Location location, java.awt.Color color)
    throws Exception
  {
    if (!this.hasColor) return;
    for (Player p : players)
      sendColor(p, location, color);
  }

  protected float getColor(float value)
  {
    if (value <= 0.0F) {
      value = -1.0F;
    }
    return value / 255.0F;
  }

  public void sendBlockCrack(Player player, Location location, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int count)
    throws Exception
  {
    if (this != BLOCK_CRACK) throw new IllegalArgumentException("This method is only available for BLOCK_CRACK!");
    sendToPlayer(player, location, offsetX, offsetY, offsetZ, speed, count, new int[] { id, data });
  }

  public void sendBlockCrack(Collection<? extends Player> players, Location location, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int count)
    throws Exception
  {
    if (this != BLOCK_CRACK) throw new IllegalArgumentException("This method is only available for BLOCK_CRACK!");
    for (Player p : players)
      sendBlockCrack(p, location, id, data, offsetX, offsetY, offsetZ, speed, count);
  }

  public void sendBlockCrack(Player[] players, Location location, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int count)
    throws Exception
  {
    if (this != BLOCK_CRACK) throw new IllegalArgumentException("This method is only available for BLOCK_CRACK!");
    for (Player p : players)
      sendBlockCrack(p, location, id, data, offsetX, offsetY, offsetZ, speed, count);
  }

  public void sendItemCrack(Player player, Location location, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int count)
    throws Exception
  {
    if (this != ITEM_CRACK) throw new IllegalArgumentException("This method is only available for ITEM_CRACK!");
    sendToPlayer(player, location, offsetX, offsetY, offsetZ, speed, count, new int[] { id, data });
  }

  public void sendItemCrack(Collection<? extends Player> players, Location location, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int count)
    throws Exception
  {
    if (this != ITEM_CRACK) throw new IllegalArgumentException("This method is only available for ITEM_CRACK!");
    for (Player p : players)
      sendItemCrack(p, location, id, data, offsetX, offsetY, offsetZ, speed, count);
  }

  public void sendItemCrack(Player[] players, Location location, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int count)
    throws Exception
  {
    if (this != ITEM_CRACK) throw new IllegalArgumentException("This method is only available for ITEM_CRACK!");
    for (Player p : players)
      sendItemCrack(p, location, id, data, offsetX, offsetY, offsetZ, speed, count);
  }

  public void sendBlockDust(Player p, Location location, int id, float offsetX, float offsetY, float offsetZ, float speed, int count)
    throws Exception
  {
    if (this != BLOCK_DUST) throw new IllegalArgumentException("This method is only available for BLOCK_DUST!");
    sendToPlayer(p, location, offsetX, offsetY, offsetZ, speed, count, new int[] { id });
  }

  public void sendBlockDust(Collection<? extends Player> players, Location location, int id, float offsetX, float offsetY, float offsetZ, float speed, int count)
    throws Exception
  {
    if (this != BLOCK_DUST) throw new IllegalArgumentException("This method is only available for BLOCK_DUST!");
    for (Player p : players)
      sendBlockDust(p, location, id, offsetX, offsetY, offsetZ, speed, count);
  }

  public void sendBlockDust(Player[] players, Location location, int id, float offsetX, float offsetY, float offsetZ, float speed, int count)
    throws Exception
  {
    if (this != BLOCK_DUST) throw new IllegalArgumentException("This method is only available for BLOCK_DUST!");
    for (Player p : players)
      sendBlockDust(p, location, id, offsetX, offsetY, offsetZ, speed, count);
  }

  protected static int getVersion(Player p)
  {
    try
    {
      Object handle = ReflectionUtilities.getHandle(p);
      Object connection = nmsFieldPlayerConnection.get(handle);
      Object network = nmsFieldNetworkManager.get(connection);
      Object channel;
      if (ReflectionUtilities.getVersion().contains("1_7"))
        channel = nmsFieldNetworkManagerM.get(network);
      else {
        channel = nmsFieldNetworkManagerI.get(network);
      }
      Object version = ReflectionUtilities.getVersion().contains("1_7") ? nmsNetworkGetVersion.invoke(network, new Object[] { channel }) : Integer.valueOf(47);
      return ((Integer)version).intValue();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  private static Enum<?> getEnum(String enumFullName)
  {
    String[] x = enumFullName.split("\\.(?=[^\\.]+$)");
    if (x.length == 2) {
      String enumClassName = x[0];
      String enumName = x[1];
      try {
        Class cl = Class.forName(enumClassName);
        return Enum.valueOf(cl, enumName);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  private static boolean isPlayerInRange(Player p, Location center) {
    double distance = 0.0D;
    if (p.getLocation().getWorld().getUID()!=center.getWorld().getUID())return false;
    if ((distance = center.distanceSquared(p.getLocation())) > 1.7976931348623157E+308D)return false;
    return distance < particleRange * particleRange;
  }

  public static class ReflectionUtilities
  {
    public static void setValue(Object instance, String fieldName, Object value)
      throws Exception
    {
      Field field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(instance, value);
    }

    public static Object getValue(Object instance, String fieldName)
      throws Exception
    {
      Field field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(instance);
    }

    public static String getVersion() {
      String name = Bukkit.getServer().getClass().getPackage().getName();
      String version = name.substring(name.lastIndexOf('.') + 1) + ".";
      return version;
    }

    public static Class<?> getNMSClass(String className) {
      String fullName = "net.minecraft.server." + getVersion() + className;
      Class clazz = null;
      try {
        clazz = Class.forName(fullName);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return clazz;
    }

    public static Class<?> getOBCClass(String className) {
      String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
      Class clazz = null;
      try {
        clazz = Class.forName(fullName);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return clazz;
    }

    public static Object getHandle(Object obj) {
      try {
        return getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj, new Object[0]);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    public static Field getField(Class<?> clazz, String name) {
      try {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>[] args) {
      for (Method m : clazz.getMethods()) {
        if ((m.getName().equals(name)) && ((args.length == 0) || (ClassListEqual(args, m.getParameterTypes())))) {
          m.setAccessible(true);
          return m;
        }
      }
      return null;
    }

    public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
      boolean equal = true;
      if (l1.length != l2.length) return false;
      for (int i = 0; i < l1.length; i++) {
        if (l1[i] != l2[i]) {
          equal = false;
          break;
        }
      }
      return equal;
    }
  }
}
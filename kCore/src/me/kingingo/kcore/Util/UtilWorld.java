package me.kingingo.kcore.Util;
import java.io.File;
import java.lang.reflect.Field;

import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R3.Convertable;
import net.minecraft.server.v1_8_R3.EntityTracker;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.IProgressUpdate;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import net.minecraft.server.v1_8_R3.ServerNBTManager;
import net.minecraft.server.v1_8_R3.WorldBorder;
import net.minecraft.server.v1_8_R3.WorldData;
import net.minecraft.server.v1_8_R3.WorldLoaderServer;
import net.minecraft.server.v1_8_R3.WorldManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.server.v1_8_R3.WorldSettings;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
import net.minecraft.server.v1_8_R3.WorldType;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.spigotmc.SpigotConfig;

public class UtilWorld{
	
	public static void sendWorldBorder(Player p, double midX, double midZ, double oldRadius, double newRadius, int seconds, int portalteleportboundary, int warningTime, int warningBlocks)
	  {
	    WorldBorder w = new WorldBorder();
	    w.setCenter(midX, midZ);
	    w.transitionSizeBetween(oldRadius, newRadius, seconds*TimeSpan.SECOND);
	    if (warningTime >= 0) w.setWarningTime(warningTime);
	    if (warningBlocks >= 0) w.setWarningDistance(warningBlocks);
	    if (portalteleportboundary >= 0) w.a(portalteleportboundary);
	    UtilPlayer.sendPacket(p, new kPacketPlayOutWorldBorder(w, EnumWorldBorderAction.INITIALIZE));
	  }

	  public static void setWorldBorderCenter(Player p, double midX, double midZ, double radius) {
	    WorldBorder w = new WorldBorder();
	    w.setCenter(midX, midZ);
	    w.setSize(radius);
	    UtilPlayer.sendPacket(p, new kPacketPlayOutWorldBorder(w, EnumWorldBorderAction.SET_CENTER));
	  }

	  public static void setWorldBorderSize(Player p, double radius) {
	    WorldBorder w = new WorldBorder();
	    w.setSize(radius);
	    UtilPlayer.sendPacket(p, new kPacketPlayOutWorldBorder(w, EnumWorldBorderAction.SET_SIZE));
	  }

	  public static void changeWorldBorderSize(Player p, double oldRadius, double newRadius, int seconds) {
	    WorldBorder w = new WorldBorder();
	    w.transitionSizeBetween(oldRadius, newRadius, seconds*TimeSpan.SECOND);
	    UtilPlayer.sendPacket(p, new kPacketPlayOutWorldBorder(w, EnumWorldBorderAction.LERP_SIZE));
	  }

	  public static void setWorldBorderWarningTime(Player p, int warningTimeInSeconds) {
	    WorldBorder w = new WorldBorder();
	    w.setWarningTime(warningTimeInSeconds);
	    UtilPlayer.sendPacket(p, new kPacketPlayOutWorldBorder(w, EnumWorldBorderAction.SET_WARNING_TIME));
	  }

	  public static void setWorldBorderWarningBlocks(Player p, int warningBlocksDistance) {
	    WorldBorder w = new WorldBorder();
	    w.setWarningDistance(warningBlocksDistance);
	    UtilPlayer.sendPacket(p, new kPacketPlayOutWorldBorder(w, EnumWorldBorderAction.SET_WARNING_BLOCKS));
	  }

//	  public static void sendBlood(Player p, int strength, int seconds) {
//	    sendWorldBorder(p, p.getLocation().getX(), p.getLocation().getZ(), 10000.0D, getDistance(strength) + 10, seconds, -1, seconds, getDistance(strength));
//	    new BukkitSyncAutoTask(seconds * 20 + 5)
//	    {
//	      public void run() {
//	        WorldBorderManager.setWorldBorderSize(this.val$p, 60000000.0D);
//	      }
//	    };
//	  }

	  private static int getDistance(int blood) {
	    switch (blood) {
	    case 0:
	      return 10000;
	    case 1:
	      return 15000;
	    case 2:
	      return 24000;
	    case 3:
	      return 29000;
	    case 4:
	      return 35000;
	    case 5:
	      return 40000;
	    }
	    return 10000;
	  }

	  public static int getMaxStrength()
	  {
	    return 5;
	  }
	
	public static void setSave(boolean b){
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-"+(b ? "on" : "off"));
	}
	
	public void setWorldUnSave(World w){
		WorldServer ws = ((CraftWorld)w).getHandle();
		try{
			Field field = ws.getClass().getDeclaredField("savingDisabled");
			field.setAccessible(true);
			field.set(ws, "false");
		}catch(Exception e){
			System.err.println("[WorldUtil] Error: "+e.getLocalizedMessage());
		}
	}
	
  public static World LoadWorld(WorldCreator creator){
	  return LoadWorld(creator,null);
  }
	
  public static World LoadWorld(WorldCreator creator,ChunkGenerator generator)
  {
    CraftServer server = (CraftServer)Bukkit.getServer();
    if (creator == null)
    {
      throw new IllegalArgumentException("Creator may not be null");
    }

    String name = creator.name();
    System.out.println("Loading world '" + name + "'");
    if(generator==null)generator = creator.generator();
    File folder = new File(server.getWorldContainer(), name);
    World world = server.getWorld(name);
    WorldType type = WorldType.getType(creator.type().getName());
    boolean generateStructures = creator.generateStructures();

    if (world != null)
    {
      return world;
    }

    if ((folder.exists()) && (!folder.isDirectory()))
    {
      throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
    }

    if (generator == null)
    {
      generator = server.getGenerator(name);
    }

    Convertable converter = new WorldLoaderServer(server.getWorldContainer());
    if (converter.isConvertable(name))
    {
      server.getLogger().info("Converting world '" + name + "'");
      converter.convert(name, new IProgressUpdate() {
          private long b = System.currentTimeMillis();

          public void a(String s) {
          }
          public void a(int i) {
            if (System.currentTimeMillis() - this.b >= 1000L) {
              this.b = System.currentTimeMillis();
              MinecraftServer.LOGGER.info("Converting... " + i + "%");
            }
          }

          public void c(String s)
          {
          }
        });
    }

    int dimension = server.getServer().worlds.size() + 1;
    boolean used = false;
    do
    {
      for (WorldServer worldServer : server.getServer().worlds)
      {
        used = worldServer.dimension == dimension;
        if (used)
        {
          dimension++;
          break;
        }
      }
    }
    while (used);
    boolean hardcore = false;

    //WorldServer internal = new WorldServer(server.getServer(), new ServerNBTManager(server.getWorldContainer(), name, true), name, dimension, new WorldSettings(creator.seed(), EnumGamemode.SURVIVAL, generateStructures, hardcore, type), server.getServer().methodProfiler, creator.environment(), generator);
    WorldSettings setting = new WorldSettings(creator.seed(), EnumGamemode.SURVIVAL, generateStructures, hardcore, type);
    setting.setGeneratorSettings(creator.generatorSettings());
    WorldData wd = new WorldData(setting, name);
    wd.checkName(name);
    WorldServer internal = (WorldServer)new WorldServer(server.getServer(), new ServerNBTManager(server.getWorldContainer(), name, true), wd, dimension, server.getServer().methodProfiler, creator.environment(), generator).b();
    
    boolean containsWorld = false;
    for (World otherWorld : server.getWorlds())
    {
      if (otherWorld.getName().equalsIgnoreCase(name.toLowerCase()))
      {
        containsWorld = true;
        break;
      }
    }

    if (!containsWorld) {
      return null;
    }
    
    System.out.println("Created world with dimension : " + dimension);

//    internal.scoreboard = server.getScoreboardManager().getMainScoreboard().getHandle();
//    internal.worldMaps = ((WorldServer)server.getServer().worlds.get(0)).worldMaps;
//    internal.tracker = new EntityTracker(internal);
//    internal.addIWorldAccess(new WorldManager(server.getServer(), internal));
//    internal.getWorldData().setDifficulty(EnumDifficulty.HARD);
//    internal.setSpawnFlags(true, true);
//    server.getServer().worlds.add(internal);
    
    internal.scoreboard = server.getScoreboardManager().getMainScoreboard().getHandle();

    internal.tracker = new EntityTracker(internal);
    internal.addIWorldAccess(new WorldManager(server.getServer(), internal));
    internal.worldData.setDifficulty(EnumDifficulty.EASY);
    internal.setSpawnFlags(true, true);
    server.getServer().worlds.add(internal);

    if (generator != null)
    {
      internal.getWorld().getPopulators().addAll(generator.getDefaultPopulators(internal.getWorld()));
    }

    server.getPluginManager().callEvent(new WorldInitEvent(internal.getWorld()));
    server.getPluginManager().callEvent(new WorldLoadEvent(internal.getWorld()));

    return internal.getWorld();
  }
}
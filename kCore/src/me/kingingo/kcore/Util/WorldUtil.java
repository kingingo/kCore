package me.kingingo.kcore.Util;
import java.io.File;
import java.lang.reflect.Field;

import net.minecraft.server.v1_8_R2.Convertable;
import net.minecraft.server.v1_8_R2.EntityTracker;
import net.minecraft.server.v1_8_R2.EnumDifficulty;
import net.minecraft.server.v1_8_R2.IProgressUpdate;
import net.minecraft.server.v1_8_R2.MinecraftServer;
import net.minecraft.server.v1_8_R2.ServerNBTManager;
import net.minecraft.server.v1_8_R2.WorldData;
import net.minecraft.server.v1_8_R2.WorldLoaderServer;
import net.minecraft.server.v1_8_R2.WorldManager;
import net.minecraft.server.v1_8_R2.WorldServer;
import net.minecraft.server.v1_8_R2.WorldSettings;
import net.minecraft.server.v1_8_R2.WorldSettings.EnumGamemode;
import net.minecraft.server.v1_8_R2.WorldType;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_8_R2.CraftServer;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.spigotmc.SpigotConfig;

public class WorldUtil{
	
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
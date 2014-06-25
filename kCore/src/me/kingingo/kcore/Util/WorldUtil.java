package me.kingingo.kcore.Util;
import java.io.File;

import net.minecraft.server.v1_7_R3.ConvertProgressUpdater;
import net.minecraft.server.v1_7_R3.Convertable;
import net.minecraft.server.v1_7_R3.EntityTracker;
import net.minecraft.server.v1_7_R3.EnumDifficulty;
import net.minecraft.server.v1_7_R3.EnumGamemode;
import net.minecraft.server.v1_7_R3.ServerNBTManager;
import net.minecraft.server.v1_7_R3.WorldLoaderServer;
import net.minecraft.server.v1_7_R3.WorldManager;
import net.minecraft.server.v1_7_R3.WorldServer;
import net.minecraft.server.v1_7_R3.WorldSettings;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_7_R3.CraftServer;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;

public class WorldUtil
{
  public static World LoadWorld(WorldCreator creator)
  {
    CraftServer server = (CraftServer)Bukkit.getServer();
    if (creator == null)
    {
      throw new IllegalArgumentException("Creator may not be null");
    }

    String name = creator.name();
    System.out.println("Loading world '" + name + "'");
    ChunkGenerator generator = creator.generator();
    File folder = new File(server.getWorldContainer(), name);
    World world = server.getWorld(name);
    net.minecraft.server.v1_7_R3.WorldType type = net.minecraft.server.v1_7_R3.WorldType.getType(creator.type().getName());
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
      converter.convert(name, new ConvertProgressUpdater(server.getServer()));
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

    WorldServer internal = new WorldServer(server.getServer(), new ServerNBTManager(server.getWorldContainer(), name, true), name, dimension, new WorldSettings(creator.seed(), EnumGamemode.a(server.getDefaultGameMode().getValue()), generateStructures, hardcore, type), server.getServer().methodProfiler, creator.environment(), generator);

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

    internal.scoreboard = server.getScoreboardManager().getMainScoreboard().getHandle();
    internal.worldMaps = ((WorldServer)server.getServer().worlds.get(0)).worldMaps;
    internal.tracker = new EntityTracker(internal);
    internal.addIWorldAccess(new WorldManager(server.getServer(), internal));
    internal.difficulty = EnumDifficulty.HARD;
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
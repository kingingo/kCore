package me.kingingo.kcore.Util;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.server.v1_7_R3.MinecraftServer;
import net.minecraft.server.v1_7_R3.RegionFile;
import net.minecraft.server.v1_7_R3.RegionFileCache;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R3.CraftServer;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class UtilMap{

	public static boolean ClearWorldReferences(String worldName)
	  {
	    HashMap regionfiles = null;
	    Field rafField = null;
	    try
	    {
	      Field a = RegionFileCache.class.getDeclaredField("a");
	      a.setAccessible(true);
	      regionfiles = (HashMap)a.get(null);
	      rafField = RegionFile.class.getDeclaredField("c");
	      rafField.setAccessible(true);
	    }
	    catch (Throwable t)
	    {
	      System.out.println("Error binding to region file cache.");
	      t.printStackTrace();
	    }

	    if (regionfiles == null) return false;
	    if (rafField == null) return false;

	    ArrayList removedKeys = new ArrayList();
	    try
	    {
	      //for (localIterator = regionfiles.entrySet().iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();
	    	for(Object o : regionfiles.keySet()){
	        Map.Entry e = (Map.Entry)o;
	        File f = (File)e.getKey();

	        if (f.toString().startsWith("." + File.separator + worldName))
	        {
	          RegionFile file = (RegionFile)e.getValue();
	          try
	          {
	            RandomAccessFile raf = (RandomAccessFile)rafField.get(file);
	            raf.close();
	            removedKeys.add(f);
	          }
	          catch (Exception ex)
	          {
	            ex.printStackTrace();
	          }
	        }
	      }
	    }
	    catch (Exception ex)
	    {
	      System.out.println("Exception while removing world reference for '" + worldName + "'!");
	      ex.printStackTrace();
	    }

	    for (Iterator localIterator = removedKeys.iterator(); localIterator.hasNext(); ) { Object key = localIterator.next();
	      regionfiles.remove(key);
	    }
	    return true;
	  }
	
	public static void UnloadWorld(JavaPlugin plugin, org.bukkit.World world)
	  {
	    world.setAutoSave(false);

	    for (Entity entity : world.getEntities())
	    {
	      entity.remove();
	    }

	    CraftServer server = (CraftServer)plugin.getServer();
	    CraftWorld craftWorld = (CraftWorld)world;

	    Bukkit.getPluginManager().callEvent(new WorldUnloadEvent(((CraftWorld)world).getHandle().getWorld()));

	    Iterator chunkIterator = ((CraftWorld)world).getHandle().chunkProviderServer.chunks.values().iterator();

	    while (chunkIterator.hasNext())
	    {
	      net.minecraft.server.v1_7_R3.Chunk chunk = (net.minecraft.server.v1_7_R3.Chunk)chunkIterator.next();
	      chunk.removeEntities();
	    }

	    ((CraftWorld)world).getHandle().chunkProviderServer.chunks.clear();
	    ((CraftWorld)world).getHandle().chunkProviderServer.unloadQueue.clear();
	    try
	    {
	      Field f = server.getClass().getDeclaredField("worlds");
	      f.setAccessible(true);

	      Map worlds = (Map)f.get(server);
	      worlds.remove(world.getName().toLowerCase());
	      f.setAccessible(false);
	    }
	    catch (IllegalAccessException ex)
	    {
	      System.out.println("Error removing world from bukkit master list: " + ex.getMessage());
	    }
	    catch (NoSuchFieldException ex)
	    {
	      System.out.println("Error removing world from bukkit master list: " + ex.getMessage());
	    }

	    MinecraftServer ms = null;
	    try
	    {
	      Field f = server.getClass().getDeclaredField("console");
	      f.setAccessible(true);
	      ms = (MinecraftServer)f.get(server);
	      f.setAccessible(false);
	    }
	    catch (IllegalAccessException ex)
	    {
	      System.out.println("Error getting minecraftserver variable: " + ex.getMessage());
	    }
	    catch (NoSuchFieldException ex)
	    {
	      System.out.println("Error getting minecraftserver variable: " + ex.getMessage());
	    }

	    ms.worlds.remove(ms.worlds.indexOf(craftWorld.getHandle()));
	  }
	
}
package me.kingingo.kcore.Util;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_8_R2.MinecraftServer;
import net.minecraft.server.v1_8_R2.RegionFile;
import net.minecraft.server.v1_8_R2.RegionFileCache;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R2.CraftServer;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class UtilMap{

	public static void setCrystals(Location loc, int radius, int high){
		int h = 5000;
	      for (int i = 0; i <= 150; i++) {
	        h -= 100;
	        setCrystalCricle(loc, 25, h);
	        if (h == 400)
	        {
	          break;
	        }
	      }
	  }
	
	public static void setCrystalCricle(Location loc,int radius, int high){
		 	int cx = loc.getBlockX();
	        int cy = high;
	        int cz = loc.getBlockZ();
	        int r = 40;
	        int rSquared = r * r;
		    int zufall = 0;
		    Location cylBlock;
	        for (int x = cx - r; x <= cx + r; x++) {
	            for (int z = cz - r; z <= cz + r; z++) {
	                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
	                    cylBlock = new Location(loc.getWorld(), x, cy, z);
	        	        zufall = (int)(1.0D + (Math.random() * 10.0D - 1.0D));
	                    if (zufall == 1) {
	                    	cylBlock.getChunk().load();
	          	          Entity en = cylBlock.getWorld().spawnEntity(cylBlock,EntityType.ENDER_CRYSTAL);
	          	          en.teleport(cylBlock);
	          	        } 
	                }
	            }
	        }
	}
	
	public static void setCrystalQuadrat(Location loc, int radius, int high){
	    int y = high;
	    int minx = loc.getBlockX() - radius;
	    int minz = loc.getBlockZ() - radius;
	    int maxx = loc.getBlockX() + radius;
	    int maxz = loc.getBlockZ() + radius;
	    int zufall = 0;
	    Location b = new Location(loc.getWorld(), 0.0D, 0.0D, 0.0D);
	    for (int x = minx; x < maxx; x++)
	      for (int z = minz; z < maxz; z++) {
	        b = new Location(loc.getWorld(), x, y, z);
	        zufall = (int)(1.0D + (Math.random() * 10.0D - 1.0D));
	        if (zufall == 1) {
	          b.getChunk().load();
	          Entity en = b.getWorld().spawnEntity(b,EntityType.ENDER_CRYSTAL);
	          en.teleport(b);
	        }
	      }
	  }
	
	public static int MinZ(Location start, int r) {
		return start.getBlockZ() - (r);
	}

	public static int MinX(Location start, int r) {
		return start.getBlockX() - (r);
	}

	public static int MaxZ(Location start, int r) {
		return start.getBlockZ() + (r);
	}

	public static int MaxX(Location start, int r) {
		return start.getBlockX() + (r);
	}
	
	public static int MaxZ(Location e,Location e1){
		if(e.getBlockZ()<e1.getBlockZ()){
			return e1.getBlockZ();
		}else{
			return e.getBlockZ();
		}
	}
	
	public static int MinZ(Location e,Location e1){
		if(e.getBlockZ()>e1.getBlockZ()){
			return e1.getBlockZ();
		}else{
			return e.getBlockZ();
		}
	}
	
	public static int MaxY(Location e,Location e1){
		if(e.getBlockY()<e1.getBlockY()){
			return e1.getBlockY();
		}else{
			return e.getBlockY();
		}
	}
	
	public static int MinY(Location e,Location e1){
		if(e.getBlockY()>e1.getBlockY()){
			return e1.getBlockY();
		}else{
			return e.getBlockY();
		}
	}
	
	public static int MaxX(Location e,Location e1){
		if(e.getBlockX()<e1.getBlockX()){
			return e1.getBlockX();
		}else{
			return e.getBlockX();
		}
	}
	
	public static int MinX(Location e,Location e1){
		if(e.getBlockX()>e1.getBlockX()){
			return e1.getBlockX();
		}else{
			return e.getBlockX();
		}
	}
	
	public static void QuadratWalls(Location ecke1,Location ecke2,Material material){
		System.out.println("MinX: "+MinX(ecke1, ecke2)+" MaxX: "+MaxX(ecke1, ecke2));
		System.out.println("MinY: "+MinY(ecke1, ecke2)+" MaxY: "+MaxY(ecke1, ecke2));
		System.out.println("MinZ: "+MinZ(ecke1, ecke2)+" MaxZ: "+MaxZ(ecke1, ecke2));
		int MaxX = MaxX(ecke1,ecke2);
		int MaxZ = MaxZ(ecke1,ecke2);
		int MinX = MinX(ecke1,ecke2);
		int MinZ = MinZ(ecke1,ecke2);
		int MinY = MinY(ecke1, ecke2);
		int MaxY = MaxY(ecke1, ecke2);
		
		for(int x = MinX; x<MaxX; x++){
			for(int z = MinZ; z<MaxZ; z++){
				new Location(ecke1.getWorld(), x, MinY, z).getBlock().setType(material);
				new Location(ecke1.getWorld(), x, MaxY, z).getBlock().setType(material);
			}
		}
		
		for (int y = MinY; y < MaxY; y++) {
			for (int z = MinZ; z < MaxZ; z++) {
				new Location(ecke1.getWorld(), MaxX, y, z).getBlock().setType(material);
			}

			for (int z = MaxZ; z > MinZ; z--) {
				new Location(ecke1.getWorld(), MinX, y, z).getBlock().setType(material);
			}
			
			for (int x = MaxX; x > MinX; x--) {
				new Location(ecke1.getWorld(), x, y, MaxZ).getBlock().setType(material);
			}

			for (int x = MinX; x < MaxX; x++) {
				new Location(ecke1.getWorld(), x, y, MinZ).getBlock().setType(material);
			}
		}
	}
	
	public static void Quadrat(Location ecke1,Location ecke2,Material[] materials){
		for(int x=MinX(ecke1, ecke2); x<MaxX(ecke1, ecke2); x++){
			for(int z=MinZ(ecke1, ecke2); z<MaxZ(ecke1, ecke2); z++){
				for(int y=MinY(ecke1, ecke2); y<MaxY(ecke1, ecke2); y++){
					ecke1.getWorld().getBlockAt(x, y, z).setType(materials[UtilMath.r(materials.length)]);;
				}
			}
		}
	}
	
	public static List<Location> Quadrat(Location loc, int r) {
		List<Location> list = new ArrayList<Location>();
		int MaxX = MaxX(loc.clone(), r);
		int MaxZ = MaxZ(loc.clone(), r);
		int MinX = MinX(loc.clone(), r);
		int MinZ = MinZ(loc.clone(), r);
		int MinY = 0;
		int MaxY = 255;

		for (int y = MinY; y < MaxY; y++) {
			for (int z = MinZ; z < MaxZ; z++) {
				Location l = new Location(loc.getWorld(), MaxX, y, z);
				if (l.getBlock().getType() == Material.AIR)
					list.add(l);
			}

			for (int z = MaxZ; z > MinZ; z--) {
				Location l = new Location(loc.getWorld(), MinX, y, z);
				if (l.getBlock().getType() == Material.AIR)
					list.add(l);
			}
			
			for (int x = MaxX; x > MinX; x--) {
				Location l = new Location(loc.getWorld(), x, y, MaxZ);
				if (l.getBlock().getType() == Material.AIR)
					list.add(l);
			}

			for (int x = MinX; x < MaxX; x++) {
				Location l = new Location(loc.getWorld(), x, y, MinZ);
				if (l.getBlock().getType() == Material.AIR)
					list.add(l);
			}
		}

		return list;
	}
	
	public static void loadChunks(Location location,int radius){
		int MaxX = MaxX(location.clone(), radius);
		int MaxZ = MaxZ(location.clone(), radius);
		int MinX = MinX(location.clone(), radius);
		int MinZ = MinZ(location.clone(), radius);
		Location loc=new Location(location.getWorld(),0,0,0);
		
		for (int z = MinZ; z < MaxZ; z++) {
			for (int x = MinX; x < MaxX; x++) {
				loc.setX(location.getX());
				loc.setZ(z);
				loc.getWorld().loadChunk(loc.getChunk());
			}
		}
	}
	
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
	      net.minecraft.server.v1_8_R2.Chunk chunk = (net.minecraft.server.v1_8_R2.Chunk)chunkIterator.next();
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
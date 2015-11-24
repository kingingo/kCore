package me.kingingo.kcore.Util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import net.minecraft.server.v1_8_R3.MapIcon;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutMap;
import net.minecraft.server.v1_8_R3.RegionFile;
import net.minecraft.server.v1_8_R3.RegionFileCache;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.map.RenderData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

public class UtilMap{
	
	public static HashMap<Integer,ArrayList<EntityEnderCrystal>> list_entitys = new HashMap<>();
	
	public static File getMapPicture(){
		File folder = new File("pictures");
		if(!folder.exists())folder.mkdirs();
		return folder.listFiles()[UtilMath.r(folder.listFiles().length)];
	}
	
	public static File[] getMapPictures(){
		File folder = new File("pictures");
		if(!folder.exists())folder.mkdirs();
		return folder.listFiles();
	}
	
	public static PacketPlayOutMap getMap(File file){
		RenderData data = getRenderData(file);
		
		 Collection icons = new ArrayList();
		    for (MapCursor cursor : data.cursors) {
		      if (cursor.isVisible()) {
		        icons.add(new MapIcon(cursor.getRawType(), cursor.getX(), cursor.getY(), cursor.getDirection()));
		      }
		    }

		return new PacketPlayOutMap(Bukkit.getMap((short)0).getId(), Bukkit.getMap((short)0).getScale().getValue(), icons, data.buffer, 0, 0, 0, 0);
	}
	
	public static RenderData getRenderData(File file) {
		Image image = null;
	    try {
	        image = ImageIO.read(file);
	    } catch (IOException exc) {
	        exc.printStackTrace();
	    }
		RenderData render = new RenderData();
		MapRenderer mapRenderer = new ImageMapRenderer(image);
		
		Arrays.fill(render.buffer, (byte)0);
		render.cursors.clear();
		
		FakeMapCanvas canvas = new FakeMapCanvas();
		canvas.setBase(render.buffer);
		mapRenderer.render(canvas.getMapView(), canvas, null);
		
		byte[] buf = canvas.getBuffer();
		for (int i = 0; i < buf.length; i++) {
			byte color = buf[i];
			if ((color >= 0) || (color <= -113)) render.buffer[i] = color;
		}
		
		return render;
	}
	
	public static MapView MapRender(String pictureName){
		return MapRender(new File("pictures"+File.separator+pictureName+".png"));
	}
	
	public static MapView MapRender(File file){
		Image image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        
        MapView v = Bukkit.getMap((short)0);
        if(v==null){
        	System.out.println("V==NULL");
        	v=Bukkit.createMap(Bukkit.getWorlds().get(0));
        }
        
        if(v!=null&&!v.getRenderers().isEmpty()){
        	 for (MapRenderer renderer : v.getRenderers()){
                 v.removeRenderer(renderer);
             }
        }
       
        v.addRenderer(new ImageMapRenderer(image));
        
        return v;
	}
	
	public static void loadParticle(HashMap<Integer, ArrayList<Location>> list,Location loc, int r){
		int cx = loc.getBlockX();
        int cy = 5000;
        int cz = loc.getBlockZ();
        int rSquared = r * r;
	    int zufall = 0;
	    Location cylBlock;
		
	      for (int i = 0; i <= 150; i++) {
	    	cy -= 100;
	        cx = loc.getBlockX();
	        cz = loc.getBlockZ();
		    zufall = 0;
		    list.put(cy, new ArrayList<Location>());
	        
	        for (int x = cx - r; x <= cx + r; x++) {
	            for (int z = cz - r; z <= cz + r; z++) {
	                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
	                    cylBlock = new Location(loc.getWorld(), x, cy, z);
	        	        zufall = (int)(1.0D + (Math.random() * 10.0D - 1.0D));
	                    if (zufall == 1) {
	                    	list.get(cy).add(cylBlock);
	          	        } 
	                }
	            }
	        }
	        
	        if (cy == 400)
	        {
	          break;
	        }
	      }
	}
	
	public static void setCrystals(Location loc, int radius){
		int h = 5000;
	      for (int i = 0; i <= 150; i++) {
	        h -= 100;
	        setCrystalQuadrat(loc, 25, i);
	        if (h == 400)
	        {
	          break;
	        }
	      }
	  }
	
	public static void spawnEnderCrystal(Location loc){		
		CraftWorld cw = (CraftWorld) loc.getWorld(); 
		EntityEnderCrystal e = new EntityEnderCrystal(cw.getHandle()); 
		e.setPosition(loc.getX(), loc.getY(), loc.getZ());
		if(!list_entitys.containsKey(loc.getBlockY()))list_entitys.put(loc.getBlockY(), new ArrayList<EntityEnderCrystal>());
		list_entitys.get(loc.getBlockY()).add(e);
	}
	
	public static void setCrystalCricle(Location loc,int r, int high){
		 	int cx = loc.getBlockX();
	        int cy = high;
	        int cz = loc.getBlockZ();
	        int rSquared = r * r;
		    int zufall = 0;
		    Entity en;
		    Location cylBlock;
	        for (int x = cx - r; x <= cx + r; x++) {
	            for (int z = cz - r; z <= cz + r; z++) {
	                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
	                    cylBlock = new Location(loc.getWorld(), x, cy, z);
	        	        zufall = (int)(1.0D + (Math.random() * 10.0D - 1.0D));
	                    if (zufall == 1) {
	                    	cylBlock.getChunk().load();
	                    	en = cylBlock.getWorld().spawnEntity(cylBlock,EntityType.ENDER_CRYSTAL);
	          	          	en.teleport(cylBlock);
	                    	//spawnEnderCrystal(cylBlock);
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
	          //spawnEnderCrystal(b);
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
	
	public static void makeQuadratWalls(Location ecke1,Location ecke2,Material material){
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
	
	public static void makeQuadrat(Location ecke1,Location ecke2,Material[] materials){
		for(int x=MinX(ecke1, ecke2); x<MaxX(ecke1, ecke2); x++){
			for(int z=MinZ(ecke1, ecke2); z<MaxZ(ecke1, ecke2); z++){
				for(int y=MinY(ecke1, ecke2); y<MaxY(ecke1, ecke2); y++){
					ecke1.getWorld().getBlockAt(x, y, z).setType(materials[UtilMath.r(materials.length)]);;
				}
			}
		}
	}
	
	public static void makeQuadrat(ArrayList<BlockState> states ,Location loc, int radius, int high,ItemStack ground,ItemStack wall) {
		int MaxX = MaxX(loc.clone(), radius);
		int MaxZ = MaxZ(loc.clone(), radius);
		int MinX = MinX(loc.clone(), radius);
		int MinZ = MinZ(loc.clone(), radius);
		int MinY = loc.getBlockY();
		int MaxY = loc.getBlockY()+high;
		
		if(wall==null)wall=ground;
		
		for (int y = MinY; y < MaxY; y++) {
			for (int z = MinZ; z < MaxZ; z++) {
				if(states!=null)states.add(loc.getWorld().getBlockAt(MaxX, y, z).getState());
				loc.getWorld().getBlockAt(MaxX, y, z).setType( (y==MinY?ground.getType():wall.getType()) );
				loc.getWorld().getBlockAt(MaxX, y, z).setData( (y==MinY?ground.getData().getData():wall.getData().getData()) );
			}

			for (int z = MaxZ; z > MinZ; z--) {
				if(states!=null)states.add(loc.getWorld().getBlockAt(MinX, y, z).getState());
				loc.getWorld().getBlockAt(MinX, y, z).setType( (y==MinY?ground.getType():wall.getType()) );
				loc.getWorld().getBlockAt(MinX, y, z).setData( (y==MinY?ground.getData().getData():wall.getData().getData()) );
			}
			
			for (int x = MaxX; x > MinX; x--) {
				if(states!=null)states.add(loc.getWorld().getBlockAt(x, y, MaxZ).getState());
				loc.getWorld().getBlockAt(x, y, MaxZ).setType( (y==MinY?ground.getType():wall.getType()) );
				loc.getWorld().getBlockAt(x, y, MaxZ).setData( (y==MinY?ground.getData().getData():wall.getData().getData()) );
			}

			for (int x = MinX; x < MaxX; x++) {
				if(states!=null)states.add(loc.getWorld().getBlockAt(x, y, MinZ).getState());
				loc.getWorld().getBlockAt(x, y, MinZ).setType( (y==MinY?ground.getType():wall.getType()) );
				loc.getWorld().getBlockAt(x, y, MinZ).setData( (y==MinY?ground.getData().getData():wall.getData().getData()) );
			}
		}


		for(int x = MinX; x<=MaxX; x++){
			for(int z = MinZ; z<=MaxZ; z++){
				if(states!=null)states.add(loc.getWorld().getBlockAt(x, MinY, z).getState());
				loc.getWorld().getBlockAt(x, MinY, z).setType(ground.getType());
				loc.getWorld().getBlockAt(x, MinY, z).setData(ground.getData().getData());

				if(states!=null)states.add(loc.getWorld().getBlockAt(x, MaxY, z).getState());
				loc.getWorld().getBlockAt(x, MaxY, z).setType(ground.getType());
				loc.getWorld().getBlockAt(x, MaxY, z).setData(ground.getData().getData());
			}
		}
	}
	
	public static List<Location> makeQuadrat(Location loc, int r) {
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
	      net.minecraft.server.v1_8_R3.Chunk chunk = (net.minecraft.server.v1_8_R3.Chunk)chunkIterator.next();
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
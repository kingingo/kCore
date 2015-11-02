package me.kingingo.kcore.Util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_8_R3.PacketPlayOutMapChunk;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;

public class UtilLocation {
	
	public static Location Distance(ArrayList<Location> locs,Location l){
		double dis=0;
		Location lo=null;
		for(Location loc : locs){
			dis=loc.distance(l);
			lo=loc;
			for(Location loc1 : locs){
				if(loc==loc1)continue;
				if(loc1.distance(l) < dis){
					lo=null;
					break;
				}
			}
			if(lo!=null)break;
		}
		
		return lo;
	}
	
	public static Location getLowestBlock(Location loc) {
		loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));
		Location toreturn = loc.clone();
		Block b;
		for(int y = 255; y > 0; y--){
			b = toreturn.getBlock();
			if(b.getType()==Material.AIR||b.getTypeId()==87||b.getTypeId()==78||b.getTypeId()==31||b.getTypeId()==37||b.getTypeId()==38||b.getTypeId()==39||b.getTypeId()==40|| b.getType().toString().contains("WATER")){
				toreturn.setY(y);
			}else{
				toreturn.setY(y+2);
				break;
			}
		}
		return toreturn;
	}
	
	public static String getLocString(Location l)
	  {
	    if (l == null) return "null";
	    StringBuilder sb = new StringBuilder();
	    sb.append(isInt(l.getX()) ? l.getBlockX() : l.getX()).append(",");
	    sb.append(isInt(l.getY()) ? l.getBlockY() : l.getY()).append(",");
	    sb.append(isInt(l.getZ()) ? l.getBlockZ() : l.getZ());
	    if (l.getYaw() != 0.0F) sb.append(",").append(l.getYaw());
	    if (l.getPitch() != 0.0F) sb.append(",").append(l.getPitch());
	    return sb.toString();
	  }
	
	public static boolean isInt(double f)
	  {
	    return Math.floor(f) == f;
	  }
	
	public static boolean isSameLocation(Location loc,Location loc1){
		return (loc.getBlockX()==loc1.getBlockX()&&loc.getBlockY()==loc1.getBlockY()&&loc.getBlockZ()==loc1.getBlockZ());
	}
	
	public static Block[] searchBlocks(Material material,int radius, Location startloc){
		List<Block> list = getScans(radius,true, startloc);
		Block[] blocks;
		int i = 0;
		for(Block b : list){
			if(b.getType()==material){
				i++;
			}
		}
		blocks=new Block[i];
		i=0;
		for(Block b : list){
			if(b.getType()==material){
				blocks[i]=b;
				i++;
			}
		}
		material=null;
		radius=0;
		i=0;
		startloc=null;
		list.clear();
		list=null;
		return blocks;
	}
	
	public static Block searchBlock(Material material,int radius, Location startloc){
		List<Block> list = getScans(radius,true, startloc);
		Block block = null;
		for(Block b : list){
			if(b.getType()==material){
				block=b;
				break;
			}
		}
		list.clear();
		list=null;
		return block;
	}
	
	public static List<Block> getScans(int radius,boolean air, Location startloc) {
		List<Block> list = Lists.newArrayList();
		Block blockName;
		final Block block = startloc.getBlock();
		final int x = block.getX();
		final int y = block.getY();
		final int z = block.getZ();
		final int minX = x - radius;
		final int minY = y - radius;
		final int minZ = z - radius;
		final int maxX = x + radius;
		final int maxY = y + radius;
		final int maxZ = z + radius;
		for (int counterX = minX; counterX <= maxX; counterX++) {
			for (int counterY = minY; counterY <= maxY; counterY++) {
				for (int counterZ = minZ; counterZ <= maxZ; counterZ++) {
					blockName = startloc.getWorld().getBlockAt(counterX, counterY, counterZ);
					if(air&&blockName.getType()==Material.AIR)continue;
					list.add(blockName);
				}
			}
		}

		return list;
	}
	
	public static Vector FromToVector(Location first_location,Location second_location){
		return new Vector(second_location.getX(), second_location.getY(), second_location.getZ()).subtract(new Vector(first_location.getX(), first_location.getY(), first_location.getZ()));
	}
	
	public static Vector calculateVector(Location from, Location to) {
		Location a = from, b = to;
		
		//calculate the distance between the locations (a => from || b => to)
		double dX = a.getX() - b.getX();
		double dY = a.getY() - b.getY();
		double dZ = a.getZ() - b.getZ();
		// -------------------------
		
		//calculate the yaw
		double yaw = Math.atan2(dZ, dX);
		// -------------------------
		
		//calculate the pitch
		double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
		// -------------------------
		
		//calculate and create the new vector
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.sin(pitch) * Math.sin(yaw);
		double z = Math.cos(pitch);
		
		Vector vector = new Vector(x, z, y);
		// -------------------------
		
		return vector;
	}
	
	public static ArrayList<Location> SpiralLocs(World w,int locs,int border,int x,int y,int z){
		return SpiralLocs(w, locs,border,new Location(w,x,y,z));
	}
	
	public static void makeCircle(Location loc, Integer r, Material m) {
        int x;
        int y = loc.getBlockY();
        int z;      
        
        for (double i = 0.0; i < 360.0; i += 0.1) {
        	double angle = i * Math.PI / 180;
            x = (int)(loc.getX() + r * Math.cos(angle));
            z = (int)(loc.getZ() + r * Math.sin(angle));
            loc.getWorld().getBlockAt(x, y, z).setType(m);
        }
    }
	
	public static void playHelix(Location location, EntityType e){
		int strands = 8;
		int radius = 10;
		int particles = 80;
		double rotation = Math.PI / 4;
		int curve = 10;
		playHelix(location, radius, strands, particles, rotation, curve, e);
	}
	
	public static void playHelix(Location location,int radius,int strands,int particles, double rotation,int curve,EntityType t){
		Entity e;
        for (int i = 1; i <= strands; i++) {
            for (int j = 1; j <= particles; j++) {
                float ratio = (float) j / particles;
                double angle = curve * ratio * 2 * Math.PI / strands + (2 * Math.PI * i / strands) + rotation;
                double x = Math.cos(angle) * ratio * radius;
                double z = Math.sin(angle) * ratio * radius;
                e=location.getWorld().spawnEntity(location, t);
                e.setVelocity(e.getVelocity().add(new Vector(x,0,z)));
            }
        }
	}
	
	public static void ThrowInSpiral(Location loc,double r,EntityType type) {
        int x;
        int y = loc.getBlockY()+2;
        int z;
        double angle; 
        Entity e;
        for (int i = 0; i < 360; i++) {
        	angle = i * Math.PI / 180;
            x = (int)(loc.getX() + r * Math.cos(angle));
            z = (int)(loc.getZ() + r * Math.sin(angle));
            e = loc.getWorld().spawnEntity(loc,type);
            e.setVelocity(new Vector(x, y, z));
        }
    }
	
	public static void DropItemInSpiral(Location loc,double max_r,Material m) {
        int x;
        int y = loc.getBlockY();
        int z;
        double angle;
        double r=1;      
        
        for (double i = 0.0; i < 360.0; i += 0.1) {
        	angle = i * Math.PI / 180;
        	r+=0.005;
            x = (int)(loc.getX() + r * Math.cos(angle));
            z = (int)(loc.getZ() + r * Math.sin(angle));
            loc.getWorld().dropItemNaturally(loc.getWorld().getBlockAt(x, y, z).getLocation(), new ItemStack(m));
            if(i>=350&&loc.getWorld().getBlockAt(x, y, z).getLocation().distance(loc)<max_r)i=0.0;
        }
    }
	
	public static void makeSpiral(Location loc,double max_r,Material m) {
        int x;
        int y = loc.getBlockY();
        int z;
        double angle;
        double r=1;      
        
        for (double i = 0.0; i < 360.0; i += 0.1) {
        	angle = i * Math.PI / 180;
        	r+=0.005;
            x = (int)(loc.getX() + r * Math.cos(angle));
            z = (int)(loc.getZ() + r * Math.sin(angle));
            loc.getWorld().getBlockAt(x, y, z).setType(m);
            if(i>=350&&loc.getWorld().getBlockAt(x, y, z).getLocation().distance(loc)<max_r)i=0.0;
        }
    }
	
	public static ArrayList<Location> RandomLocs(World w,int locs,int border,Location loc){
		ArrayList<Location> list = new ArrayList<>();
		  	int x;
	        int y = loc.getBlockY();
	        int z;
	        int r=1;      
	        
	        for (int i = 0; i < locs; i++) {
	        	r+=5;
	            x = (int)UtilMath.RandomInt(loc.getBlockX()+r, loc.getBlockX());
	            z = (int)UtilMath.RandomInt(loc.getBlockZ()+r, loc.getBlockZ());

	            for(Location l : list){
	            	if(w.getBlockAt(x, y, z).getLocation().distance(l) < border){
	            		x=-9999;
	            		z=-9999;
	            	}
	            }
	            if(x!=-9999&&z!=-9999)list.add(w.getBlockAt(x, y, z).getLocation());
	            if(list.size()>locs){
	            	break;
	            }else if(list.size()<locs){
	            	i=0;
	            }
	        }
		
		for(Location l : list){
			System.out.println("RandomLocs: "+l);
		}
		
		return list;
	}
	
	public static Location getHighestLocInCase(Location center,Material m){
		Location max = new Location(center.getWorld(),0,0,0);
		boolean found=false;
		
		for(int y = center.getBlockY(); y < (center.getBlockY()+500) ; y++){
			if(center.getWorld().getBlockAt(center.getBlockX(),y,center.getBlockZ()).getType()==m){
				max.setY(y-1);
				found=true;
				break;
			}
		}
		
		if(!found){
			return null;
		}else{
			found=false;
		}
		
		for(int x = center.getBlockX(); x < (center.getBlockX()+500) ; x++){
			if(center.getWorld().getBlockAt(x,center.getBlockY(),center.getBlockZ()).getType()==m){
				max.setX(x-1);
				found=true;
				break;
			}
		}
		
		if(!found){
			return null;
		}else{
			found=false;
		}
		
		for(int z = center.getBlockZ(); z < (center.getBlockZ()+500) ; z++){
			if(center.getWorld().getBlockAt(center.getBlockX(),center.getBlockY(),z).getType()==m){
				max.setZ(z-1);
				found=true;
				break;
			}
		}
		
		if(!found){
			return null;
		}else{
			found=false;
		}
		
		return max;
	}
	
	public static Location getLowestLocInCase(Location center,Material m){
		Location min = new Location(center.getWorld(),0,0,0);
		boolean found=false;
		
		for(int y = center.getBlockY(); y > (center.getBlockY()-500) ; y--){
			if(center.getWorld().getBlockAt(center.getBlockX(),y,center.getBlockZ()).getType()==m){
				min.setY(y+1);
				found=true;
				break;
			}
		}
		
		if(!found){
			return null;
		}else{
			found=false;
		}
		
		for(int x = center.getBlockX(); x > (center.getBlockX()-500) ; x--){
			if(center.getWorld().getBlockAt(x,center.getBlockY(),center.getBlockZ()).getType()==m){
				min.setX(x+1);
				found=true;
				break;
			}
		}
		
		if(!found){
			return null;
		}else{
			found=false;
		}
		
		for(int z = center.getBlockZ(); z > (center.getBlockZ()-500) ; z--){
			if(center.getWorld().getBlockAt(center.getBlockX(),center.getBlockY(),z).getType()==m){
				min.setZ(z+1);
				found=true;
				break;
			}
		}
		
		if(!found){
			return null;
		}else{
			found=false;
		}
		
		return min;
	}
	
	public static ArrayList<Location> SpiralLocs(World w,int locs,int border,Location loc){
		ArrayList<Location> list = new ArrayList<>();
		  	int x;
	        int y = loc.getBlockY();
	        int z;
	        double angle;
	        double r=1;      
	        
	        for (double i = 0.0; i < 360.0; i += 0.1) {
	        	angle = i * Math.PI / 180;
	        	r+=0.005;
	            x = (int)(loc.getX() + r * Math.cos(angle));
	            z = (int)(loc.getZ() + r * Math.sin(angle));

	            for(Location l : list){
	            	if(w.getBlockAt(x, y, z).getLocation().distance(l) < border){
	            		x=-9999;
	            		z=-9999;
	            	}
	            }
	            if(x!=-9999&&z!=-9999)list.add(w.getBlockAt(x, y, z).getLocation());
	            if(i>=350&&list.size()<locs)i=0.0;
	        }
		
		for(Location l : list){
			System.out.println("SpiralLocs: "+l);
		}
		
		return list;
	}
	
}
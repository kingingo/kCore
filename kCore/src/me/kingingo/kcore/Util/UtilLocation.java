package me.kingingo.kcore.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

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
		Location toreturn = loc.clone();
		int start = 255;
		toreturn.setY(start);
		while (true) {
			Block b = toreturn.getBlock();
			if (b.getType() == Material.AIR
					|| b.getType() == Material.LONG_GRASS
					|| b.getTypeId() == 78 || b.getTypeId() == 37
					|| b.getTypeId() == 38 || b.getTypeId() == 39
					|| b.getTypeId() == 40 || b.getType() == Material.BEDROCK
					|| b.getType().toString().contains("WATER")) {
				start--;
				toreturn.setY(start);
			} else {
				start++;
				toreturn.setY(start);
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
	
	public static List<Block> getScans(int radius, Location startloc) {
		List<Block> list = Lists.newArrayList();
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
					final Block blockName = startloc.getWorld().getBlockAt(
							counterX, counterY, counterZ);
					list.add(blockName);
				}
			}
		}

		return list;
	}
	
	public static ArrayList<Location> LocWithBorder(World w,int locs,int MinBorder,int MaxBorder,int MaxX,int MinX,int MaxZ,int MinZ,int MaxY,int MinY){
		ArrayList<Location> list = new ArrayList<>();
		int x=0;
		int z=0;
		int y=0;
		Location l = null;
		for(int i = 0; i <= 2000; i++){
			if(list.size()==locs)break;
			x=UtilMath.RandomInt(MaxX, MinX);
			z=UtilMath.RandomInt(MaxZ, MinZ);
			y=UtilMath.RandomInt(MaxY, MinY);
			l=new Location(w,x,y,z);
			for(Location loc : list){
				if(l.distance(loc)<MinBorder){
					l=null;
					break;
				}
				if(l.distance(loc)>MaxBorder){
					l=null;
					break;
				}
			}
			if(l!=null)list.add(l);
		}
		
		for(Location loc : list){
			System.out.println("LocWithBorder: "+loc);
		}
		
		return list;
	}
	
}

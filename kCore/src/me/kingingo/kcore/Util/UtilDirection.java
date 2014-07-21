package me.kingingo.kcore.Util;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public enum UtilDirection
{
	NORTH(20, 0,BlockFace.NORTH),
	SOUTH(-20, 0,BlockFace.SOUTH),
	EAST(0, 20,BlockFace.EAST),
	WEST(0, -20,BlockFace.WEST),
	NORTHEAST(20, 20,BlockFace.NORTH_EAST),
	SOUTHEAST(-20, 20,BlockFace.SOUTH_EAST),
	NORTHWEST(20, -20,BlockFace.NORTH_WEST),
	SOUTHWEST(-20, -20,BlockFace.SOUTH_WEST);
	
	private int x;
	private int z;
	private BlockFace f;
	
	private UtilDirection( int x , int z,BlockFace f )
	{
		this.x = x;
		this.f=f;
		this.z = z;
	}
	
	public Location getBlockFace(Location loc){
		return loc.getBlock().getRelative(f).getLocation();
	}
	
	public Location get(Location loc){
		loc.setX(loc.getX()+x);
		loc.setZ(loc.getZ()+z);
		return loc;
	}
	
	public UtilDirection nextDirection(){
		switch(this){
		case NORTH: return EAST;
		case EAST: return SOUTH;
		case SOUTH: return WEST;
		case WEST: return NORTH;
		default:
			return NORTH;
		}
	}	
	
//	public UtilDirection nextDirection(){
//		switch(this){
//		case NORTH: return NORTHEAST;
//		case NORTHEAST: return EAST;
//		case EAST: return SOUTHEAST;
//		case SOUTHEAST: return SOUTH;
//		case SOUTH: return SOUTHWEST;
//		case SOUTHWEST: return WEST;
//		case WEST: return NORTHWEST;
//		case NORTHWEST: return NORTH;
//		default:
//			return NORTH;
//		}
//	}
	
	public BlockFace getBlockFace(){
		return this.f;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getZ()
	{
		return this.z;
	}
	
	public static UtilDirection getDirection(String direction)
	{
		for ( UtilDirection dir : UtilDirection.values() )
		{
			if ( dir.name().equalsIgnoreCase(direction) )
				return dir;
		}
		return UtilDirection.NORTH;
	}
	
	public static UtilDirection getDirection(BlockFace face)
	{
		for ( UtilDirection dir : UtilDirection.values() )
		{
			if ( face == dir.getBlockFace() )
				return dir;
		}
		return UtilDirection.NORTH;
	}
}
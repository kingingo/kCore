package me.kingingo.kcore.Util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

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
	
	public UtilDirection WithRespectTo(){
		switch(this){
		case NORTH: return SOUTH;
		case EAST: return WEST;
		case SOUTH: return NORTH;
		case WEST: return EAST;
		default:
			return NORTH;
		}
	}
	
	public UtilDirection WithRespectTo2(){
		switch(this){
		case NORTH: return SOUTH;
		case NORTHEAST: return SOUTHWEST;
		case EAST: return WEST;
		case SOUTHEAST: return NORTHWEST;
		case SOUTH: return NORTH;
		case SOUTHWEST: return NORTHEAST;
		case WEST: return EAST;
		case NORTHWEST: return SOUTHEAST;
		default:
			return NORTH;
		}
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
	
	public UtilDirection nextDirection2(){
		switch(this){
		case NORTH: return NORTHEAST;
		case NORTHEAST: return EAST;
		case EAST: return SOUTHEAST;
		case SOUTHEAST: return SOUTH;
		case SOUTH: return SOUTHWEST;
		case SOUTHWEST: return WEST;
		case WEST: return NORTHWEST;
		case NORTHWEST: return NORTH;
		default:
			return NORTH;
		}
	}
	
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
	
	public static UtilDirection getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
         if (0 <= rotation && rotation < 22.5) {
            return UtilDirection.WEST; 
        } else if (22.5 <= rotation && rotation < 67.5) {
            return UtilDirection.NORTHWEST;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return UtilDirection.NORTH;
        } else if (112.5 <= rotation && rotation < 157.5) {
            return UtilDirection.NORTHEAST;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return UtilDirection.EAST;
        } else if (202.5 <= rotation && rotation < 247.5) {
            return UtilDirection.SOUTHEAST;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return UtilDirection.SOUTH;
        } else if (292.5 <= rotation && rotation < 337.5) {
            return UtilDirection.SOUTHWEST;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return UtilDirection.WEST;
        } else {
        	System.err.println("[Error] UtilDirection getCardinalDirection ERROR");
            return UtilDirection.NORTH;
        }
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
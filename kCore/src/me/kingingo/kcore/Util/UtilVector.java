package me.kingingo.kcore.Util;

import java.util.ArrayList;

import org.bukkit.util.Vector;

import me.kingingo.kcore.TreasureChest.NEW.Templates.BlockVector;

public class UtilVector {

	public static int getMaxZ(ArrayList<BlockVector> list){
		int z = 0;
		boolean b = false;
		for(int i = 0; i<list.size(); i++){
			z=list.get(i).getBlockZ();
			b=true;
			for(BlockVector v1 : list){
				if(z < v1.getBlockZ()){
					z=0;
					b=false;
					break;
				}
			}
			
			if(b){
				break;
			}
		}
		
		return z;
	}
	
	public static int getMaxX(ArrayList<BlockVector> list){
		int x = 0;
		boolean b = false;
		for(int i = 0; i<list.size(); i++){
			x=list.get(i).getBlockX();
			b=true;
			for(BlockVector v1 : list){
				if(x < v1.getBlockX()){
					x=0;
					b=false;
					break;
				}
			}
			
			if(b){
				break;
			}
		}
		
		return x;
	}
	
	public static Vector add(Vector a, Vector b)
	  {
	    return new Vector(a.getBlockX() + b.getBlockX(), a.getBlockY() + b.getBlockY(), a.getBlockZ() + b.getBlockZ());
	  }
	
	public static int subtractY(Vector a, Vector b)
	  {
	    return (b.getBlockY() - a.getBlockY());
	  }
	
	public static int subtractZ(Vector a, Vector b)
	  {
	    return (b.getBlockZ() - a.getBlockZ());
	  }
	
	public static int subtractX(Vector a, Vector b)
	  {
	    return (b.getBlockX() - a.getBlockX());
	  }
	
	public static Vector subtract(Vector a, Vector b)
	  {
	    return new Vector(b.getBlockX() - a.getBlockX(),b.getBlockY() - a.getBlockY(), b.getBlockZ() - a.getBlockZ());
	  }
	
	public static BlockVector subtract(BlockVector a, BlockVector b)
	  {
	    return new BlockVector(b.getBlockX() - a.getBlockX(),b.getBlockY() - a.getBlockY(), b.getBlockZ() - a.getBlockZ(),0,(byte)0);
	  }
	
	public static final Vector rotateAroundAxisX(Vector v, double angle)
	  {
	    double cos = Math.cos(angle);
	    double sin = Math.sin(angle);
	    double y = v.getY() * cos - v.getZ() * sin;
	    double z = v.getY() * sin + v.getZ() * cos;
	    return v.setY(y).setZ(z);
	  }

	  public static final Vector rotateAroundAxisY(Vector v, double angle)
	  {
	    double cos = Math.cos(angle);
	    double sin = Math.sin(angle);
	    double x = v.getX() * cos + v.getZ() * sin;
	    double z = v.getX() * -sin + v.getZ() * cos;
	    return v.setX(x).setZ(z);
	  }

	  public static final Vector rotateAroundAxisZ(Vector v, double angle)
	  {
	    double cos = Math.cos(angle);
	    double sin = Math.sin(angle);
	    double x = v.getX() * cos - v.getY() * sin;
	    double y = v.getX() * sin + v.getY() * cos;
	    return v.setX(x).setY(y);
	  }

	  public static final Vector rotateVector(Vector v, double angleX, double angleY, double angleZ)
	  {
	    rotateAroundAxisX(v, angleX);
	    rotateAroundAxisY(v, angleY);
	    rotateAroundAxisZ(v, angleZ);
	    return v;
	  }

	  public static final double angleToXAxis(Vector vector) {
	    return Math.atan2(vector.getX(), vector.getY());
	  }
	
}

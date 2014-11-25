package me.kingingo.kcore.Util;

import org.bukkit.util.Vector;

public class UtilVector {

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

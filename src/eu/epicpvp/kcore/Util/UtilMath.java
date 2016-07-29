package eu.epicpvp.kcore.Util;

import java.text.DecimalFormat;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class UtilMath
{
  public static Random random = new Random();

  public static double trim(int degree, double d){
    String format = "#.#";

    for (int i = 1; i < degree; i++) {
      format = format + "#";
    }
    DecimalFormat twoDForm = new DecimalFormat(format);
    return Double.parseDouble(twoDForm.format(d));
  }
  
  public static Vector getRandomCircleVector() {
		double rnd, x, z;
		rnd = random.nextDouble() * 2 * Math.PI;
		x = Math.cos(rnd);
		z = Math.sin(rnd);
		
		return new Vector(x, 0, z);
	}
  
  public static Integer RandomInt(int high, int low) {
    return random.nextInt(high-low) + low;
  }

  public static int randomInteger(int i){
    return random.nextInt(i);
  }

  public static double offset2d(Entity a, Entity b)
  {
    return offset2d(a.getLocation().toVector(), b.getLocation().toVector());
  }
  
  public static short prozentRechnen(int prozent, short max){
	  return UtilNumber.toShort( ((max*prozent)/100) );
  }
  
  public static double RandomDouble(double start,double end){
	  double random = new Random().nextDouble();
	  double result = start + (random * (end - start));
	  return result;
  }
  
  public static byte getCompressedAngle(float value) {
      return (byte) (value * 256.0F / 360.0F);
  }

  public static int asFixedPoint(double value) {
      return (int) (value * 32.0D);
  }

  public static float toDegree(double angle) {
      return (float) Math.toDegrees(angle);
  }

  public static double offset2d(Location a, Location b)
  {
    return offset2d(a.toVector(), b.toVector());
  }

  public static double offset2d(Vector a, Vector b)
  {
    a.setY(0);
    b.setY(0);
    return a.distance(b);
  }

  public static double offset(Entity a, Entity b)
  {
    return offset(a.getLocation().toVector(), b.getLocation().toVector());
  }

  public static double offset(Location a, Location b)
  {
    return offset(a.toVector(), b.toVector());
  }

  public static double offset(Vector a, Vector b)
  {
    return a.distance(b);
  }
}

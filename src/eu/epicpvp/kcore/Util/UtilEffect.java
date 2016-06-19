package eu.epicpvp.kcore.Util;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class UtilEffect {

	public static void playCircleEffect(Player player){
		playCircleEffect(player.getLocation().add(0,-1,0),UtilParticle.BLOCK_DUST);
	}
	
	public static void playTornado(Location location,UtilParticle cloud,UtilParticle tornado){
		double distance = .375d;
		double yOffset = .8;
		float cloudSize = 2.5f;
		float tornadoHeight = 5f;
		float maxTornadoRadius = 5f;
		boolean showTornado = true;
		int step = 0;
		boolean showCloud = (cloud!=null);
		Location l = location.add(0, yOffset, 0);
		for(int i = 0; i < (100 * cloudSize); i++){
			Vector v = UtilMath.getRandomCircleVector().multiply(UtilMath.random.nextDouble() * cloudSize);
			if(showCloud){
				cloud.display(0, 7, l.add(v), 20);
				l.subtract(v);
			}
		}
		Location t = l.clone().add(0, .2, 0);
		double r = .45 * (maxTornadoRadius*(2.35/tornadoHeight));
		for(double y = 0; y < tornadoHeight; y+=distance){
			double fr = r * y;
			if(fr > maxTornadoRadius)
				fr = maxTornadoRadius;
			for(Vector v : createCircle(y, fr)){
				if(showTornado){
					tornado.display(0, 1, t.add(v), 20);
					t.subtract(v);
					step++;
				}
			}
		}
		l.subtract(0, yOffset, 0);
	}
	
	public static ArrayList<Vector> createCircle(double y, double radius){
		double amount = radius * 64;
		double inc = (2*Math.PI)/amount;
		ArrayList<Vector> vecs = new ArrayList<Vector>();
		for(int i = 0; i < amount; i++){
			double angle = i * inc;
			double x = radius * Math.cos(angle);
			double z = radius * Math.sin(angle);
			Vector v = new Vector(x, y, z);
			vecs.add(v);
		}
		return vecs;
	}

	public static void playHelix(Location location,UtilParticle particle){
		playHelix(location, false ,10, particle);
	}
	
	public static void playHelix(Location location,boolean onGround,int radius,UtilParticle particle){
		int strands = 8;
		int particles = 80;
		double rotation = Math.PI / 4;
		int curve = 10;
        for (int i = 1; i <= strands; i++) {
            for (int j = 1; j <= particles; j++) {
                float ratio = (float) j / particles;
                double angle = curve * ratio * 2 * Math.PI / strands + (2 * Math.PI * i / strands) + rotation;
                double x = Math.cos(angle) * ratio * radius;
                double z = Math.sin(angle) * ratio * radius;
                location.add(x, 0, z);
                particle.display(0, 1, (onGround ? new Location(location.getWorld(),location.getX(),UtilLocation.getLowestY(location), location.getZ()) : location), 20);
                location.subtract(x, 0, z);
            }
        }
	}
	
	public static void playCircleEffect(Location loc,UtilParticle particle){
			double radius = 0.001;
        	for(double y = 4; y >= 0; y-=0.002) {
        		radius+=0.001;
        		double x = radius * Math.cos(y);
        		double z = radius * Math.sin(y);
        		double z1 = radius * Math.cos(225-y);
        		double x1 = radius * Math.sin(225-y);

        		particle.display((float)0.1, 1, new Location(loc.getWorld(), (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z)), 10);
        		particle.display((float)0.1, 1, new Location(loc.getWorld(), (float) (loc.getX() + x1), (float) (loc.getY() + y), (float) (loc.getZ() + z1)), 10);
		    }
	}
}

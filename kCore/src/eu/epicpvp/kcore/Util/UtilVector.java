package eu.epicpvp.kcore.Util;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.MysteryBox.Templates.BlockVector;

public class UtilVector {

	public static int getMaxZ(ArrayList<BlockVector> list) {
		int z = 0;
		boolean b = false;
		for (int i = 0; i < list.size(); i++) {
			z = list.get(i).getBlockZ();
			b = true;
			for (BlockVector v1 : list) {
				if (z < v1.getBlockZ()) {
					z = 0;
					b = false;
					break;
				}
			}

			if (b) {
				break;
			}
		}

		return z;
	}

	public static int getMaxX(ArrayList<BlockVector> list) {
		int x = 0;
		boolean b = false;
		for (int i = 0; i < list.size(); i++) {
			x = list.get(i).getBlockX();
			b = true;
			for (BlockVector v1 : list) {
				if (x < v1.getBlockX()) {
					x = 0;
					b = false;
					break;
				}
			}

			if (b) {
				break;
			}
		}

		return x;
	}

	public static Vector add(Vector a, Vector b) {
		return a.clone().add(b);
	}

	public static int subtractY(Vector a, Vector b) {
		return (b.getBlockY() - a.getBlockY());
	}

	public static int subtractZ(Vector a, Vector b) {
		return (b.getBlockZ() - a.getBlockZ());
	}

	public static int subtractX(Vector a, Vector b) {
		return (b.getBlockX() - a.getBlockX());
	}

	public static Vector subtract(Vector a, Vector b) {
		return a.clone().subtract(b);
	}

	public static BlockVector subtract(BlockVector a, BlockVector b) {
		return new BlockVector(b.getBlockX() - a.getBlockX(), b.getBlockY() - a.getBlockY(),
				b.getBlockZ() - a.getBlockZ(), 0, (byte) 0);
	}

	public static final Vector rotateAroundAxisX(Vector v, double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double y = v.getY() * cos - v.getZ() * sin;
		double z = v.getY() * sin + v.getZ() * cos;
		return v.setY(y).setZ(z);
	}

	public static final Vector rotateAroundAxisY(Vector v, double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double x = v.getX() * cos + v.getZ() * sin;
		double z = v.getX() * -sin + v.getZ() * cos;
		return v.setX(x).setZ(z);
	}

	public static final Vector rotateAroundAxisZ(Vector v, double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double x = v.getX() * cos - v.getY() * sin;
		double y = v.getX() * sin + v.getY() * cos;
		return v.setX(x).setY(y);
	}

	public static final Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
		rotateAroundAxisX(v, angleX);
		rotateAroundAxisY(v, angleY);
		rotateAroundAxisZ(v, angleZ);
		return v;
	}

	public static final double angleToXAxis(Vector vector) {
		return Math.atan2(vector.getX(), vector.getY());
	}

	public static ArrayList<Vector> makeCircleTop(Location center, double radius) {
		ArrayList<Location> list = UtilLocation.makeCircle(center, radius);
		ArrayList<Vector> top = new ArrayList<>();
		
		for(Location loc : list){
			Block highestBlock = loc.getWorld().getHighestBlockAt(loc);
			
			if(highestBlock!=null){
				top.add(subtract(center.toVector(), highestBlock.getLocation().toVector()));
			}
		}
		
		return top;
	}

	public static ArrayList<Vector> convertToVector(Location center, ArrayList<Location> list) {
		ArrayList<Vector> vlist = new ArrayList<>();

		for (Location loc : list) {
			vlist.add(UtilVector.subtract(center.toVector(), loc.toVector()));
		}

		return vlist;
	}

	public static ArrayList<Vector> makeCircle(Location center, Integer radius) {
		ArrayList<Location> list = UtilLocation.makeCircle(center, radius);
		return convertToVector(center, list);
	}

	/**
	 * This handles non-unit vectors, with yaw and pitch instead of X,Y,Z
	 * angles.
	 * <p>
	 * Thanks to SexyToad!
	 */
	public static final Vector rotateVector(Vector v, float yawDegrees, float pitchDegrees) {
		double yaw = Math.toRadians(-1 * (yawDegrees + 90));
		double pitch = Math.toRadians(-pitchDegrees);

		double cosYaw = Math.cos(yaw);
		double cosPitch = Math.cos(pitch);
		double sinYaw = Math.sin(yaw);
		double sinPitch = Math.sin(pitch);

		double initialX, initialY, initialZ;
		double x, y, z;

		// Z_Axis rotation (Pitch)
		initialX = v.getX();
		initialY = v.getY();
		x = initialX * cosPitch - initialY * sinPitch;
		y = initialX * sinPitch + initialY * cosPitch;

		// Y_Axis rotation (Yaw)
		initialZ = v.getZ();
		initialX = x;
		z = initialZ * cosYaw - initialX * sinYaw;
		x = initialZ * sinYaw + initialX * cosYaw;

		return v.setX(x).setY(y).setZ(z);
	}
}

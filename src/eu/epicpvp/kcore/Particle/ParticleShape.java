package eu.epicpvp.kcore.Particle;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilVector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class ParticleShape<P extends Enum<P>, V> {

	protected static final double PARTICLES_PER_BLOCK_DEFAULT = 6;
	protected static double particlesPerBlock = PARTICLES_PER_BLOCK_DEFAULT;
	private final Map<Vector, P> positions = new HashMap<>();
	@Getter
	private final String name;
	@Getter
	private final PermissionType permission;
	@Getter
	private final boolean playerSpecificTransform;

	public ParticleShape(String name, PermissionType permission, boolean playerSpecificTransform) {
		this.name = name;
		this.permission = permission;
		this.playerSpecificTransform = playerSpecificTransform;
		initShape();
		System.out.println(name + " has " + positions.size() + " particles");
	}

	public ParticleShape(String name, PermissionType permission) {
		this(name, permission, false);
	}

	protected final void reinitShape() {
		positions.clear();
		initShape();
	}

	protected abstract void initShape();

	protected Map<Vector, P> createSymmetricLines(double x1, double y1, double x2, double y2, P part) {
		return createSymmetricLines(x1, y1, 0, x2, y2, 0, part, part);
	}

	protected Map<Vector, P> createSymmetricLines(double x1, double y1, double x2, double y2, P part1, P part2) {
		return createSymmetricLines(x1, y1, 0, x2, y2, 0, part1, part2);
	}

	protected Map<Vector, P> createSymmetricLines(double x1, double y1, double z1, double x2, double y2, double z2, P part) {
		return createSymmetricLines(x1, y1, z1, x2, y2, z2, part, part);
	}

	protected Map<Vector, P> createSymmetricLines(double x1, double y1, double z1, double x2, double y2, double z2, P part1, P part2) {
		Map<Vector, P> line1 = createLine(-x1, y1, z1, -x2, y2, z2, part1);
		Map<Vector, P> line2 = createLine(x1, y1, z1, x2, y2, z2, part2);
		HashMap<Vector, P> result = Maps.newHashMapWithExpectedSize(line1.size() + line2.size());
		result.putAll(line1);
		result.putAll(line2);
		return result;
	}

	protected Map<Vector, P> fill(P searchPart, P part) {
		Map<Vector, P> result = new HashMap<>();

		for (Map.Entry<Vector, P> entry1 : getPositions().entrySet()) {
			if (entry1.getValue() != searchPart) {
				continue;
			}
			Vector pos1 = entry1.getKey();

			for (Map.Entry<Vector, P> entry2 : getPositions().entrySet()) {
				if (entry2.getValue() != searchPart) {
					continue;
				}
				Vector pos2 = entry2.getKey();

				if (Math.abs(pos1.getY() - pos2.getY()) < 0.065 && pos1.distance(pos2) > .3) {
					double y1 = 0;
					double y2 = 0;

					if (pos1.getY() > pos2.getY()) {
						y1 = pos1.getY() - .21;
						y2 = pos2.getY() + .21;
					} else {
						y1 = pos1.getY() + .21;
						y2 = pos2.getY() - .21;
					}

					result.putAll(createLine(pos1.getX(), y1, pos2.getX(), y2, part));
				}
			}
		}

		return result;
	}

	protected Map<Vector, P> createFillEllipseYZ(double x, double y, double z, double a, double b, P part) {
		double amount = ((a+b)/2) * 64;
		double inc = (2 * Math.PI) / amount;
		Map<Vector, P> result = Maps.newHashMapWithExpectedSize(UtilNumber.toInt(amount));
		for (int i = 0; i < amount; i++) {
			double angle = i * inc;
			double yCurr = (b * Math.cos(angle)) + y;
			double zCurr = (a * Math.sin(angle)) + z;
			result.putAll(createLine(x, y, z, x, yCurr, zCurr, part));
		}
		
		return result;
	}
	
	protected Map<Vector, P> createEllipseYZ(double x, double y, double z, double a, double b, P part) {
		double amount = ((a+b)/2) * 64;
		double inc = (2 * Math.PI) / amount;
		Map<Vector, P> result = Maps.newHashMapWithExpectedSize(UtilNumber.toInt(amount));
		for (int i = 0; i < amount; i++) {
			double angle = i * inc;
			double yCurr = (b * Math.cos(angle)) + y;
			double zCurr = (a * Math.sin(angle)) + z;
			Vector v = new Vector(x, yCurr, zCurr);
			result.put(v, part);
		}
		
		return result;
	}
	
	protected Map<Vector, P> rotateAroundAxisY(Map<Vector, P> map, double angle){
		Map<Vector, P> result = Maps.newHashMapWithExpectedSize(UtilNumber.toInt(map.size()));
		
		for(Map.Entry<Vector, P> entry : map.entrySet())
			result.put(UtilVector.rotateAroundAxisY(entry.getKey(), angle), entry.getValue());
		
		return result;
	}
	
	protected Map<Vector, P> createCircleXZ(double x, double y, double z, double radius, P part) {
		double amount = radius * 64;
		double inc = (2 * Math.PI) / amount;
		Map<Vector, P> result = Maps.newHashMapWithExpectedSize(UtilNumber.toInt(amount));
		for (int i = 0; i < amount; i++) {
			double angle = i * inc;
			double xCurr = (radius * Math.cos(angle)) + x;
			double zCurr = (radius * Math.sin(angle)) + z;
			Vector v = new Vector(xCurr, y, zCurr);
			result.put(v, part);
		}


		return result;
	}

	protected Map<Vector, P> createLine(double x1, double y1, double z1, double x2, double y2, double z2, P part) {
		Vector v1 = new Vector(x1, y1, z1);
		Vector v2 = new Vector(x2, y2, z2);
		Vector diff = v1.clone().subtract(v2);
		double len = diff.length();
		int amount = (int) (Math.round(particlesPerBlock * len));
		diff.multiply(1.0 / amount);
		amount++;
		Map<Vector, P> result = Maps.newHashMapWithExpectedSize(amount);
		for (int i = 0; i < amount; i++) {
			Vector toPut = v1.clone().add(diff.clone().multiply(-i));
			result.put(toPut, part);
		}
		return result;
	}

	protected Map<Vector, P> createLine(double x1, double y1, double x2, double y2, P part) {
		return createLine(x1, y1, 0, x2, y2, 0, part);
	}

	protected Map<Vector, P> createSymmetricPoints(double x, double y, P part1, P part2) {
		return ImmutableMap.of(new Vector(-x, y, 0), part1, new Vector(x, y, 0), part2);
	}

	public Map<Vector, P> getPositions() {
		return this.positions;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	public static class ValueHolder<V> {
		public V val;
	}

	/**
	 * @param entity the entity to check
	 * @return whether to go on drawing
	 */
	public abstract boolean transformPerTick(Entity entity, Location playerLoc, ValueHolder<V> valueHolder, Location previous);

	/**
	 * @param entity the entity to check
	 * @param playerLoc the base draw location, (= player location)
	 * @param particlePos the particle position (change this object!)
	 */
	public abstract Color transformPerParticle(Entity entity, Location playerLoc, Vector particlePos, P part, ValueHolder<V> valueHolder);

	public Color transformPerParticleAndPlayer(Entity entity, Player sendTo, Location playerLoc, Vector particlePos, P part, ValueHolder<V> valueHolder, Color color) {
		return color;
	}

	public abstract ValueHolder<V> createValueHolder();
}

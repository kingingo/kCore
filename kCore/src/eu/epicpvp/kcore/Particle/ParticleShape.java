package eu.epicpvp.kcore.Particle;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class ParticleShape<E extends Enum<E>, V> {

	private static final double PARTICLES_PER_BLOCK = 6;
	private Map<Vector, E> positions = new HashMap<>();

	protected Map<Vector, E> createSymmetricLines(double x1, double y1, double x2, double y2, E part) {
		Map<Vector, E> line1 = createLine(-x1, y1, -x2, y2, part);
		Map<Vector, E> line2 = createLine(x1, y1, x2, y2, part);
		HashMap<Vector, E> result = Maps.newHashMapWithExpectedSize(line1.size() + line2.size());
		result.putAll(line1);
		result.putAll(line2);
		return result;
	}

	protected Map<Vector, E> createLine(double x1, double y1, double x2, double y2, E part) {
		Vector v1 = new Vector(x1, y1, 0);
		Vector v2 = new Vector(x2, y2, 0);
		Vector diff = v1.clone().subtract(v2);
		double len = diff.length();
		int amount = (int) (Math.round(PARTICLES_PER_BLOCK * len));
		diff.multiply(1.0 / amount);
		amount++;
		Map<Vector, E> result = Maps.newHashMapWithExpectedSize(amount);
		for (int i = 0; i < amount; i++) {
			Vector toPut = v1.clone().add(diff.clone().multiply(-i));
			result.put(toPut, part);
		}
		return result;
	}

	public Map<Vector, E> getPositions() {
		return this.positions;
	}

	public static class ValueHolder<V> {
		V val;

		public ValueHolder(V val) {
			this.val = val;
		}
	}

	/**
	 * @param player the player to check
	 * @param locVector the base draw location
	 * @return whether to go on drawing
	 */
	public abstract boolean transformPerTick(Player player, Location playerLoc, Vector locVector, ValueHolder<V> valueHolder, Location previous);

	/**
	 * @param player the player to check
	 * @param loc the base draw location, (= player location)
	 * @param particlePos the particle position (change this object!)
	 */
	public abstract Color transformPerParticle(Player player, Location loc, Vector particlePos, E particleType, ValueHolder<V> valueHolder);

	public abstract ValueHolder<V> createValueHolder();
}

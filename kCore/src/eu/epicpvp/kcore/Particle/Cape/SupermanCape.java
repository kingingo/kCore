package eu.epicpvp.kcore.Particle.Cape;

import java.util.Map;

import eu.epicpvp.kcore.Permission.PermissionType;
import org.bukkit.Color;
import org.bukkit.util.Vector;

public class SupermanCape extends CapeShape {

	public SupermanCape(String name, PermissionType permission, Color outerColor, Color innerColor) {
		super(name, permission, outerColor, innerColor);
	}

	@Override
	protected void initShape() {
		double yAdd = .22;

		//top middle -> top corner
		getPositions().putAll(createSymmetricCapeLines(.3, 1.15 + yAdd, CapePart.OUTER));
		//-> bottom corner
		getPositions().putAll(createSymmetricLines(.3, 1.15 + yAdd, .6, .2 + yAdd, CapePart.OUTER));
		//-> bottom middle
		getPositions().putAll(createSymmetricCapeLines(.6, .2 + yAdd, CapePart.OUTER));

		//inner
		//get side line to calculate inner lines
		particlesPerBlock = 8;
		createLine(.3, 1.15 + yAdd, .6, .2 + yAdd, CapePart.OUTER).keySet().stream()
				.filter(vector -> vector.getY() >= .201 + yAdd && vector.getY() <= 1.14 + yAdd) //ignore outer points, not using == because of possible double inaccuracy
				.forEach(vector -> getPositions().putAll(createSymmetricCapeLines(vector.getX() - .15, vector.getY(), CapePart.INNER)));
		particlesPerBlock = PARTICLES_PER_BLOCK_DEFAULT;
	}

	private Map<Vector, CapePart> createSymmetricCapeLines(double x2, double y, CapePart capePart) {
		return createSymmetricLines(0, y, x2, y, capePart);
	}
}

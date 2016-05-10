package eu.epicpvp.kcore.Particle.Wings;

import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilVector;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BatWings extends WingShape {

	public BatWings(String name, PermissionType permission, boolean moveWings, Color outerColor, Color innerColor, Color middleColor) {
		super(name, permission, true, moveWings, outerColor, innerColor, middleColor);
	}

	@Override
	protected void initShape() {
		//Mid -> 1. peak
		double yAdd = .15;
		getPositions().putAll(createSymmetricLines(0, 1 + yAdd, .6, .7 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> between 1. and 2. peak
		getPositions().putAll(createSymmetricLines(.6, .7 + yAdd, .7, 1 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> 2. peak
		getPositions().putAll(createSymmetricLines(.7, 1 + yAdd, 1, 1 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> between 2.-3. peak
		getPositions().putAll(createSymmetricLines(1, 1 + yAdd, 1.1, 1.4 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> 3. peak
		getPositions().putAll(createSymmetricLines(1.1, 1.4 + yAdd, 1.3, 1.6 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> between 3.-4. peak
		getPositions().putAll(createSymmetricLines(1.3, 1.6 + yAdd, 1.2, 1.9 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> 4. peak
		getPositions().putAll(createSymmetricLines(1.2, 1.9 + yAdd, 1.3, 2.1 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> top inner
		getPositions().putAll(createSymmetricLines(1.3, 2.1 + yAdd, 1.1, 1.9 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> rounding top
		getPositions().putAll(createSymmetricLines(1.1, 1.9 + yAdd, .6, 1.7 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> rounding top #2
		getPositions().putAll(createSymmetricLines(.6, 1.7 + yAdd, .4, 1.35 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> mid
		particlesPerBlock = 8;
		getPositions().putAll(createSymmetricLines(.4, 1.35 + yAdd, 0, 1.15 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		particlesPerBlock = PARTICLES_PER_BLOCK_DEFAULT;

//		getPositions().putAll(fill(WingPart.OUTER_RIGHT, WingPart.INNER_RIGHT));
//		getPositions().putAll(fill(WingPart.OUTER_LEFT, WingPart.INNER_LEFT));

		//inner
//		mid -> 3. peak
		getPositions().putAll(createSymmetricLines(.22, 1.02 + yAdd, 1.1, 1.6 + yAdd, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		//around 1. peak
		getPositions().putAll(createSymmetricPoints(.45, 0.92 + yAdd, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		getPositions().putAll(createSymmetricPoints(.55, 1 + yAdd, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		//around 2. peak
		getPositions().putAll(createSymmetricLines(.7, 1.17 + yAdd, .9, 1.17 + yAdd, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		getPositions().putAll(createSymmetricLines(.85, 1.17 + yAdd, .9, 1.3 + yAdd, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		//top line back
		getPositions().putAll(createSymmetricLines(.7, 1.57 + yAdd, 1.1, 1.77 + yAdd, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		getPositions().putAll(createSymmetricPoints(.65, 1.4 + yAdd, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));

		//Peak 1
		getPositions().putAll(createSymmetricLines(.6, .7 + yAdd, .65, .4 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//Peak 2
		particlesPerBlock = 8;
		getPositions().putAll(createSymmetricLines(1, 1 + yAdd, 1.13, .8 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		particlesPerBlock = PARTICLES_PER_BLOCK_DEFAULT;
		//Peak 3
		getPositions().putAll(createSymmetricLines(1.3, 1.6 + yAdd, 1.53, 1.45 + yAdd, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));

		//devil horns
		particlesPerBlock = 8;
//		getPositions().putAll(createSymmetricLines(.2, 1.9 - 1.32, .7 - .55, .3, 2.08 - 1.32, .6 - .55, WingPart.MIDDLE, WingPart.MIDDLE));
		particlesPerBlock = PARTICLES_PER_BLOCK_DEFAULT;
	}

	@Override
	public Color transformPerParticle(Player player, Location playerLoc, Vector particlePos, WingPart wingPart, ValueHolder<WingState> valueHolder) {
		//TODO create method in ParticleShape (this is duplicate code with AngelWings)
		if (wingPart != WingPart.MIDDLE) {
			return super.transformPerParticle(player, playerLoc, particlePos, wingPart, valueHolder);
		}
		UtilVector.rotateAroundAxisX(particlePos, Math.toRadians(playerLoc.getPitch()));
		UtilVector.rotateVector(particlePos, playerLoc.getYaw() - 90, 0);

		Vector playerLocVector = playerLoc.toVector();
		playerLocVector.setY(playerLocVector.getY() + (player.isSneaking() ? 1.1 : 1.4));
		particlePos.add(playerLocVector);

		return middleColor;
	}

	@Override
	public Color transformPerParticleAndPlayer(Player player, Player sendTo, Location playerLoc, Vector particlePos, WingPart wingPart, ValueHolder<WingState> valueHolder, Color color) {
		if (wingPart == WingPart.MIDDLE && player.isSneaking() && player.getUniqueId().equals(sendTo.getUniqueId())) {
			// its a bit too much in sight if sneaking due to different display of head position and actual camera position (WHYYYY mojang)
			particlePos.setY(particlePos.getY() + .15);
		}
		return color;
	}
}

package eu.epicpvp.kcore.Particle.Wings;

import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilVector;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AngelWings extends WingShape {

	private static final double TOP_CORNER_X = 1.0;
	private static final double TOP_CORNER_Y = 1.7;
	private static final double MIDDLE_UPPER_Y = 1.3;
	private static final double BOTTOM_MIDDLE_Y = .9;
	private static final double WING_MIDDLE_X = .6;
	private static final double WING_MIDDLE_Y = .6;
	private static final double BOTTOM_X = .65;
	private static final double BOTTOM_Y = .3;

	public AngelWings(String name, PermissionType permission, boolean moveWings, Color outerColor, Color innerColor, Color middleColor) {
		super(name, permission, moveWings, outerColor, innerColor, middleColor);
	}

	@Override
	protected void initShape() {
		// upper line to middle top
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X, TOP_CORNER_Y, 0, MIDDLE_UPPER_Y, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		// top corner to wing middle
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X + .08, TOP_CORNER_Y - .15, WING_MIDDLE_X, WING_MIDDLE_Y, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		// wing middle to bottom
		getPositions().putAll(createSymmetricLines(WING_MIDDLE_X, WING_MIDDLE_Y, BOTTOM_X, BOTTOM_Y, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		// bottom to middle
		getPositions().putAll(createSymmetricLines(BOTTOM_X - .15, BOTTOM_Y, 0, BOTTOM_MIDDLE_Y, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));

		// inner lines
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X - .15, TOP_CORNER_Y - .25, 0, MIDDLE_UPPER_Y - .25, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		// first inner side line (top to bottom wing corner)
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X - .2, TOP_CORNER_Y - .35, .45, WING_MIDDLE_Y + .1, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		// second inner side line (top to bottom wing corner)
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X - .4, TOP_CORNER_Y - .5, .37, BOTTOM_MIDDLE_Y - .1, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		// third inner line
		getPositions().putAll(createSymmetricLines(.2, BOTTOM_MIDDLE_Y, .35, BOTTOM_MIDDLE_Y + .2, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));

		getPositions().putAll(createCircle(0, .9, 0, .3, WingPart.MIDDLE)); //we special case MIDDLE below, we add something to y later
	}

	@Override
	public Color transformPerParticle(Player player, Location playerLoc, Vector particlePos, WingPart wingPart, ValueHolder<WingState> valueHolder) {
		//TODO create method in ParticleShape (this is duplicate code with BatWings)
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
}

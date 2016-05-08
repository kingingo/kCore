package eu.epicpvp.kcore.Particle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.google.common.collect.Maps;

import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilVector;
import lombok.Getter;

public class WingShape extends ParticleShape<WingShape.WingPart, WingShape.WingState> {

	private static final double PI = 3.1415927; //not as precise as Math.PI, but enough
	private static final long MOVEMENT_MILLIS = 300;
	private static final double ROT_SPEED = 1.0 / 32.0;
	private static final double WING_MIN_ROT = 0.1;
	private static final double WING_MAX_ROT = 1.3;

	private static final double TOP_CORNER_X = 1.0;
	private static final double TOP_CORNER_Y = 1.7;
	private static final double MIDDLE_UPPER_Y = 1.3;
	private static final double BOTTOM_MIDDLE_Y = .9;
	private static final double WING_MIDDLE_X = .6;
	private static final double WING_MIDDLE_Y = .6;
	private static final double BOTTOM_X = .65;
	private static final double BOTTOM_Y = 0.3;

	private static final double THINKNESS_CONST = .28;

	private final boolean moveWings;
	private final Color outerColor;
	private final Color innerColor;
	private final Color middleColor;
	@Getter
	private ItemStack item;
	@Getter
	private PermissionType permission;

	public WingShape(ItemStack item,PermissionType permission,boolean moveWings, Color outerColor, Color innerColor, Color middleColor) {
		this.item=item;
		this.permission=permission;
		this.moveWings = moveWings;
		this.outerColor = outerColor;
		this.innerColor = innerColor;
		this.middleColor = middleColor;

		//upper line to middle top
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X, TOP_CORNER_Y, 0, MIDDLE_UPPER_Y, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//top corner to wing middle
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X + .08, TOP_CORNER_Y - .15, WING_MIDDLE_X, WING_MIDDLE_Y, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		// wing middle to bottom
		getPositions().putAll(createSymmetricLines(WING_MIDDLE_X, WING_MIDDLE_Y, BOTTOM_X, BOTTOM_Y, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		// bottom to middle
		getPositions().putAll(createSymmetricLines(BOTTOM_X - .15, BOTTOM_Y, 0, BOTTOM_MIDDLE_Y, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//upper line - inner
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X - THINKNESS_CONST * .8, TOP_CORNER_Y - THINKNESS_CONST, 0, MIDDLE_UPPER_Y - THINKNESS_CONST, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//upper line - inner second line
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X - THINKNESS_CONST * 2, TOP_CORNER_Y - THINKNESS_CONST * 2, THINKNESS_CONST, MIDDLE_UPPER_Y - THINKNESS_CONST * 2, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//upper line - inner third line
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X - THINKNESS_CONST, TOP_CORNER_Y - THINKNESS_CONST, THINKNESS_CONST * 1.5, MIDDLE_UPPER_Y - THINKNESS_CONST * 2.5, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//lower inner points
//		wingLeft.put(new Vector(-.4, .55, 0), WingPart.INNER);
//		wingRight.put(new Vector(.4, .55, 0), WingPart.INNER);
		getPositions().putAll(createLine(0, BOTTOM_MIDDLE_Y -.1, 0, MIDDLE_UPPER_Y + .1, WingPart.MIDDLE));
		getPositions().putAll(createLine(0, BOTTOM_MIDDLE_Y -.11, 0, MIDDLE_UPPER_Y + .1, WingPart.MIDDLE));
//		wingLeft = ImmutableMap.copyOf(wingLeft);
//		wingRight = ImmutableMap.copyOf(wingRight);
	}

	public static class WingState {
		private double rotBase;
		private double rotTransformed;
		private long lastMove;
	}

	public enum WingPart {
		OUTER_LEFT,
		OUTER_RIGHT,
		INNER_LEFT,
		INNER_RIGHT,
		MIDDLE
	}

	protected Map<Vector, WingPart> createSymmetricLines(double x1, double y1, double x2, double y2, WingPart part1, WingPart part2) {
		Map<Vector, WingPart> line1 = createLine(-x1, y1, -x2, y2, part1);
		Map<Vector, WingPart> line2 = createLine(x1, y1, x2, y2, part2);
		HashMap<Vector, WingPart> result = Maps.newHashMapWithExpectedSize(line1.size() + line2.size());
		result.putAll(line1);
		result.putAll(line2);
		return result;
	}

	@Override
	public boolean transformPerTick(Player player, Location playerLoc, Vector locVector, ValueHolder<WingState> valueHolder, Location previous) {
		long now = System.currentTimeMillis();

		boolean isMovementNow = previous != null;
		if (isMovementNow) {
			if (!previous.toVector().equals(playerLoc.toVector())) {
				valueHolder.val.lastMove = now;
				return false;
			}
		} else if (now - valueHolder.val.lastMove < MOVEMENT_MILLIS) { //while moving do not display particles from timer
			return false;
		}
		if (moveWings) {
			double rot = valueHolder.val.rotBase;
			double rotPlus = ROT_SPEED;
			if (rot < 0) {
				rot = 0;
			} else if (rot > 2) {
				rot = 0;
			} else {
				if (rot > 1) {
					rotPlus *= 1.7;
				}
				rot += rotPlus;
			}
			valueHolder.val.rotBase = rot;
			if (rot > 1) {
				rot = 1 - (rot - 1);
			}
			//fit into values between min and max wing rot
			valueHolder.val.rotTransformed = ((Math.cos(PI * rot * .5) * -1 + 2) - 1) * (WING_MAX_ROT - WING_MIN_ROT) + WING_MIN_ROT;
		} else {
			valueHolder.val.rotTransformed = WING_MIN_ROT;
		}
		boolean sneaked = player.isSneaking();
		if (sneaked) {
			locVector.add(playerLoc.getDirection().setY(0).normalize().multiply(-0.8));
			locVector.setY(locVector.getY() - .25);
		} else {
			locVector.add(playerLoc.getDirection().setY(0).normalize().multiply(-0.5));
		}
		return true;
	}

	@Override
	public Color transformPerParticle(Player player, Location playerLoc, Vector vector, WingPart wingPart, ValueHolder<WingState> valueHolder) {
		boolean sneaked = player.isSneaking();
		vector = UtilVector.rotateAroundAxisX(vector, sneaked ? -.3 : -.2); //schräg zum körper
		vector = UtilVector.rotateVector(vector, playerLoc.getYaw() + 90, 0); //richtig gedreht zur kopfrichtung
		if (wingPart != WingPart.MIDDLE) {
			vector = UtilVector.rotateAroundAxisY(vector, valueHolder.val.rotTransformed); //flügeldrehung / flügelschlag
		}
		Vector locVectorHere = playerLoc.toVector();
		if (wingPart == WingPart.MIDDLE) {
			locVectorHere = locVectorHere.add(playerLoc.getDirection().setY(0).normalize().multiply(-.1));
		}
		vector.add(locVectorHere);
		switch (wingPart) {
			case INNER_LEFT:
			case INNER_RIGHT:
				return innerColor;
			case OUTER_LEFT:
			case OUTER_RIGHT:
				return outerColor;
			case MIDDLE:
				return middleColor;
			default:
				return null;
		}
	}

	@Override
	public ValueHolder<WingState> createValueHolder() {
		return new ValueHolder<>(new WingState());
	}
}

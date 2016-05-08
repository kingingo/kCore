package eu.epicpvp.kcore.Particle.Wings;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.google.common.collect.Maps;

import eu.epicpvp.kcore.Particle.ParticleShape;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilVector;

public abstract class WingShape extends ParticleShape<WingShape.WingPart, WingShape.WingState> {

	protected static final double PI = 3.1415927; //not as precise as Math.PI, but enough
	protected static final double ROT_SPEED = 1.0 / 32.0;
	protected static final double WING_MIN_ROT = 0.1;
	protected static final double WING_MAX_ROT = 1.3;

	protected final boolean moveWings;
	protected final Color outerColor;
	protected final Color innerColor;
	protected final Color middleColor;

	public WingShape(ItemStack item,PermissionType permission,boolean moveWings, Color outerColor, Color innerColor, Color middleColor) {
		super(item,permission);
		this.moveWings = moveWings;
		this.outerColor = outerColor;
		this.innerColor = innerColor;
		this.middleColor = middleColor;
	}

	public static class WingState {
		protected double rotBase;
		protected double rotTransformed;
		protected long lastMove;
	}

	public enum WingPart {
		OUTER_LEFT,
		OUTER_RIGHT,
		INNER_LEFT,
		INNER_RIGHT,
		MIDDLE
	}
	
	protected Map<Vector, WingPart> createSymmetricLines(double x1, double y1, double x2, double y2, WingPart part1, WingPart part2) {
		return createSymmetricLines(x1, y1, 0, x2, y2, 0, part1, part2);
	}

	protected Map<Vector, WingPart> createSymmetricLines(double x1, double y1, double z1, double x2, double y2, double z2, WingPart part1, WingPart part2) {
		Map<Vector, WingPart> line1 = createLine(-x1, y1, z1, -x2, y2, z2, part1);
		Map<Vector, WingPart> line2 = createLine(x1, y1, z1, x2, y2, z2, part2);
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
		Vector toChange = vector;
		boolean sneaked = player.isSneaking();
		vector = UtilVector.rotateAroundAxisX(vector, sneaked ? -.3 : -.2); //schräg zum körper
		vector = UtilVector.rotateVector(vector, playerLoc.getYaw() - 90, 0); //richtig gedreht zur kopfrichtung
		
		switch (wingPart) {
		case INNER_LEFT:
		case OUTER_LEFT:
			vector = UtilVector.rotateAroundAxisY(vector, -valueHolder.val.rotTransformed); //flügeldrehung / flügelschlag
			break;
		case INNER_RIGHT:
		case OUTER_RIGHT:
			vector = UtilVector.rotateAroundAxisY(vector, valueHolder.val.rotTransformed); //flügeldrehung / flügelschlag
			break;
		}
		
		Vector locVectorHere = playerLoc.toVector();
		if (wingPart == WingPart.MIDDLE) {
			locVectorHere = locVectorHere.add(playerLoc.getDirection().setY(0).normalize().multiply(-.1));
		}
		vector.add(locVectorHere);
		toChange.multiply(0).add(vector);

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

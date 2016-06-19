package eu.epicpvp.kcore.Particle.Wings;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Particle.NoMoveShape;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilVector;

public abstract class WingShape extends NoMoveShape<WingShape.WingPart, WingState> {

	protected static final double PI = 3.141592654; //not as precise as Math.PI, but enough
	protected static final double ROT_SPEED_BASE = 1.0 / 32.0;
	protected static final double WING_ANIMATION_DISABLED_ROT = .05 * PI;
	protected static final double WING_MIN_ROT = 0.1;
	protected static final double WING_MAX_ROT = 1.25;

	protected final boolean moveWings;
	protected final Color outerColor;
	protected final Color innerColor;
	protected final Color middleColor;

	public WingShape(String name, PermissionType permission, boolean moveWings, Color outerColor, Color innerColor, Color middleColor) {
		this(name, permission, false, moveWings, outerColor, innerColor, middleColor);
	}

	public WingShape(String name, PermissionType permission, boolean playerSpecificTransform, boolean moveWings, Color outerColor, Color innerColor, Color middleColor) {
		super(name, permission, playerSpecificTransform);
		this.moveWings = moveWings;
		this.outerColor = outerColor;
		this.innerColor = innerColor;
		this.middleColor = middleColor;
	}

	public enum WingPart {
		OUTER_LEFT,
		OUTER_RIGHT,
		INNER_LEFT,
		INNER_RIGHT,
		MIDDLE
	}

	@Override
	protected boolean transformPerTick0(Entity entity, Location playerLoc, ValueHolder<WingState> valueHolder, Location previous) {
		if (!moveWings) {
			valueHolder.val.rotTransformed = WING_ANIMATION_DISABLED_ROT;
			return true;
		}
		double rot = valueHolder.val.rotBase;
		double rotSpeed = ROT_SPEED_BASE;
		if (rot < 0) {
			rot = 0;
		} else if (rot > 2) {
			rot = 0;
		} else {
			if (rot > 1) {
				rotSpeed *= 1.7; //faster backwards
			}
			rot += rotSpeed;
		}
		valueHolder.val.rotBase = rot;
		if (rot > 1) {
			rot = 1 - (rot - 1);
		}
		//fit rot into cos function and then into values between min and max wing rot
		valueHolder.val.rotTransformed = ((Math.cos(PI * rot * .5) * -1 + 2) - 1) * (WING_MAX_ROT - WING_MIN_ROT) + WING_MIN_ROT;
		return true;
	}

	@Override
	public Color transformPerParticle(Entity entity, Location playerLoc, Vector particlePos, WingPart wingPart, ValueHolder<WingState> valueHolder) {
		if (entity instanceof Player && ((Player)entity).isSneaking()) {
			UtilVector.rotateAroundAxisX(particlePos, .1 * PI); //schräg zum körper
		}
		UtilVector.rotateVector(particlePos, playerLoc.getYaw() - 90, 0); //richtig gedreht zur kopfrichtung

		if (entity instanceof Player && ((Player)entity).isSneaking()) {
			particlePos.subtract(playerLoc.getDirection().setY(0).normalize().multiply(.3 + .4));
			particlePos.setY(particlePos.getY() - .1);
		} else {
			particlePos.subtract(playerLoc.getDirection().setY(0).normalize().multiply(.3));
		}

		switch (wingPart) {
			case INNER_LEFT:
			case OUTER_LEFT:
				UtilVector.rotateAroundAxisY(particlePos, -valueHolder.val.rotTransformed); //flügeldrehung / flügelschlag
				break;
			case INNER_RIGHT:
			case OUTER_RIGHT:
				UtilVector.rotateAroundAxisY(particlePos, valueHolder.val.rotTransformed); //flügeldrehung / flügelschlag
				break;
		}

		Vector playerLocVector = playerLoc.toVector();
		if (wingPart == WingPart.MIDDLE) { //put middle a bit more behind
			playerLocVector.add(playerLoc.getDirection().setY(0).normalize().multiply(-.1));
		}
		particlePos.add(playerLocVector);

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

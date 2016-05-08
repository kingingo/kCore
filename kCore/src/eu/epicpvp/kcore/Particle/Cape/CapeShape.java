package eu.epicpvp.kcore.Particle.Cape;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Particle.ParticleShape;
import eu.epicpvp.kcore.Particle.ParticleShape.ValueHolder;
import eu.epicpvp.kcore.Particle.Wings.WingShape.WingPart;
import eu.epicpvp.kcore.Particle.Wings.WingShape.WingState;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilVector;

public abstract class CapeShape extends ParticleShape<CapeShape.CapePart, CapeShape.CapeState> {

	private static final double CAPE_MIN_ROT = 0.1;
	
	private final boolean moveCape;
	private final Color outColor;
	private final Color inColor;
	private final Color middleColor;
	
	public CapeShape(ItemStack item, PermissionType permission,boolean moveCape,Color outColor, Color inColor, Color middleColor) {
		super(item, permission);
		this.moveCape=moveCape;
		this.outColor=outColor;
		this.inColor=inColor;
		this.middleColor=middleColor;
	}
	
	public static class CapeState {
		private double rotBase;
		private double rotTransformed;
		private long lastMove;
	}

	public enum CapePart {
		OUT,
		IN,
		MIDDLE
	}

	@Override
	public boolean transformPerTick(Player player, Location playerLoc, Vector locVector, ValueHolder<CapeState> valueHolder, Location previous) {
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
		
		valueHolder.val.rotTransformed = CAPE_MIN_ROT;
	
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
	public Color transformPerParticle(Player player, Location playerLoc, Vector vector, CapePart capePart, ValueHolder<CapeState> valueHolder) {
		Vector toChange = vector;
		boolean sneaked = player.isSneaking();
		vector.add(new Vector(0,0,-0.85));
		vector = UtilVector.rotateAroundAxisX(vector, sneaked ? .6 : .5); //schräg zum körper
		vector = UtilVector.rotateVector(vector, playerLoc.getYaw() - 90, 0); //richtig gedreht zur kopfrichtung
		
		Vector locVectorHere = playerLoc.toVector();
		if (capePart == CapePart.MIDDLE) {
			locVectorHere = locVectorHere.add(playerLoc.getDirection().setY(0).normalize().multiply(-.1));
		}
		vector.add(locVectorHere);
		toChange.multiply(0).add(vector);

		switch (capePart) {
			case IN:
				return inColor;
			case OUT:
				return outColor;
			case MIDDLE:
				return middleColor;
			default:
				return null;
		}
	}

	@Override
	public ValueHolder<CapeState> createValueHolder() {
		return new ValueHolder<>(new CapeState());
	}
}

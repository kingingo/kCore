package eu.epicpvp.kcore.Particle.Cape;

import eu.epicpvp.kcore.Particle.NoMoveShape;
import eu.epicpvp.kcore.Particle.NoMoveShape.SimpleLastMoveHolder;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilVector;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class CapeShape extends NoMoveShape<CapeShape.CapePart, SimpleLastMoveHolder> {

	private final Color outerColor;
	private final Color innerColor;
	private final Color middleColor;

	public CapeShape(String name, PermissionType permission, Color outerColor, Color innerColor, Color middleColor) {
		super(name, permission);
		this.outerColor = outerColor;
		this.innerColor = innerColor;
		this.middleColor = middleColor;
	}

	public enum CapePart {
		OUTER,
		INNER,
		MIDDLE
	}

	@Override
	public boolean transformPerTick0(Player player, Location playerLoc, Vector locVector, ValueHolder<SimpleLastMoveHolder> valueHolder, Location previous) {
		transformPerTickBehindPlayer(player, playerLoc, locVector, true);
		return true;
	}

	@Override
	public Color transformPerParticle(Player player, Location playerLoc, Vector vector, CapePart capePart, ValueHolder<SimpleLastMoveHolder> valueHolder) {
		Vector toChange = vector;
		vector.subtract(new Vector(0, 0, .85));
		vector = UtilVector.rotateAroundAxisX(vector, player.isSneaking() ? .6 : .5); //schräg zum körper
		vector = UtilVector.rotateVector(vector, playerLoc.getYaw() - 90, 0); //richtig gedreht zur kopfrichtung

		Vector locVectorHere = playerLoc.toVector();
		if (capePart == CapePart.MIDDLE) {
			locVectorHere = locVectorHere.add(playerLoc.getDirection().setY(0).normalize().multiply(-.1));
		}
		vector.add(locVectorHere);
		toChange.zero().add(vector); //reset and add to origin vector (vector is copied above)

		switch (capePart) {
			case INNER:
				return innerColor;
			case OUTER:
				return outerColor;
			case MIDDLE:
				return middleColor;
			default:
				return null;
		}
	}

	@Override
	public ValueHolder<SimpleLastMoveHolder> createValueHolder() {
		return new ValueHolder<>(new SimpleLastMoveHolder());
	}
}

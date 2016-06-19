package eu.epicpvp.kcore.Particle.Portal;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Particle.NoMoveShape;
import eu.epicpvp.kcore.Particle.NoMoveShape.SimpleLastMoveHolder;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilVector;

public abstract class PortalShape extends NoMoveShape<PortalShape.PortalPart, SimpleLastMoveHolder> {

	private final Color outerColor;
	private final Color innerColor;

	public PortalShape(String name, PermissionType permission, Color outerColor, Color innerColor) {
		super(name, permission);
		this.outerColor = outerColor;
		this.innerColor = innerColor;
	}

	public enum PortalPart {
		OUTER,
		INNER
	}

	@Override
	public boolean transformPerTick0(Entity entity, Location playerLoc, ValueHolder<SimpleLastMoveHolder> valueHolder, Location previous) {
		return true;
	}

	@Override
	public Color transformPerParticle(Entity entity, Location playerLoc, Vector particlePos, PortalPart portalPart, ValueHolder<SimpleLastMoveHolder> valueHolder) {
		particlePos.subtract(playerLoc.getDirection().setY(0).normalize().multiply(.65));
		Vector locVectorHere = playerLoc.toVector();

		particlePos.add(locVectorHere);

		switch (portalPart) {
			case INNER:
				return innerColor;
			case OUTER:
				return outerColor;
			default:
				return null;
		}
	}

	@Override
	public ValueHolder<SimpleLastMoveHolder> createValueHolder() {
		return new ValueHolder<>(new SimpleLastMoveHolder());
	}
}

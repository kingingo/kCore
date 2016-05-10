package eu.epicpvp.kcore.Particle;

import eu.epicpvp.kcore.Permission.PermissionType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class NoMoveShape<E extends Enum<E>, V extends NoMoveShape.LastMoveHolder> extends ParticleShape<E, V> {

	private final long movementMillis;

	public NoMoveShape(String name, PermissionType permission, long movementMillis) {
		this(name, permission, false, movementMillis);
	}

	public NoMoveShape(String name, PermissionType permission) {
		this(name, permission, false);
	}

	public NoMoveShape(String name, PermissionType permission, boolean playerSpecificTransform) {
		this(name, permission, playerSpecificTransform, 300);
	}

	public NoMoveShape(String name, PermissionType permission, boolean playerSpecificTransform, long movementMillis) {
		super(name, permission, playerSpecificTransform);
		this.movementMillis = movementMillis;
	}

	public interface LastMoveHolder {
		public void setLastMove(long millis);
		public long getLastMove();
	}
	
	public static class SimpleLastMoveHolder implements LastMoveHolder {
		@Getter
		@Setter
		public long lastMove;
	}

	@Override
	public final boolean transformPerTick(Player player, Location playerLoc, ValueHolder<V> valueHolder, Location previous) {
		long now = System.currentTimeMillis();

		boolean isMovementNow = previous != null;
		if (isMovementNow) {
			if (!previous.toVector().equals(playerLoc.toVector())) {
				valueHolder.val.setLastMove(now);
				return false;
			}
		} else if (now - valueHolder.val.getLastMove() < movementMillis) { //while moving do not display particles from timer
			return false;
		}
		return transformPerTick0(player, playerLoc, valueHolder, previous);
	}

	protected abstract boolean transformPerTick0(Player player, Location playerLoc, ValueHolder<V> valueHolder, Location previous);
}

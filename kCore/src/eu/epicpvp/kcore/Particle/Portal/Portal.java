package eu.epicpvp.kcore.Particle.Portal;

import java.util.Map;

import org.bukkit.Color;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Permission.PermissionType;

public class Portal extends PortalShape {

	public Portal(String name, PermissionType permission, Color outerColor, Color innerColor) {
		super(name, permission, outerColor, innerColor);
	}

	@Override
	protected void initShape() {

		
		particlesPerBlock = PARTICLES_PER_BLOCK_DEFAULT;
	}

	private Map<Vector, PortalPart> createSymmetricCapeLines(double x2, double y, PortalPart portalPart) {
		return createSymmetricLines(0, y, x2, y, portalPart);
	}
}

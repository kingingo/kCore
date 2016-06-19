package eu.epicpvp.kcore.Particle.Portal;

import java.util.Map;

import org.bukkit.Color;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Particle.Cape.CapeShape.CapePart;
import eu.epicpvp.kcore.Permission.PermissionType;

public class Portal extends PortalShape {

	public Portal(String name, PermissionType permission, Color outerColor, Color innerColor) {
		super(name, permission, outerColor, innerColor);
	}

	@Override
	protected void initShape() {
		getPositions().putAll( createEllipse(3, 1.5, 0, 0.4, 0.9, PortalPart.OUTER) );
		getPositions().putAll( createFillEllipse(3, 1.5, 0, 0.3, 0.8, PortalPart.INNER) );
		
		particlesPerBlock = PARTICLES_PER_BLOCK_DEFAULT/2;
	}

	private Map<Vector, PortalPart> createSymmetricCapeLines(double x2, double y, PortalPart portalPart) {
		return createSymmetricLines(0, y, x2, y, portalPart);
	}
}
package eu.epicpvp.kcore.Particle.Cape;

import eu.epicpvp.kcore.Permission.PermissionType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class SupermanCape extends CapeShape {

	public SupermanCape(String name, PermissionType permission, Color outColor, Color inColor, Color middleColor) {
		super(name, permission, outColor, inColor, middleColor);
	}

	@Override
	protected void initShape() {
		getPositions().putAll(createSymmetricLines(0, 1.15, .3, 1.15, CapePart.OUTER));
		getPositions().putAll(createSymmetricLines(.3, 1.15, .6, .2, CapePart.OUTER));
		getPositions().putAll(createSymmetricLines(.6, .25, 0, .2, CapePart.OUTER));
		getPositions().putAll(fill(CapePart.OUTER, CapePart.INNER));
	}
}

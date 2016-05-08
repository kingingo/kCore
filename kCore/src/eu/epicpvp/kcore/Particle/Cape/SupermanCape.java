package eu.epicpvp.kcore.Particle.Cape;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Permission.PermissionType;

public class SupermanCape extends CapeShape{

	public SupermanCape(ItemStack item, PermissionType permission, Color outColor, Color inColor,
			Color middleColor) {
		super(item, permission, outColor, inColor, middleColor);
	}

	@Override
	public void initShape() {
		getPositions().putAll(createSymmetricLines( 0, 1.15, .3, 1.15, CapePart.OUT));
		getPositions().putAll(createSymmetricLines( .3, 1.15, .6, .2, CapePart.OUT));
		getPositions().putAll(createSymmetricLines( .6, .25, 0, .2, CapePart.OUT));
		getPositions().putAll(fill(CapePart.OUT, CapePart.IN));
	}

}

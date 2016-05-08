package eu.epicpvp.kcore.Particle.Wings;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Permission.PermissionType;

public class InsectWings extends WingShape{

	public InsectWings(ItemStack item, PermissionType permission, boolean moveWings, Color outerColor, Color innerColor,
			Color middleColor) {
		super(item, permission, moveWings, outerColor, innerColor, middleColor);
	}

	@Override
	public void initShape() {
		//Mid-mid -> bottom
		getPositions().putAll(createSymmetricLines( 0, 1.5, .5, 1.1, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		getPositions().putAll(createSymmetricLines( .5, 1.1, 1, .7, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		getPositions().putAll(createSymmetricLines( 1, .7, 1.1, .3, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		getPositions().putAll(createSymmetricLines( 1.1, .3, .7 , .25, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		getPositions().putAll(createSymmetricLines( .7 , .25, .2, 1.35, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		
		getPositions().putAll(fill(WingPart.OUTER_RIGHT, WingPart.INNER_RIGHT));
		getPositions().putAll(fill(WingPart.OUTER_LEFT, WingPart.INNER_LEFT));
	}

}

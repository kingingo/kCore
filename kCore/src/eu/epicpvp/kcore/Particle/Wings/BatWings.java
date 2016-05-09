package eu.epicpvp.kcore.Particle.Wings;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Permission.PermissionType;

public class BatWings extends WingShape{

	public BatWings(ItemStack item, PermissionType permission, boolean moveWings, Color outerColor,
			Color innerColor, Color middleColor) {
		super(item, permission, moveWings, outerColor, innerColor, middleColor);
	}
	
	@Override
	public void initShape() {
		//Mid -> first peak
		getPositions().putAll(createSymmetricLines( 0, 1, .6, .7, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> between 1. and 2. peak
		getPositions().putAll(createSymmetricLines( .6, .7, .7, 1, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> 2. peak
		getPositions().putAll(createSymmetricLines( .7, 1, 1, 1, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> between 2.-3. peak
		getPositions().putAll(createSymmetricLines( 1, 1, 1.1, 1.4, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> 3. peak
		getPositions().putAll(createSymmetricLines( 1.1, 1.4, 1.3, 1.6, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//Mid-mid -> bottom
		getPositions().putAll(createSymmetricLines( 1.3, 1.6, 1.2, 1.9, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> between 3.-4. peak
		getPositions().putAll(createSymmetricLines( 1.2, 1.9, 1.3, 2.1, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> top inner
		getPositions().putAll(createSymmetricLines( 1.3, 2.1, 1.1, 1.9, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> rounding top
		getPositions().putAll(createSymmetricLines( 1.1, 1.9, .6, 1.7, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> rounding top #2
		getPositions().putAll(createSymmetricLines( .6, 1.7, .4, 1.3, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> mid
		getPositions().putAll(createSymmetricLines( .4, 1.3, 0.2, 1.2, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		
		getPositions().putAll(fill(WingPart.OUTER_RIGHT,WingPart.INNER_RIGHT));
		getPositions().putAll(fill(WingPart.OUTER_LEFT,WingPart.INNER_LEFT));
//		getPositions().putAll(createSymmetricLines( .2, 1, 1.1, 1.5, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		
		//Peak 1
		getPositions().putAll(createSymmetricLines( .6, .7, .65, .4, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//Peak 2
		getPositions().putAll(createSymmetricLines( 1, 1, 1.1, .8, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//Peak 3
		getPositions().putAll(createSymmetricLines( 1.3, 1.6, 1.5, 1.4, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//devil horns
		getPositions().putAll(createSymmetricLines( .2, 1.9, .7, .3, 2.08, .6, WingPart.MIDDLE, WingPart.MIDDLE));
	}

}

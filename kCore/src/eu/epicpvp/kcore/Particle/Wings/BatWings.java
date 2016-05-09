package eu.epicpvp.kcore.Particle.Wings;

import eu.epicpvp.kcore.Particle.Line;
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
		new Line<>(0, 1, WingPart.OUTER_RIGHT) //mid
				.lineTo(.6, .7) //first peak
				.lineTo(.7, 1) //between 1. and 2. peak
				.lineTo(1, 1) //2. peak
				.lineTo(1.1, 1.4) //between 2.-3. peak
				.lineTo(1.3, 1.6) //3. peak
				.lineTo(1.2, 1.9) //between 3.-4. peak
				.lineTo(1.3, 2.1) //4. peak
				.lineTo(1.1, 1.9) //top inner
				.lineTo(.6, 1.7) //rounding top
				.lineTo(.4, 1.3) //rounding top #2
				.lineTo(.2, 1.2) //back to mid (nearly)
				.makeAllSymmetric(WingPart.OUTER_LEFT)
				.addTo(this);

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

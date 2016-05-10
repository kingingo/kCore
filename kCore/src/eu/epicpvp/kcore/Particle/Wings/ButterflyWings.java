package eu.epicpvp.kcore.Particle.Wings;

import eu.epicpvp.kcore.Particle.Line;
import eu.epicpvp.kcore.Permission.PermissionType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class ButterflyWings extends WingShape {

	public ButterflyWings(String name, PermissionType permission, boolean moveWings, Color outerColor, Color innerColor, Color middleColor) {
		super(name, permission, moveWings, outerColor, innerColor, middleColor);
	}

	@Override
	protected void initShape() {
		//Mid-mid -> bottom
		getPositions().putAll(createSymmetricLines( 0, 1, .6, .2, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//->mid-side
		getPositions().putAll(createSymmetricLines( .6, .2, .5, .8, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//knick
		getPositions().putAll(createSymmetricLines( .5, .8, .1, 1, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//knick ende
		getPositions().putAll(createSymmetricLines( .1, 1, .5, 1.2, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//->top
		getPositions().putAll(createSymmetricLines( .5, 1.2, .6, 2, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//->rounding top->mid
		getPositions().putAll(createSymmetricLines( .6, 2, .3, 1.8, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//->mid-mid
		getPositions().putAll(createSymmetricLines( .3, 1.8, 0, 1, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));

		getPositions().putAll(createSymmetricLines(.5, 1.8, .3, 1.2, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		getPositions().putAll(createSymmetricLines(.3, 0.8, .5, 0.4, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
	}
}

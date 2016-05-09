package eu.epicpvp.kcore.Particle.Wings;

import eu.epicpvp.kcore.Particle.Line;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Permission.PermissionType;

public class ButterflyWings extends WingShape{

	public ButterflyWings(ItemStack item, PermissionType permission, boolean moveWings, Color outerColor,
			Color innerColor, Color middleColor) {
		super(item, permission, moveWings, outerColor, innerColor, middleColor);
	}

	@Override
	public void initShape() {
		new Line<>(0, 1, WingPart.OUTER_RIGHT) //mid-mid
				.lineTo(.6, .2) //bottom
				.lineTo(.5, .8) //mid-side
				.lineTo(.1, 1) //knick
				.lineTo(.5, 1.2) //knick ende
				.lineTo(.6, 2) //top
				.lineTo(.3, 1.8) //rounding top->mid
				.lineTo(0, 1) //back to mid-mid
				.makeAllSymmetric(WingPart.OUTER_LEFT)
				.addTo(this);

		getPositions().putAll(createSymmetricLines( .5, 1.8, .3, 1.2, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		getPositions().putAll(createSymmetricLines( .3, 0.8, .5, 0.4 ,WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
	}

}

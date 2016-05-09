package eu.epicpvp.kcore.Particle.Wings;

import eu.epicpvp.kcore.Particle.Line;
import eu.epicpvp.kcore.Permission.PermissionType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class InsectWings extends WingShape {

	public InsectWings(ItemStack item, PermissionType permission, boolean moveWings, Color outerColor, Color innerColor, Color middleColor) {
		super(item, permission, moveWings, outerColor, innerColor, middleColor);
	}

	@Override
	public void initShape() {
		//Mid-mid -> bottom
		new Line<>(0, 1.5, WingPart.OUTER_RIGHT)
				.lineTo(.5, 1.1)
				.lineTo(1, .7)
				.lineTo(1.1, .3)
				.lineTo(.7, .25)
				.lineTo(.2, 1.35)
				.makeAllSymmetric(WingPart.OUTER_LEFT)
				.addTo(this);

		getPositions().putAll(fill(WingPart.OUTER_RIGHT, WingPart.INNER_RIGHT));
		getPositions().putAll(fill(WingPart.OUTER_LEFT, WingPart.INNER_LEFT));
	}
}

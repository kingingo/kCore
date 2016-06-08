package eu.epicpvp.kcore.Particle.Wings;

import org.bukkit.Color;

import eu.epicpvp.kcore.Permission.PermissionType;

public class InsectWings extends WingShape {

	public InsectWings(String name, PermissionType permission, boolean moveWings, Color outerColor, Color innerColor, Color middleColor) {
		super(name, permission, moveWings, outerColor, innerColor, middleColor);
	}

	@Override
	protected void initShape() {
		//mid -> wing upper line middle
		getPositions().putAll(createSymmetricLines(0, 1.5, .5, 1.1, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//->upper corner
		getPositions().putAll(createSymmetricLines(.5, 1.1, 1, .7, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> lower outer corner
		getPositions().putAll(createSymmetricLines(1, .7, 1.1, .3, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> lower inner corner
		getPositions().putAll(createSymmetricLines(1.1, .3, .7, .25, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//-> middle
		getPositions().putAll(createSymmetricLines(.7, .25, .2, 1.35, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		//inner
		getPositions().putAll(createSymmetricLines(1, .5, .55, 0.95, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		getPositions().putAll(createSymmetricLines(0.8, .4, .55, 0.95, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));
		getPositions().putAll(createSymmetricPoints(0.9, .5, WingPart.INNER_LEFT, WingPart.INNER_RIGHT));

//		getPositions().putAll(fill(WingPart.OUTER_RIGHT, WingPart.INNER_RIGHT));
//		getPositions().putAll(fill(WingPart.OUTER_LEFT, WingPart.INNER_LEFT));
	}
}

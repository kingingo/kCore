package eu.epicpvp.kcore.Particle.Wings;

import eu.epicpvp.kcore.Particle.Line;
import eu.epicpvp.kcore.Permission.PermissionType;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class AngelWings extends WingShape {

	private static final double TOP_CORNER_X = 1.0;
	private static final double TOP_CORNER_Y = 1.7;
	private static final double MIDDLE_UPPER_Y = 1.3;
	private static final double BOTTOM_MIDDLE_Y = .9;
	private static final double WING_MIDDLE_X = .6;
	private static final double WING_MIDDLE_Y = .6;
	private static final double BOTTOM_X = .65;
	private static final double BOTTOM_Y = 0.3;

	private static final double THINKNESS_CONST = .28;

	public AngelWings(ItemStack item, PermissionType permission, boolean moveWings, Color outerColor, Color innerColor, Color middleColor) {
		super(item, permission, moveWings, outerColor, innerColor, middleColor);
	}

	@Override
	public void initShape() {
		// upper line to middle top
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X, TOP_CORNER_Y, 0, MIDDLE_UPPER_Y, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		// top corner to wing middle
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X + .08, TOP_CORNER_Y - .15, WING_MIDDLE_X, WING_MIDDLE_Y, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		// wing middle to bottom
		getPositions().putAll(createSymmetricLines(WING_MIDDLE_X, WING_MIDDLE_Y, BOTTOM_X, BOTTOM_Y, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		// bottom to middle
		getPositions().putAll(createSymmetricLines(BOTTOM_X - .15, BOTTOM_Y, 0, BOTTOM_MIDDLE_Y, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));

		// upper line - inner
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X - THINKNESS_CONST * .8, TOP_CORNER_Y - THINKNESS_CONST, 0, MIDDLE_UPPER_Y - THINKNESS_CONST, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		// upper line - inner second line
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X - THINKNESS_CONST * 2, TOP_CORNER_Y - THINKNESS_CONST * 2, THINKNESS_CONST, MIDDLE_UPPER_Y - THINKNESS_CONST * 2, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		// upper line - inner third line
		getPositions().putAll(createSymmetricLines(TOP_CORNER_X - THINKNESS_CONST, TOP_CORNER_Y - THINKNESS_CONST, THINKNESS_CONST * 1.5, MIDDLE_UPPER_Y - THINKNESS_CONST * 2.5, WingPart.OUTER_LEFT, WingPart.OUTER_RIGHT));
		// lower inner points
		// wingLeft.put(new Vector(-.4, .55, 0), WingPart.INNER);
		// wingRight.put(new Vector(.4, .55, 0), WingPart.INNER);
		getPositions().putAll(createLine(0, BOTTOM_MIDDLE_Y - .1, 0, MIDDLE_UPPER_Y + .1, WingPart.INNER_LEFT));
		getPositions().putAll(createLine(0, BOTTOM_MIDDLE_Y - .11, 0, MIDDLE_UPPER_Y + .1, WingPart.INNER_LEFT));

		getPositions().putAll(createCircle(0, 2.1, .65, 0.3, WingPart.MIDDLE));
		// wingLeft = ImmutableMap.copyOf(wingLeft);
		// wingRight = ImmutableMap.copyOf(wingRight);
	}
}

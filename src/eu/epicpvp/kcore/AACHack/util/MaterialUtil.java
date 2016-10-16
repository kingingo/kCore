package eu.epicpvp.kcore.AACHack.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class MaterialUtil {

	public static boolean canNeverStandOn(Block block) {
		return canNeverStandOn(block.getType());
	}

	public static boolean canNeverStandOn(Material mat) {
		if (!mat.isBlock()) {
			return true;
		}
		switch (mat) {
			case AIR:
			case GRASS:
			case LONG_GRASS:
			case BANNER:
			case STANDING_BANNER:
			case WALL_BANNER:
			case DEAD_BUSH:
			case DOUBLE_PLANT:
			case FIRE:
			case ITEM_FRAME:
			case LEVER:
			case MELON_SEEDS:
			case MELON_STEM:
			case PAINTING:
			case PORTAL:
			case PUMPKIN_STEM:
			case RAILS:
			case RED_ROSE:
			case REDSTONE_WIRE:
			case REDSTONE_TORCH_ON:
			case REDSTONE_TORCH_OFF:
			case SIGN_POST:
			case WALL_SIGN:
			case YELLOW_FLOWER:
				//TODO
				return true;
			default:
				return false;
		}
	}
}

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
				case AIR: //TODO
					return true;
				default:
					return false;
		}
	}
}

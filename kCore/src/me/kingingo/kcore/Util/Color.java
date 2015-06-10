package me.kingingo.kcore.Util;

import org.bukkit.DyeColor;

public class Color {

	public static String GRAY = "§7";
	public static String RED = "§c";
	public static String YELLOW = "§e";
	public static String GREEN = "§a";
	public static String BLACK = "§0";
	public static String BLUE = "§1";
	public static String AQUA = "§b";
	public static String WHITE = "§f";
	public static String ORANGE = "§6";
	public static String PURPLE = "§5";
	public static String PINK = "§d";
	public static String BOLD = "§l";
	
	public static org.bukkit.Color rdmColor(){
		return rdmDyeColor().getColor();
	}
	
	public static DyeColor rdmDyeColor(){
		return DyeColor.values()[ UtilMath.r(DyeColor.values().length) ];
	}
}

package eu.epicpvp.kcore.Util;

import org.bukkit.DyeColor;

public class Color {

	public static String GRAY = "§7";
	public static String DARK_GRAY = "§8";
	public static String RED = "§c";
	public static String DARK_RED = "§4";
	public static String YELLOW = "§e";
	public static String GREEN = "§a";
	public static String BLACK = "§0";
	public static String BLUE = "§1";
	public static String AQUA = "§b";
	public static String WHITE = "§f";
	public static String ORANGE = "§6";
	public static String PURPLE = "§5";
	public static String DARK_GREEN = "§2";
	public static String PINK = "§d";
	public static String CYAN = "§3";

	public static String BOLD = "§l";
	public static String ITALIC = "§o";
	public static String RESET = "§r";
	public static String STRIKE_THROUGH = "§m";
	public static String OBFUSCATED = "§k";

	public static String[] FORMAT = new String[]{BOLD,ITALIC,RESET,STRIKE_THROUGH,OBFUSCATED};
	public static String[] COLORS = new String[]{DARK_GREEN,GRAY,DARK_RED,DARK_GRAY,RED,YELLOW,GREEN,BLACK,BLUE,AQUA,WHITE,ORANGE,PURPLE,PINK,CYAN};
	
	public static org.bukkit.Color toColor(String color){
		switch(color){
		case "§7":return org.bukkit.Color.GRAY;
		case "§8":return org.bukkit.Color.GRAY;
		case "§c":return org.bukkit.Color.RED;
		case "§4":return org.bukkit.Color.RED;
		case "§e":return org.bukkit.Color.YELLOW;
		case "§a":return org.bukkit.Color.LIME;
		case "§0":return org.bukkit.Color.BLACK;
		case "§1":return org.bukkit.Color.BLUE;
		case "§b":return org.bukkit.Color.AQUA;
		case "§f":return org.bukkit.Color.WHITE;
		case "§6":return org.bukkit.Color.ORANGE;
		case "§5":return org.bukkit.Color.PURPLE;
		case "§2":return org.bukkit.Color.GREEN;
		case "§d":return org.bukkit.Color.MAROON;
		case "§3":return org.bukkit.Color.NAVY;
		}
		return org.bukkit.Color.BLACK;
	}
	
	public static org.bukkit.Color rdmColor(){
		return rdmDyeColor().getColor();
	}
	
	public static DyeColor rdmDyeColor(){
		return DyeColor.values()[ UtilMath.randomInteger(DyeColor.values().length) ];
	}
	
	public static boolean isColor(String color){
		if(color.contains("§")||color.contains("&")){
			color=color.replaceAll("&", "§");
			
			if(UtilString.countNumbers("§")>1){
				String[] colors = color.split("§");
				
				for(String c : colors){
					if(!isColor(c)){
						return false;
					}
				}
			}else{
				for(String c : COLORS)if(color.equalsIgnoreCase(c))return true;
				for(String f : FORMAT)if(color.equalsIgnoreCase(f))return true;
				return false;
			}
		}
		return false;
	}
}

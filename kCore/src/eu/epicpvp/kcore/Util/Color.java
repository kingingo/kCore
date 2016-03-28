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
	
	public static org.bukkit.Color rdmColor(){
		return rdmDyeColor().getColor();
	}
	
	public static DyeColor rdmDyeColor(){
		return DyeColor.values()[ UtilMath.r(DyeColor.values().length) ];
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

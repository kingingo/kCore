package eu.epicpvp.kcore.Util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFirework;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class UtilFirework {
	 
	public static Type RandomType(){
		return Type.values()[UtilMath.r(Type.values().length)];
	}
	
	public static Color RandomColor(){
		switch(UtilMath.RandomInt(16, 0)){
		case 0: return Color.AQUA;
		case 1: return Color.BLACK;
		case 2: return Color.BLUE;
		case 3: return Color.FUCHSIA;
		case 4: return Color.GRAY;
		case 5: return Color.GREEN;
		case 6: return Color.LIME;
		case 7: return Color.MAROON;
		case 8: return Color.NAVY;
		case 9: return Color.OLIVE;
		case 10: return Color.ORANGE;
		case 11: return Color.PURPLE;
		case 12: return Color.RED;
		case 13: return Color.SILVER;
		case 14: return Color.TEAL;
		case 15: return Color.WHITE;
		case 16: return Color.YELLOW;
		}
		return Color.AQUA;
	}
	
	public static void start(Location location, Color color, Type type) {
		start(1,location,color,type);
	}
	
	public static void start(int lifeSpan, Location location, Color color, Type type) {
		if(type==null)type=RandomType();
		if(color==null)color=RandomColor();
		 Firework fw = location.getWorld().spawn(location,Firework.class);
         FireworkMeta meta = fw.getFireworkMeta();
         meta.addEffect(FireworkEffect.builder().flicker(false).with(type).trail(false).withColor(color).build());
         fw.setFireworkMeta(meta);
         if(lifeSpan!=-1)((CraftFirework)fw).getHandle().expectedLifespan = lifeSpan;
	}
 
}

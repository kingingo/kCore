package me.kingingo.kcore.Util;
import lombok.Getter;

import org.bukkit.entity.Player;

public class UtilEXP {
	
	public static int getLevelToExp(int level){
		if (level <= 16){
		    return level * level + 6 * level;
		}else if (level <= 31) {
			return (int)(2.5D * level * level - 40.5D * level + 360.0D);
		}else{
			return (int)(4.5D * level * level - 162.5D * level + 2220.0D);
		}
	}
	
	public int getExpToLevel(int lvl){
	    return lvl >= 15 ? 37 + (lvl - 15) * 5 : lvl >= 30 ? 112 + (lvl - 30) * 9 : 7 + lvl * 2;
	}
	
	public static int getPlayerExp(Player player){
		return getLevelToExp(player.getLevel());
	}
}
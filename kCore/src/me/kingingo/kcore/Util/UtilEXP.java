package me.kingingo.kcore.Util;
import lombok.Getter;

import org.bukkit.entity.Player;

public class UtilEXP {
	 @Getter
	 private static int bottleExpEarn = 10;
	
	 public static int levelToExp(int level){
	    if (level <= 15)
	      return level * level + 6 * level;
	    if (level <= 30) {
	      return (int)(2.5D * level * level - 40.5D * level + 360.0D);
	    }
	    return (int)(4.5D * level * level - 162.5D * level + 2220.0D);
	  }

	  public static int deltaLevelToExp(int level){
	    if (level <= 15)
	      return 2 * level + 7;
	    if (level <= 30) {
	      return 5 * level - 38;
	    }
	    return 9 * level - 158;
	  }
	  
	  public static int xptobottles(float xp) {
		  int bottles = (int)Math.ceil(xp / bottleExpEarn);
		  return bottles;
	  }
	  
	  public static int getPlayerExperience(Player player) {
		  int bukkitExp = levelToExp(player.getLevel()) + (int)(deltaLevelToExp(player.getLevel()) * player.getExp());
		  return bukkitExp;
	  }
	  
	  public static int currentlevelxpdelta(Player player){
	    int levelxp = deltaLevelToExp(player.getLevel()) - (levelToExp(player.getLevel()) + (int)(deltaLevelToExp(player.getLevel()) * player.getExp()) - levelToExp(player.getLevel()));
	    return levelxp;
	  }
}
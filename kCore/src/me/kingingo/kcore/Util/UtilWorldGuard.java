package me.kingingo.kcore.Util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;

public class UtilWorldGuard {

	private static WorldGuardPlugin worldGuard;
	private static ArrayList<StateFlag> flags;
	
	public static boolean checkWorldGuard(){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	    if (plugin != null || (plugin instanceof WorldGuardPlugin)) {
	    	worldGuard=(WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	    	return true;
	    }
	    return false;
	}
	
	public static StateFlag getCustomFlag(String flag_name){
		for(StateFlag flag : getFlags()){
			if(flag.getName().equalsIgnoreCase(flag_name)){
				return flag;
			}
		}
		return null;
	}
	
	public static ArrayList<StateFlag> getFlags(){
		if(flags==null)flags=new ArrayList<>();
		return flags;
	}
	
	public static void addCustomFlag(StateFlag flag){
		if(flags==null)flags=new ArrayList<>();
		try {
		      Field flagField = DefaultFlag.class.getField("flagsList");

		      Flag[] flags = new Flag[DefaultFlag.flagsList.length + 1];
		      System.arraycopy(DefaultFlag.flagsList, 0, flags, 0, DefaultFlag.flagsList.length);
		      
		      flags[DefaultFlag.flagsList.length] = flag;

		      if (flag == null) {
		        throw new RuntimeException("flag is null");
		      }

		      UtilReflection.setStaticValue(flagField, flags);
		    }
		    catch (Exception ex) {
		      Bukkit.getServer().getLogger().log(Level.WARNING, "Could not add flag {0} to WorldGuard", flag.getName());
		    }

		    for (int i = 0; i < DefaultFlag.getFlags().length; i++) {
		      Flag flag1 = DefaultFlag.getFlags()[i];
		      if (flag1 == null)
		        throw new RuntimeException("Flag[" + i + "] is null");
		    }
		    flags.add(flag);
	}
	
	public static boolean RegionFlag(Player player,StateFlag flag){
		return RegionFlag(player.getLocation(), flag);
	}
	
	public static boolean canBuild(Player player){
		return canBuild(player.getLocation(), player);
	}
	
	public static boolean canBuild(Location loc ,Player player){
		if(worldGuard==null&&!checkWorldGuard())return false;
		return worldGuard.getRegionManager(loc.getWorld()).getApplicableRegions(loc).canBuild( worldGuard.wrapPlayer(player) );
	}
	
//	public static boolean canUse(Player player){
//		return canUse(player.getLocation(), player);
//	}
//	
//	public static boolean canUse(Location loc, Player player){
//		if(worldGuard==null&&!checkWorldGuard())return false;
//		return !worldGuard.getRegionManager(loc.getWorld()).getApplicableRegions(loc).canUse(worldGuard.wrapPlayer(player));
//	}
	
	public static boolean RegionFlag(Location location,StateFlag flag){
		if(worldGuard==null&&!checkWorldGuard())return false;
		return worldGuard.getRegionManager(location.getWorld()).getApplicableRegions(location).allows(flag);
	}
	
}

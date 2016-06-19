package eu.epicpvp.kcore.Util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;

public class UtilWorldGuard {

	private static WorldGuardPlugin worldGuard;
	private static HashMap<String,Flag> customFlags;
	
	public static boolean checkWorldGuard(){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	    if (plugin != null || (plugin instanceof WorldGuardPlugin)) {
	    	worldGuard=(WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	    	return true;
	    }
	    return false;
	}
	
	public static HashMap<String,Flag> getCustomFlags(){
		if(customFlags==null)customFlags=new HashMap<>();
		return customFlags;
	}
	
	public void loadFlagsForWorld(World world){
	    if (world == null)
	    {
	      System.err.println("World object is null.");
	      return;
	    }
	  
	}
	
	public static synchronized void addCustomFlag(Flag flag)
	  {
	    if (customFlags.containsKey(flag.getName())) {
	      if (!((Flag)customFlags.get(flag.getName())).getClass().equals(flag.getClass()))
	        Bukkit.getServer().getLogger().log(Level.WARNING, "Duplicate flag: {0}", flag.getName());
	    }
	    else {
	      customFlags.put(flag.getName(), flag);
	      addFlag(flag);
	      
	      
	    }
	  }
	
	public static void addFlag(Flag<?> flag){
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

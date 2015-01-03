package me.kingingo.kcore.Bar;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import me.kingingo.kcore.Bar.NMS.FakeDragon;
import me.kingingo.kcore.Bar.NMS.v1_7;
import me.kingingo.kcore.Bar.NMS.v1_8;
import me.kingingo.kcore.Bar.NMS.v1_8Fake;
import me.kingingo.kcore.Util.UtilDisplay;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Bar {
	
    //public static boolean newProtocol = false;
	//public static String version;
	///public static boolean isBelowGround = true;
	public static Map<String, FakeDragon> dragonplayers = new HashMap<String, FakeDragon>();

	public static void setStatus(Player player, String text, int healthpercent){

		FakeDragon dragon = null;
        if(dragonplayers.containsKey(player.getName())){
            dragon = dragonplayers.get(player.getName());
        }else if(!text.equals("")){
            dragon = newDragon(player,text);
            dragonplayers.put(player.getName(), dragon);
            Object mobPacket = dragon.getSpawnPacket();
            UtilPlayer.sendPacket(player, mobPacket);
        }
     
        if(text.equals("") && dragonplayers.containsKey(player.getName())){
            Object destroyPacket = dragon.getDestroyPacket();
            UtilPlayer.sendPacket(player, destroyPacket);
         
            dragonplayers.remove(player.getName());
        }else{
            dragon.name=text;
            dragon.health=(healthpercent/100f)*UtilDisplay.MAX_HEALTH;
            Object metaPacket = dragon.getMetaPacket(dragon.getWatcher());
            Object teleportPacket = dragon.getTeleportPacket(getDragonLocation(player));
            UtilPlayer.sendPacket(player, metaPacket);
            UtilPlayer.sendPacket(player, teleportPacket);
        }
    }
	
	public static FakeDragon newDragon(Player player,String message) {
		 if (UtilPlayer.getVersion(player) >= 47){
			 System.out.println("1.8");
			 FakeDragon fakeDragon = null;
			    try
			    {
			      fakeDragon = (FakeDragon)v1_8Fake.class.getConstructor(new Class[] { String.class, Location.class }).newInstance(new Object[] { message, player.getLocation().add(0, -198, 0) });
			    } catch (IllegalArgumentException e) {
			      e.printStackTrace();
			    } catch (SecurityException e) {
			      e.printStackTrace();
			    } catch (InstantiationException e) {
			      e.printStackTrace();
			    } catch (IllegalAccessException e) {
			      e.printStackTrace();
			    } catch (InvocationTargetException e) {
			      e.printStackTrace();
			    } catch (NoSuchMethodException e) {
			      e.printStackTrace();
			    }

			    return fakeDragon;
		 }else{
			 System.out.println("1.7");
			 FakeDragon fakeDragon = null;
			    try
			    {
			      fakeDragon = (FakeDragon)v1_7.class.getConstructor(new Class[] { String.class, Location.class }).newInstance(new Object[] { message, player.getLocation().add(0, -198, 0) });
			    } catch (IllegalArgumentException e) {
			      e.printStackTrace();
			    } catch (SecurityException e) {
			      e.printStackTrace();
			    } catch (InstantiationException e) {
			      e.printStackTrace();
			    } catch (IllegalAccessException e) {
			      e.printStackTrace();
			    } catch (InvocationTargetException e) {
			      e.printStackTrace();
			    } catch (NoSuchMethodException e) {
			      e.printStackTrace();
			    }

			    return fakeDragon;
		 }
	  }
	
//	public static void detectVersion(){
//	    if (v1_8Fake.isUsable()) {
//	      fakeDragonClass = v1_8Fake.class;
//	      version = "v1_7_R4.";
//	      isBelowGround = false;
//	    } else {
//	      String name = Bukkit.getServer().getClass().getPackage().getName();
//	      String mcVersion = name.substring(name.lastIndexOf(46) + 1);
//	      String[] versions = mcVersion.split("_");
//
//	      if (versions[0].equals("v1")) {
//	        int minor = Integer.parseInt(versions[1]);
//
//	        if (minor == 7) {
//	          newProtocol = true;
//	          fakeDragonClass = v1_7.class;
//	        } else if (minor == 8) {
//	          fakeDragonClass = v1_8.class;
//	          isBelowGround = false;
//	        }
//	      }
//
//	      version = mcVersion + ".";
//	    }
//	  }
	
	private static Location getDragonLocation(Player player) {
		Location loc = player.getLocation();
	    if (UtilPlayer.getVersion(player)>=47) {
	      loc.subtract(0.0D, 300.0D, 0.0D);
	      return loc;
	    }

	    float pitch = loc.getPitch();

	    if (pitch >= 55.0F)
	      loc.add(0.0D, -300.0D, 0.0D);
	    else if (pitch <= -55.0F)
	      loc.add(0.0D, 300.0D, 0.0D);
	    else {
	      loc = loc.getBlock().getRelative(getDirection(loc), Bukkit.getServer().getViewDistance() * 16).getLocation();
	    }

	    return loc;
	  }
	
	private static BlockFace getDirection(Location loc) {
	    float dir = Math.round(loc.getYaw() / 90.0F);
	    if ((dir == -4.0F) || (dir == 0.0F) || (dir == 4.0F))
	      return BlockFace.SOUTH;
	    if ((dir == -1.0F) || (dir == 3.0F))
	      return BlockFace.EAST;
	    if ((dir == -2.0F) || (dir == 2.0F))
	      return BlockFace.NORTH;
	    if ((dir == -3.0F) || (dir == 1.0F))
	      return BlockFace.WEST;
	    return null;
	  }
	
	
}

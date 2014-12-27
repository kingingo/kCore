package me.kingingo.kcore.Util;

import me.kingingo.kcore.Nick.Events.BroadcastMessageEvent;
import me.kingingo.kcore.Nick.Events.PlayerSendMessageEvent;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

public class UtilServer
{
	
	public static void spawnRabbit(Location loc){
		final Skeleton skel1 = loc.getWorld().spawn(loc, Skeleton.class);
	    final Skeleton skel2 = loc.getWorld().spawn(loc, Skeleton.class);
	    skel1.setCustomName("Dinnerbone");
	    skel1.setCustomNameVisible(false);
	    skel2.setCustomNameVisible(false);
        skel2.setPassenger(skel1);
	  
	    
        ItemStack hasenkopf = UtilItem.Head("rabbit2077");
        ItemStack bauch = new ItemStack(Material.WOOL,1,(byte)8);//UtilItem.Head("Hunter_Comm");
	    skel2.getEquipment().setHelmet(hasenkopf);
	    skel1.getEquipment().setHelmet(bauch);
	    skel1.getEquipment().setItemInHand(new ItemStack(0));
	    skel2.getEquipment().setItemInHand(new ItemStack(Material.CARROT_ITEM));
	    
	    skel1.setNoDamageTicks(Integer.MAX_VALUE);
	    skel2.setNoDamageTicks(Integer.MAX_VALUE);
	    
	    Bukkit.getScheduler().runTaskTimer(null, new Runnable() {
			
			@Override
			public void run() {
               skel1.setCustomNameVisible(false);
       	       skel2.setCustomNameVisible(false);
       	       ((CraftEntity) skel1).getHandle().setPositionRotation(skel2.getLocation().getX(), skel2.getLocation().getY(),skel2.getLocation().getZ(), 0F, 0F);
			}
		}, 1, 1);
	}
	
  public static Player[] getPlayers()
  {
    return Bukkit.getServer().getOnlinePlayers();
  }

  public static Server getServer()
  {
    return Bukkit.getServer();
  }

  public static void broadcast(String message)
  {
	BroadcastMessageEvent ev = new BroadcastMessageEvent(message);
	Bukkit.getPluginManager().callEvent(ev);  
    for (Player cur : getPlayers())
    	UtilPlayer.sendMessage(cur, ev.getMessage());
      //UtilPlayer.message(cur, message);
  }

  public static void broadcastSpecial(String event, String message)
  {
	BroadcastMessageEvent ev = new BroadcastMessageEvent(message);
	Bukkit.getPluginManager().callEvent(ev);  
    for (Player cur : getPlayers())
    {
    //  UtilPlayer.message(cur, "§b§l" + event);
    //  UtilPlayer.message(cur, message);
    	UtilPlayer.sendMessage(cur, ev.getMessage());
      cur.playSound(cur.getLocation(), Sound.ORB_PICKUP, 2.0F, 0.0F);
      cur.playSound(cur.getLocation(), Sound.ORB_PICKUP, 2.0F, 0.0F);
    }
  }

  public static double getFilledPercent()
  {
    return getPlayers().length / getServer().getMaxPlayers();
  }
}
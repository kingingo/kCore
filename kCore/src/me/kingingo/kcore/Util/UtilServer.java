package me.kingingo.kcore.Util;

import me.kingingo.kcore.Nick.Events.BroadcastMessageEvent;
import me.kingingo.kcore.Nick.Events.PlayerSendMessageEvent;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Player;

public class UtilServer
{
	
	private static UtilFirework firework;
	
	public static void playFirework(Location loc, Color color, Type t) {
    	firework = new UtilFirework();
		try {
			firework.playFirework(loc.getWorld(), loc, FireworkEffect.builder().with(t).withColor(color).build());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

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
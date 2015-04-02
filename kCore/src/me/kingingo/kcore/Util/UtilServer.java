package me.kingingo.kcore.Util;

import java.io.PrintStream;

import lombok.Getter;
import me.kingingo.kcore.LogHandler.Event.LogEvent;
import me.kingingo.kcore.Nick.Events.BroadcastMessageEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.earth2me.essentials.Essentials;

public class UtilServer{
	
	@Getter
	private static boolean loghandleradded = false;
	
	public static void addLogHandler(){
		if(!isLoghandleradded()){
		//s	Bukkit.getLogger().getLogger("Minecraft").addHandler(arg0);
			
			System.setOut(new PrintStream(System.out) {
		        @Override
		        public void println(String s) {
		            super.println(s);
		    		Bukkit.getPluginManager().callEvent(new LogEvent(s));
		        }
		    });
			
			if(Bukkit.getPluginManager().getPlugin("ProtocolLib")!=null){
				ProtocolManager pro = ProtocolLibrary.getProtocolManager();
				PrintStream print = (PrintStream)UtilReflection.getValue("output", pro.getAsynchronousManager().getErrorReporter());
				if(print!=null){
					UtilReflection.setValue("output", pro.getAsynchronousManager().getErrorReporter(), new PrintStream(print) {
				        @Override
				        public void println(String s) {
				            super.println(s);
				    		Bukkit.getPluginManager().callEvent(new LogEvent(s));
				        }
				    });
				}else{
					System.out.println("[kCore] ProtocolLib PrintStream == NULL");
				}
			}else{
				System.setErr(new PrintStream(System.err) {
			        @Override
			        public void println(String s) {
			            super.println(s);
			    		Bukkit.getPluginManager().callEvent(new LogEvent(s));
			        }
			    });
			}
		}
	}
	
	public static void essPermissionHandler(){
		if(Bukkit.getPluginManager().getPlugin("Essentials")!=null&&Bukkit.getPluginManager().getPlugin("Essentials").isEnabled()){
			Essentials ess = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
			
			try{
				boolean b =(boolean) UtilReflection.getValue("useSuperperms", ess.getPermissionsHandler());
				System.out.println("[EpicPvP] Essentials PermissionHandler "+b);
			}catch(Exception e){
				System.out.println("[EpicPvP] Error: "+e.getMessage());
			}
			
			ess.getPermissionsHandler().setUseSuperperms(true);
			ess.getPermissionsHandler().checkPermissions();
		}
	}
	
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
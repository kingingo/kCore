package me.kingingo.kcore.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.DeliveryPet.DeliveryPet;
import me.kingingo.kcore.GemsShop.GemsShop;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Monitor.LagMeter;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Packet.Packets.TEAM_MESSAGE;
import me.kingingo.kcore.PacketAPI.packetlistener.kPacketListener;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Update.Updater;
import me.kingingo.kcore.UpdateAsync.UpdaterAsync;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;

public class UtilServer{
	
	@Getter
	private static boolean loghandleradded = false;
	@Getter
	@Setter
	private static kPacketListener packetListener;
	@Getter
	@Setter
	private static LagMeter lagMeter;
	@Getter
	@Setter
	private static DeliveryPet deliveryPet;
	@Getter
	@Setter
	private static GemsShop gemsShop;
	@Getter
	@Setter
	private static Updater updater;
	@Getter
	@Setter
	private static UpdaterAsync updaterAsync;
	
	public static UpdaterAsync createUpdaterAsync(JavaPlugin instance){
		if(updaterAsync==null)updaterAsync=new UpdaterAsync(instance);
		return updaterAsync;
	}
	
	public static Updater createUpdater(JavaPlugin instance){
		if(updater==null)updater=new Updater(instance);
		return updater;
	}
	
	public static GemsShop createGemsShop(GemsShop gemShop){
		if(gemsShop==null&&gemShop!=null)gemsShop=gemShop;
		return gemsShop;
	}
	
	public static DeliveryPet createDeliveryPet(DeliveryPet pet){
		if(deliveryPet==null&&pet!=null)deliveryPet=pet;
		return deliveryPet;
	}
	
	public static LagMeter createLagListener(CommandHandler handler){
		if(lagMeter==null&&handler!=null)lagMeter=new LagMeter(handler);
		return lagMeter;
	}
	
	public static kPacketListener createPacketListener(JavaPlugin instance){
		if(packetListener==null)packetListener=new kPacketListener(instance);
		return packetListener;
	}
	
	public static void DebugLog(long time,String[] Reason,String c){
		System.err.println("[DebugMode]: Class: "+c);
		for(String r : Reason){
			System.err.println("[DebugMode]: Reason: "+r);
		}
		System.err.println("[DebugMode]: Zeit: "+ ((System.currentTimeMillis()-time) / 1000.0D) + " Seconds");
	}
	
	public static void DebugLog(long time,String Reason,String c){
		System.err.println("[DebugMode]: Class: "+c);
		System.err.println("[DebugMode]: Reason: "+Reason);
		System.err.println("[DebugMode]: Zeit: "+ ((System.currentTimeMillis()-time) / 1000.0D) + " Seconds");
	}
	
	public static void DebugLog(long time,String c){
		System.err.println("[DebugMode]: Class: "+c);
		System.err.println("[DebugMode]: Zeit: "+ ((System.currentTimeMillis()-time) / 1000.0D) + " Seconds");
	}
	
	public static void DebugLog(String m){
		System.err.println("[DebugMode]: "+m);
	}
	
	public static List<Integer> showLine(Location loc, String text) {
		
	      WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
	      EntityWitherSkull skull = new EntityWitherSkull(world);
	      skull.setLocation(loc.getX(), loc.getY() + 1 + 55, loc.getZ(), 0, 0);
	      PacketPlayOutSpawnEntity skull_packet = new PacketPlayOutSpawnEntity(skull,skull.getId());
	 
	      EntityHorse horse = new EntityHorse(world);
	      horse.setLocation(loc.getX(), loc.getY() + 55, loc.getZ(), 0, 0);
	      horse.setAge(-1700000);
	      horse.setCustomName(text);
	      horse.setCustomNameVisible(true);
	      PacketPlayOutSpawnEntityLiving packedt = new PacketPlayOutSpawnEntityLiving(horse);
	      for (Player player : loc.getWorld().getPlayers()) {
	         EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
	         nmsPlayer.playerConnection.sendPacket(packedt);
	         nmsPlayer.playerConnection.sendPacket(skull_packet);
	 
	         PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse, skull);
	         nmsPlayer.playerConnection.sendPacket(pa);
	      }
	      return Arrays.asList(skull.getId(), horse.getId());
	   }
	
	public static void sendTeamMessage(String message,PacketManager packetManager){
		
		if(message.contains("%=%")){
			String[] split = message.split("%=%");
			for(String msg : split){
				for(Player p : getPlayers())if(p.hasPermission(kPermission.TEAM_MESSAGE.getPermissionToString()))p.sendMessage(msg);
			}
		}else{
			for(Player p : getPlayers())if(p.hasPermission(kPermission.TEAM_MESSAGE.getPermissionToString()))p.sendMessage(message);
		}
		
		packetManager.SendPacket("BG", new TEAM_MESSAGE(message));
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
	
  public static Collection<? extends Player> getPlayers(){
    return Bukkit.getServer().getOnlinePlayers();
  }

  public static Server getServer()
  {
    return Bukkit.getServer();
  }

  public static void broadcastLanguage(String name,Object input){
	    for (Player cur : getPlayers())UtilPlayer.sendMessage(cur, Language.getText("PREFIX")+Language.getText(cur, name,input));
  }
  
  public static void broadcastLanguage(String name,Object[] input){
	    for (Player cur : getPlayers())UtilPlayer.sendMessage(cur, Language.getText("PREFIX")+Language.getText(cur, name,input));
  }
  
  public static void broadcastLanguage(String name){
	    for (Player cur : getPlayers())UtilPlayer.sendMessage(cur, Language.getText("PREFIX")+Language.getText(cur, name));
  }
  
  public static void broadcast(String message){
    for (Player cur : getPlayers())UtilPlayer.sendMessage(cur, message);
  }

  public static double getFilledPercent()
  {
    return getPlayers().size() / getServer().getMaxPlayers();
  }
}
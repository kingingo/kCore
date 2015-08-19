package me.kingingo.kcore.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Packet.Packets.TEAM_MESSAGE;
import me.kingingo.kcore.PacketAPI.packetlistener.kPacketListener;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.lag.Lag;
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
	private static kPacketListener listener;
	private static Lag lag;
	
	public static void createLagListener(JavaPlugin instance){
		if(lag==null)lag=new Lag(instance);
	}
	
	public static void createPacketListener(JavaPlugin instance){
		if(listener==null)listener=new kPacketListener(instance);
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
	
//	public static PacketPlayOutSpawnEntityLiving getCreateHorse(Location location,String text){
//		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
//	    UtilReflection.setValue("a", packet, 100);
//	    UtilReflection.setValue("b", packet, ((byte)EntityType.HORSE.getTypeId()));
//	    UtilReflection.setValue("c", packet, EnumEntitySize.SIZE_2.a(location.getBlockX()));
//	    UtilReflection.setValue("d", packet, MathHelper.floor(location.getBlockY() * 32.0D));
//	    UtilReflection.setValue("e", packet, EnumEntitySize.SIZE_2.a(location.getBlockZ()));
//	    UtilReflection.setValue("i", packet, ((byte)(int)(location.getYaw() * 256.0F / 360.0F)));
//	    UtilReflection.setValue("j", packet, ((byte)(int)(location.getPitch() * 256.0F / 360.0F)));
//	    DataWatcher d = new DataWatcher(null);
//        d.a(10, text);
//        d.a(11, (byte) 1);
//        d.a(12, -1700000);
//        UtilReflection.setValue("l", packet,d);
//        return packet;
//	}
//	
//	public static PacketPlayOutSpawnEntity getCreateSkull(Location location){
//		PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity();
//		UtilReflection.setValue("a", packet, 66);
//	    UtilReflection.setValue("b", packet, MathHelper.floor(location.getX() * 32.0D));
//	    UtilReflection.setValue("c", packet, MathHelper.floor(location.getY() * 32.0D));
//	    UtilReflection.setValue("d", packet, MathHelper.floor(location.getZ() * 32.0D));
//	    UtilReflection.setValue("h", packet, MathHelper.d(location.getPitch() * 256.0F / 360.0F));
//	    UtilReflection.setValue("i", packet, MathHelper.d(location.getYaw() * 256.0F / 360.0F));
//	    UtilReflection.setValue("j", packet, MathHelper.d(location.getYaw() * 256.0F / 360.0F));
//	    UtilReflection.setValue("k", packet, MathHelper.d(location.getYaw() * 256.0F / 360.0F));
//        return packet;
//	}
//	
//	public static void spawnHologram(String text,Player player,Location location) {
//		PacketPlayOutSpawnEntity skull = getCreateSkull(location);
//		PacketPlayOutSpawnEntityLiving horse = getCreateHorse(location, text);
//		PacketPlayOutAttachEntity attach = new PacketPlayOutAttachEntity();
//		
//		UtilReflection.setValue("a", attach, 1);
//		UtilReflection.setValue("b", attach, UtilReflection.getValue("a", horse));
//		UtilReflection.setValue("c", attach, (skull != null ? UtilReflection.getValue("a", skull) : -1));
//		((CraftPlayer)player).getHandle().playerConnection.sendPacket(horse);
//		((CraftPlayer)player).getHandle().playerConnection.sendPacket(skull);
//		((CraftPlayer)player).getHandle().playerConnection.sendPacket(attach);
//	}
//	
//	public static void addLogHandler(){
//		if(!isLoghandleradded()){
//			System.setOut(new PrintStream(System.out) {
//		        @Override
//		        public void println(String s) {
//		            super.println(s);
//		    		Bukkit.getPluginManager().callEvent(new LogEvent(s));
//		        }
//		    });
//			
//				ProtocolManager pro = ProtocolLibrary.getProtocolManager();
//				
//				PrintStream print = (PrintStream)UtilReflection.getValue("output", pro.getAsynchronousManager().getErrorReporter());
//				if(print!=null){
//					UtilReflection.setValue("output", pro.getAsynchronousManager().getErrorReporter(), new PrintStream(print) {
//				        @Override
//				        public void println(String s) {
//				            super.println(s);
//				    		Bukkit.getPluginManager().callEvent(new LogEvent(s));
//				        }
//				    });
//				}else{
//					System.out.println("[kCore] ProtocolLib PrintStream == NULL");
//				}
//				
//				System.setErr(new PrintStream(System.err) {
//			        @Override
//			        public void println(String s) {
//			            super.println(s);
//			    		Bukkit.getPluginManager().callEvent(new LogEvent(s));
//			        }
//			    });
//				
//		}
//	}
	
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
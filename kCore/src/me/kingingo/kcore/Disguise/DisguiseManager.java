package me.kingingo.kcore.Disguise;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Disguise.disguises.DisguiseInsentient;
import me.kingingo.kcore.NPC.Event.PlayerInteractNPCEvent;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerEntityDestroy;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerNamedEntitySpawn;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerSpawnEntityLiving;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.ScheduledPacket;

public class DisguiseManager implements Listener {

	@Getter
	JavaPlugin instance;
	@Getter
	private ArrayList<DisguiseBase> disguise = new ArrayList<>();
	private HashMap<Integer,ArrayList<Player>> see = new HashMap<>();
	
	public DisguiseManager(JavaPlugin instance){
		this.instance=instance;
		
//		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Server.NAMED_ENTITY_SPAWN){
//		 	@Override
//			public void onPacketSending(PacketEvent event) {
//	        if(event.getPacketType() == PacketType.Play.Server.NAMED_ENTITY_SPAWN){
//	            Player player = event.getPlayer();
//	            System.err.println("PLAYER: "+player.getName());
//	            try {
//	                PacketContainer packet = event.getPacket();
//	                WrapperPlayServerNamedEntitySpawn pa = new WrapperPlayServerNamedEntitySpawn(packet);
//	                	for(DisguiseBase db : disguise){
//	                		if(db.GetEntityId()==pa.getEntityID()){
//	                			sendPacket(player, new PacketPlayOutEntityDestroy(new int[] { db.GetEntityId() }));
//	                			event.setPacket(db.GetSpawnPacket());
//	                		}
//	                }
//	            } catch (Exception e){
//	            	System.err.println("[DisguiseManager] Error: ");
//	            	e.printStackTrace();
//	            }
//	        }
//	    }
//	});
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Server.NAMED_ENTITY_SPAWN){
		 	@Override
			public void onPacketSending(PacketEvent event) {
	        if(event.getPacketType() == PacketType.Play.Server.NAMED_ENTITY_SPAWN){
	            Player player = event.getPlayer();
	            System.err.println("PLAYER: "+player.getName());
	            try {
	                PacketContainer packet = event.getPacket();
	                WrapperPlayServerNamedEntitySpawn pa = new WrapperPlayServerNamedEntitySpawn(packet);
	                	for(DisguiseBase db : disguise){
	                		if(db.GetEntityId()==pa.getEntityID()){
	                			event.schedule(new ScheduledPacket(new WrapperPlayServerEntityDestroy(new int[] { db.GetEntityId() }).getHandle(),player,false));
	                			event.schedule(new ScheduledPacket(db.GetSpawnPacket(),player,false));
	                		}
	                }
	            } catch (Exception e){
	            	System.err.println("[DisguiseManager] Error: ");
	            	e.printStackTrace();
	            }
	        }
	    }
	});
		
		Bukkit.getPluginManager().registerEvents(this, getInstance());
	}
	
	public boolean isDisguise(LivingEntity entity){
		boolean b = false;
		for(DisguiseBase disguise : getDisguise()){
			if(entity.getEntityId()==disguise.GetEntityId()){
				b=true;
				break;
			}
		}
		return b;
	}
	
	public DisguiseBase getDisguise(LivingEntity entity){
		DisguiseBase disguise = null;
		for(DisguiseBase db : getDisguise()){
			if(entity.getEntityId()==db.GetEntityId()){
				disguise=db;
				break;
			}
		}
		return disguise;
	}
	
	public void sendPacket(Player player,Packet packet){
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public void sendPacket(Player player,PacketContainer packet){
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
		} catch (InvocationTargetException e) {
			System.err.println("[DisguiseManager] Error: ");
			e.printStackTrace();
		}
	}
	
	public void disguise(Player player,LivingEntity entity,DisguiseType type){
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type, null);
		if(!getDisguise().contains(disguise))getDisguise().add(disguise);
		sendPacket(player, new PacketPlayOutEntityDestroy(new int[] { disguise.GetEntityId() }));
		sendPacket(player, disguise.GetSpawnPacket());
	}
	
	public void disguise(Player player,LivingEntity entity,DisguiseType type,Object[] o){
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type, o);
		if(!getDisguise().contains(disguise))getDisguise().add(disguise);
		sendPacket(player, new PacketPlayOutEntityDestroy(new int[] { disguise.GetEntityId() }));
		sendPacket(player, disguise.GetSpawnPacket());
	}
	
	public void disguise(Player player,DisguiseBase disguise){
		if(!getDisguise().contains(disguise))getDisguise().add(disguise);
		sendPacket(player, new PacketPlayOutEntityDestroy(new int[] { disguise.GetEntityId() }));
		sendPacket(player, disguise.GetSpawnPacket());
	}
	
	public void disguise(LivingEntity entity,DisguiseType type){
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type,null);
		for(Player player : UtilServer.getPlayers()){
			if(disguise.GetEntity() != ((CraftPlayer)player).getHandle()){
				disguise(player, disguise);
			}
		}
	}
	
	public void disguise(LivingEntity entity,DisguiseType type,Object[] o){
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type, o);
		for(Player player : UtilServer.getPlayers()){
			if(disguise.GetEntity() != ((CraftPlayer)player).getHandle()){
				disguise(player, disguise);
			}
		}
	}
	
	public void disguise(DisguiseBase disguise){
		for(Player player : UtilServer.getPlayers()){
			if(disguise.GetEntity() != ((CraftPlayer)player).getHandle()){
				disguise(player, disguise);
			}
		}
	}
	
	public void undisguiseAll(){
		for(Player player : UtilServer.getPlayers()){
			undisguise(player);
		}
		getDisguise().clear();
	}
	
	public void undisguise(LivingEntity entity){
		if(isDisguise(entity)){
			DisguiseBase disguise = getDisguise(entity);
		    PacketPlayOutEntityDestroy de = new PacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
		    PacketPlayOutNamedEntitySpawn s = new PacketPlayOutNamedEntitySpawn( ((CraftPlayer)entity).getHandle() );
			for(Player player : UtilServer.getPlayers()){
				if(entity!=player){
					sendPacket(player, de);
					sendPacket(player, s);
					if(entity instanceof Player){
						player.showPlayer(((Player)entity));
					}
				}
			}
			getDisguise().remove(disguise);
		}
	}

	public void updateDisguise(DisguiseBase disguise){
	    for (Player player : UtilServer.getPlayers()){
	      if (disguise.GetEntity() != ((CraftPlayer)player).getHandle()){
	    	  sendPacket(player, disguise.GetMetaDataPacket());
	      }
	    }
	}
	
	  @EventHandler
	  public void TeleportDisguises(UpdateEvent event)
	  {
	    if (event.getType() != UpdateType.SEC)return;
	    for (Player player : UtilServer.getPlayers()){
	      for (Player otherPlayer : UtilServer.getPlayers()){
	        if (player != otherPlayer){
	          if (otherPlayer.getLocation().subtract(0.0D, 0.5D, 0.0D).getBlock().getTypeId() != 0)
	            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityTeleport(((CraftPlayer)otherPlayer).getHandle()));
	        }
	      }
	    }
	  }
	  
	  @EventHandler
	  public void PlayerQuit(PlayerQuitEvent event){
	    undisguise(event.getPlayer());
	  }
	  
}

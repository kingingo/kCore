package me.kingingo.kcore.Disguise;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Disguise.disguises.livings.DisguisePlayer;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Events.PacketSendEvent;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.PacketAPI.Packets.kDataWatcher;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityDestroy;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityEquipment;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityMetadata;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityTeleport;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityVelocity;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutNamedEntitySpawn;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo.kPlayerInfoData;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntity;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntityLiving;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerReceiveEvent;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerSendEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DisguiseManager extends kListener {

	@Getter
	JavaPlugin instance;
	@Getter
	private HashMap<Integer,DisguiseBase> disguise = new HashMap<>();
	
	public DisguiseManager(JavaPlugin instance){
		super(instance,"DisguiseManager");
		this.instance=instance;
		UtilServer.createPacketListener(instance);
	}
	
	kPacketPlayOutSpawnEntityLiving entityLiving;
	kPacketPlayOutNamedEntitySpawn namedEntitySpawn;
	kPacketPlayOutEntityMetadata entityMetadata;
	kPacketPlayOutPlayerInfo info;
	kPlayerInfoData data;
	@EventHandler
	public void Send(PacketListenerSendEvent ev){
		if(ev.getPlayer()!=null){
			if(ev.getPacket() instanceof PacketPlayOutSpawnEntityLiving){
				if( entityLiving == null )entityLiving=new kPacketPlayOutSpawnEntityLiving();
				entityLiving.setPacket(((PacketPlayOutSpawnEntityLiving)ev.getPacket()));
				if(ev.getPlayer().getEntityId()!=entityLiving.getEntityID()&&getDisguise().containsKey(entityLiving.getEntityID())){
					ev.setPacket(getDisguise().get(entityLiving.getEntityID()).GetSpawnPacket().getPacket());
				}
			}else if(ev.getPacket() instanceof PacketPlayOutNamedEntitySpawn){
				if( namedEntitySpawn == null )namedEntitySpawn=new kPacketPlayOutNamedEntitySpawn();
				namedEntitySpawn.setPacket(((PacketPlayOutNamedEntitySpawn)ev.getPacket()));
				if(ev.getPlayer().getEntityId()!=namedEntitySpawn.getEntityID()&&getDisguise().containsKey(namedEntitySpawn.getEntityID())){
					if(getDisguise().get(namedEntitySpawn.getEntityID()) instanceof DisguisePlayer)sendPacket(ev.getPlayer(), ((DisguisePlayer)getDisguise().get(namedEntitySpawn.getEntityID())).getTabList());
					ev.setPacket(getDisguise().get(namedEntitySpawn.getEntityID()).GetSpawnPacket().getPacket());
				}
			}else if(ev.getPacket() instanceof PacketPlayOutEntityMetadata){
				if( entityMetadata == null )entityMetadata=new kPacketPlayOutEntityMetadata();
				entityMetadata.setPacket(((PacketPlayOutEntityMetadata)ev.getPacket()));
				if(ev.getPlayer().getEntityId()!=entityMetadata.getEntityID()&&getDisguise().containsKey(entityMetadata.getEntityID())){
					ev.setPacket( getDisguise().get(entityMetadata.getEntityID()).GetMetaDataPacket().getPacket());
				}
			}
		}
	}
	
	public boolean isDisguise(LivingEntity entity){
		return getDisguise().containsKey(entity.getEntityId());
	}
	
	public DisguiseBase getDisguise(LivingEntity entity){
		if(!isDisguise(entity))return null;
		return getDisguise().get(entity.getEntityId());
	}
	
	public void sendPacket(Player player,kPacket packet){
		UtilPlayer.sendPacket(player, packet);
	}
	
	private void disguise(Player player,LivingEntity entity,DisguiseType type){
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type, null);
		if(!getDisguise().containsKey(entity.getEntityId()))getDisguise().put(entity.getEntityId(),disguise);
		sendPacket(player, new kPacketPlayOutEntityDestroy(new int[] { disguise.GetEntityId() }));
		if(type==DisguiseType.PLAYER)sendPacket(player, ((DisguisePlayer)disguise).getTabList());
		sendPacket(player, disguise.GetSpawnPacket());
	}
	
	private void disguise(Player player,LivingEntity entity,DisguiseType type,Object[] o){
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type, o);
		if(!getDisguise().containsKey(entity.getEntityId()))getDisguise().put(entity.getEntityId(),disguise);
		sendPacket(player, new kPacketPlayOutEntityDestroy(new int[] { disguise.GetEntityId() }));
		if(type==DisguiseType.PLAYER)sendPacket(player, ((DisguisePlayer)disguise).getTabList());
		sendPacket(player, disguise.GetSpawnPacket());
	}
	
	private void disguise(Player player,DisguiseBase disguise){
		if(!getDisguise().containsKey(disguise.GetEntityId()))getDisguise().put(disguise.GetEntityId(),disguise);
		sendPacket(player, new kPacketPlayOutEntityDestroy(new int[] { disguise.GetEntityId() }));
		if(disguise instanceof DisguisePlayer)sendPacket(player, ((DisguisePlayer)disguise).getTabList());
		sendPacket(player, disguise.GetSpawnPacket());
	}
	
	public void disguise(LivingEntity entity,DisguiseType type){
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type,new String[]{entity.getName()});
		if(!getDisguise().containsKey(disguise.GetEntityId()))getDisguise().put(disguise.GetEntityId(),disguise);
		for(Player player : UtilServer.getPlayers()){
			if(disguise.GetEntity() != ((CraftPlayer)player).getHandle()){
				disguise(player, disguise);
			}
		}
	}
	
	public void disguise(LivingEntity entity,DisguiseType type,Object[] o){
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type, o);
		if(!getDisguise().containsKey(entity.getEntityId()))getDisguise().put(entity.getEntityId(),disguise);
		for(Player player : UtilServer.getPlayers()){
			if(disguise.GetEntity() != ((CraftPlayer)player).getHandle()){
				disguise(player, disguise);
			}
		}
	}
	
	public void disguise(DisguiseBase disguise){
		if(!getDisguise().containsKey(disguise.GetEntityId()))getDisguise().put(disguise.GetEntityId(),disguise);
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
		    kPacketPlayOutEntityDestroy de = new kPacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
		    kPacketPlayOutNamedEntitySpawn s = new kPacketPlayOutNamedEntitySpawn( ((CraftPlayer)entity).getHandle() );
			for(Player player : UtilServer.getPlayers()){
				if(entity!=player){
					sendPacket(player, de);
					if(disguise instanceof DisguisePlayer)sendPacket(player, ((DisguisePlayer)disguise).removeFromTablist());
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
	            UtilPlayer.sendPacket(player, new kPacketPlayOutEntityTeleport(((CraftPlayer)otherPlayer).getHandle()) );
	        }
	      }
	    }
	  }
	  
	  @EventHandler
	  public void PlayerQuit(PlayerQuitEvent event){
	    undisguise(event.getPlayer());
	  }
}

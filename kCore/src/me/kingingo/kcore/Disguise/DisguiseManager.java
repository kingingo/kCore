package me.kingingo.kcore.Disguise;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Disguise.Events.DisguiseCreateEvent;
import me.kingingo.kcore.Disguise.Events.DisguiseEntityLivingEvent;
import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Disguise.disguises.DisguiseInsentient;
import me.kingingo.kcore.Disguise.disguises.livings.DisguisePlayer;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityDestroy;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityMetadata;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityTeleport;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutNamedEntitySpawn;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutSpawnEntityLiving;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerSendEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DisguiseManager extends kListener {

	@Getter
	private JavaPlugin instance;
	@Getter
	private HashMap<Integer,DisguiseBase> disguise = new HashMap<>();
	@Getter
	@Setter
	private DisguiseShop disguiseShop;
	
	public DisguiseManager(JavaPlugin instance){
		super(instance,"DisguiseManager");
		this.instance=instance;
		UtilServer.createPacketListener(instance);
	}
	
	@EventHandler
	public void Send(PacketListenerSendEvent ev){
		if(ev.getPlayer()!=null&&ev.getPacket()!=null){
			try{
				if(ev.getPacket() instanceof PacketPlayOutSpawnEntityLiving){
					kPacketPlayOutSpawnEntityLiving entityLiving=new kPacketPlayOutSpawnEntityLiving(((PacketPlayOutSpawnEntityLiving)ev.getPacket()));
					
					if(ev.getPlayer().getEntityId()!=entityLiving.getEntityID()&&getDisguise().containsKey(entityLiving.getEntityID())&&getDisguise().get(entityLiving.getEntityID())!=null){
						if(getDisguise().get(entityLiving.getEntityID()) instanceof DisguisePlayer)sendPacket(ev.getPlayer(), ((DisguisePlayer)getDisguise().get(entityLiving.getEntityID())).getTabList());
						ev.setPacket(getDisguise().get(entityLiving.getEntityID()).GetSpawnPacket().getPacket());
					}
					entityLiving.setPacket(null);
					entityLiving=null;
				}else if(ev.getPacket() instanceof PacketPlayOutNamedEntitySpawn){
					kPacketPlayOutNamedEntitySpawn namedEntitySpawn=new kPacketPlayOutNamedEntitySpawn(((PacketPlayOutNamedEntitySpawn)ev.getPacket()));
					
					if(ev.getPlayer().getEntityId()!=namedEntitySpawn.getEntityID()&&getDisguise().containsKey(namedEntitySpawn.getEntityID())){
						if(getDisguise().get(namedEntitySpawn.getEntityID()) instanceof DisguisePlayer)sendPacket(ev.getPlayer(), ((DisguisePlayer)getDisguise().get(namedEntitySpawn.getEntityID())).getTabList());
						ev.setPacket(getDisguise().get(namedEntitySpawn.getEntityID()).GetSpawnPacket().getPacket());
					}
					namedEntitySpawn.setPacket(null);
					namedEntitySpawn=null;
				}else if(ev.getPacket() instanceof PacketPlayOutEntityMetadata){
					kPacketPlayOutEntityMetadata entityMetadata=new kPacketPlayOutEntityMetadata(((PacketPlayOutEntityMetadata)ev.getPacket()));
					
					if(ev.getPlayer().getEntityId()!=entityMetadata.getEntityID()&&getDisguise().containsKey(entityMetadata.getEntityID())){
						ev.setPacket( getDisguise().get(entityMetadata.getEntityID()).GetMetaDataPacket().getPacket());
					}
					entityMetadata.setPacket(null);
					entityMetadata=null;
				}
			}catch(Exception e){
				e.printStackTrace();
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
	
	private void disguise(Player player,DisguiseBase disguise){
		if(!getDisguise().containsKey(disguise.GetEntityId()))getDisguise().put(disguise.GetEntityId(),disguise);
		sendPacket(player, new kPacketPlayOutEntityDestroy(new int[] { disguise.GetEntityId() }));
		if(disguise instanceof DisguisePlayer)sendPacket(player, ((DisguisePlayer)disguise).getTabList());
		sendPacket(player, disguise.GetSpawnPacket());
		Bukkit.getPluginManager().callEvent(new DisguiseEntityLivingEvent(this, disguise, player));
	}
	
	public void disguise(LivingEntity entity,DisguiseType type){
		undisguise(entity);
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type,new String[]{entity.getName()});
		Bukkit.getPluginManager().callEvent(new DisguiseCreateEvent(this, disguise, entity));
		if(!getDisguise().containsKey(disguise.GetEntityId()))getDisguise().put(disguise.GetEntityId(),disguise);
		for(Player player : UtilServer.getPlayers()){
			if(disguise.GetEntity() != ((CraftPlayer)player).getHandle()){
				disguise(player, disguise);
			}
		}
		Bukkit.getPluginManager().callEvent(new DisguiseEntityLivingEvent(this, disguise, entity));
	}
	
	public void disguise(LivingEntity entity,DisguiseType type,Object[] o){
		undisguise(entity);
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type, o);
		Bukkit.getPluginManager().callEvent(new DisguiseCreateEvent(this, disguise, entity));
		if(!getDisguise().containsKey(entity.getEntityId()))getDisguise().put(entity.getEntityId(),disguise);
		for(Player player : UtilServer.getPlayers()){
			if(disguise.GetEntity() != ((CraftPlayer)player).getHandle()){
				disguise(player, disguise);
			}
		}
		Bukkit.getPluginManager().callEvent(new DisguiseEntityLivingEvent(this, disguise, entity));
	}
	
	public void disguise(DisguiseBase disguise){
		if(!getDisguise().containsKey(disguise.GetEntityId()))getDisguise().put(disguise.GetEntityId(),disguise);
		for(Player player : UtilServer.getPlayers()){
			if(disguise.GetEntity() != ((CraftPlayer)player).getHandle()){
				disguise(player, disguise);
			}
		}
		if(disguise.GetEntity() instanceof LivingEntity)Bukkit.getPluginManager().callEvent(new DisguiseEntityLivingEvent(this, disguise,(LivingEntity) disguise.GetEntity()));
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
			getDisguise().remove(entity.getEntityId());
		    kPacketPlayOutEntityDestroy de = new kPacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
		    kPacketPlayOutNamedEntitySpawn s = new kPacketPlayOutNamedEntitySpawn( ((CraftPlayer)entity).getHandle() );
			for(Player player : UtilServer.getPlayers()){
				if(entity.getEntityId()!=player.getEntityId()){
					sendPacket(player, de);
					if(disguise instanceof DisguisePlayer)sendPacket(player, ((DisguisePlayer)disguise).removeFromTablist());
					sendPacket(player, s);
					if(entity instanceof Player){
						player.showPlayer(((Player)entity));
					}
				}
			}
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
	public void create(DisguiseCreateEvent ev){
		if(ev.getEntity() instanceof Player && ev.getBase() instanceof DisguiseInsentient){
			UtilPlayer.sendHovbarText(((Player)ev.getEntity()), "�eDisguise �7� �a�l"+ ((DisguiseInsentient)ev.getBase()).GetEntityTypeId().name());
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
		  if(isDisguise(event.getPlayer())){
				DisguiseBase disguise = getDisguise(event.getPlayer());
				getDisguise().remove(event.getPlayer().getEntityId());
			    kPacketPlayOutEntityDestroy de = new kPacketPlayOutEntityDestroy(new int[] { event.getPlayer().getEntityId() });
				for(Player player : UtilServer.getPlayers()){
					if(event.getPlayer().getEntityId()!=player.getEntityId()){
						sendPacket(player, de);
						if(disguise instanceof DisguisePlayer)sendPacket(player, ((DisguisePlayer)disguise).removeFromTablist());
					}
				}
			}
	  }
}

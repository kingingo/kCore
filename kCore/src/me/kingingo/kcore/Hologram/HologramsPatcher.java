package me.kingingo.kcore.Hologram;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Hologram.nametags.Events.HologramCreateEvent;
import me.kingingo.kcore.Hologram.nametags.Events.HologramRemoveEvent;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerAttachEntity;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerEntityDestroy;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerEntityMetadata;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerEntityTeleport;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerNamedEntitySpawn;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerSpawnEntityLiving;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.ScheduledPacket;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

public class HologramsPatcher implements Listener
{
  private final double VERTICAL_OFFSET = 56.799999999999997D;
  private ArrayList<Player> list = new ArrayList<>();
  private ArrayList<Integer> packet = new ArrayList<>();

  public HologramsPatcher(Plugin plugin){
    System.out.println("[HologramPatcher] Aktiviert!");
    Bukkit.getPluginManager().registerEvents(this, plugin);
    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGHEST, new PacketType[] { 
    	      PacketType.Play.Server.ATTACH_ENTITY, 
    	      PacketType.Play.Server.SPAWN_ENTITY_LIVING, 
    	      PacketType.Play.Server.ENTITY_METADATA, 
    	      PacketType.Play.Server.ENTITY_TELEPORT }){
	 	@Override
		public void onPacketSending(PacketEvent event) {
	 		    if (!(UtilPlayer.getVersion(event.getPlayer())>=47)){
	 		      return;
	 		    }

	 		    if (event.isCancelled()) {
	 		      return;
	 		    }

	 		    if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
	 		      WrapperPlayServerSpawnEntityLiving spawnLivingPacket = new WrapperPlayServerSpawnEntityLiving(event.getPacket());
	 		      Entity entity = spawnLivingPacket.getEntity(event);

	 		      if (entity == null) {
	 		        return;
	 		      }

	 		      if ((spawnLivingPacket.getType() == EntityType.HORSE) && (packet.contains(entity.getEntityId()))){
	 		    	System.out.println("SPAWN ENTITY LIVING == "+event.getPlayer().getName());
	 		        spawnLivingPacket.setY(spawnLivingPacket.getY() - 56.799999999999997D);
	 		        spawnLivingPacket.setType(30);

	 		        List metadata = spawnLivingPacket.getMetadata().getWatchableObjects();

	 		        if (metadata != null) {
	 		          pruneUselessIndexes(metadata);
	 		          metadata.add(new WrappedWatchableObject(0, Byte.valueOf((byte)32)));
	 		          spawnLivingPacket.setMetadata(new WrappedDataWatcher(metadata));
	 		        }
	 		      }
	 		    }
	 		    else if (event.getPacketType() == PacketType.Play.Server.ATTACH_ENTITY)
	 		    {
	 		      WrapperPlayServerAttachEntity attachPacket = new WrapperPlayServerAttachEntity(event.getPacket());

	 		      Entity vehicle = attachPacket.getVehicle(event);
	 		      if ((vehicle != null) && (vehicle.getType() == EntityType.WITHER_SKULL) && (packet.contains(vehicle.getEntityId())))
	 		      {
		 		    	System.out.println("ATTACH == "+event.getPlayer().getName());
	 		        Entity passenger = attachPacket.getEntity(event);

	 		        if ((passenger != null) && (passenger.getType() == EntityType.HORSE)) {
	 		          event.setCancelled(true);
	 		        }
	 		      }
	 		    }
	 		    else if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA)
	 		    {
	 		      WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(event.getPacket());
	 		      Entity entity = metadataPacket.getEntity(event);

	 		      if (entity == null) {
	 		        return;
	 		      }

	 		      if (packet.contains(entity.getEntityId()))
	 		      {
		 		    	System.out.println("METADATA == "+event.getPlayer().getName());
	 		        if (entity.getType() == EntityType.HORSE)
	 		        {
	 		          List metadata = metadataPacket.getEntityMetadata();
	 		          pruneUselessIndexes(metadata);
	 		          metadataPacket.setEntityMetadata(metadata);
	 		        }
	 		      }
	 		    } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_TELEPORT)
	 		    {
	 		      WrapperPlayServerEntityTeleport teleportPacket = new WrapperPlayServerEntityTeleport(event.getPacket());

	 		      Entity entity = teleportPacket.getEntity(event);

	 		      if (entity == null) {
	 		        return;
	 		      }

	 		      if ((entity.getType() == EntityType.WITHER_SKULL) && (entity.getPassenger() != null) && (entity.getPassenger().getType() == EntityType.HORSE) && (packet.contains(entity.getEntityId())))
	 		      {
		 		    	System.out.println("TELEPORT == "+event.getPlayer().getName());
	 		        teleportPacket.setEntityID(entity.getPassenger().getEntityId());
	 		        teleportPacket.setY(teleportPacket.getY() - 56.799999999999997D);
	 		      }
	 		    }
	 	}
    });
    
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void HologramsR(HologramRemoveEvent ev){
	  packet.remove(ev.getNts().getHorse().getEntityID());
	  packet.remove(ev.getNts().getSkull().getEntityID());
	  packet.remove(ev.getNts().getAttach().getEntityId());
  }

  @EventHandler(priority=EventPriority.LOWEST)
  public void HologramsC(HologramCreateEvent ev){
	  packet.add(ev.getNts().getHorse().getEntityID());
	  packet.add(ev.getNts().getSkull().getEntityID());
	  packet.add(ev.getNts().getAttach().getEntityId());
  }
  
//  @EventHandler
//	public void Quit(PlayerQuitEvent ev){
//		if(list.contains(ev.getPlayer()))list.remove(ev.getPlayer());
//	}
//	
//	@EventHandler(priority=EventPriority.LOWEST)
//	public void Join(PlayerJoinEvent ev){
//		if(UtilPlayer.getVersion(ev.getPlayer())>=47)list.add(ev.getPlayer());
//	}

  private void pruneUselessIndexes(List<WrappedWatchableObject> metadata) {
    Iterator iter = metadata.iterator();

    while (iter.hasNext()) {
      int index = ((WrappedWatchableObject)iter.next()).getIndex();

      if ((index != 2) && (index != 3))
        iter.remove();
    }
  }
}
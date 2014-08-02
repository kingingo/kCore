package me.kingingo.kcore.Disguise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Disguise.disguises.DisguiseInsentient;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DisguiseManager implements Listener {

	@Getter
	JavaPlugin instance;
	@Getter
	private HashMap<Integer,DisguiseBase> disguise = new HashMap<>();
	
	public DisguiseManager(JavaPlugin instance){
		this.instance=instance;
		Bukkit.getPluginManager().registerEvents(this, getInstance());
	}
	
	public boolean isDisguise(LivingEntity entity){
		return getDisguise().containsKey(entity.getEntityId());
	}
	
	public DisguiseBase getDisguise(LivingEntity entity){
		return getDisguise().get(entity.getEntityId());
	}
	
	public void disguise(LivingEntity entity,DisguiseType type){
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type);
		if (!disguise.GetEntity().isAlive()) {
		      return;
		    }
	    this.disguise.put(Integer.valueOf(disguise.GetEntityId()), disguise);

	    reApplyDisguise(disguise);
	}
	
	public void disguise(DisguiseBase disguise){
	    if (!disguise.GetEntity().isAlive()) {
	      return;
	    }
	    this.disguise.put(Integer.valueOf(disguise.GetEntityId()), disguise);

	    reApplyDisguise(disguise);
	}

	public void undisguise(LivingEntity entity)
	  {
	    if (!this.disguise.containsKey(Integer.valueOf(entity.getEntityId()))) {
	      return;
	    }
	    PacketPlayOutEntityDestroy de = new PacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
	    PacketPlayOutNamedEntitySpawn s = new PacketPlayOutNamedEntitySpawn( ((CraftPlayer)entity).getHandle() );
	    
	    
	    for (Player player : Bukkit.getOnlinePlayers())
	    {
	      if (entity != player)
	      {
	        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
	        entityPlayer.playerConnection.sendPacket(de);
	        entityPlayer.playerConnection.sendPacket(s);
//	        if ((entity instanceof Player)){
//	          player.showPlayer(((Player)entity));
//	        }
//	        else
//	        {
//	          entityPlayer.playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(((CraftLivingEntity)entity).getHandle()));
//	        }
	      }
	    }
	    
	    //Packet20NamedEntitySpawn p20 = new Packet20NamedEntitySpawn(p22.getHandle());
	    this.disguise.remove(Integer.valueOf(entity.getEntityId()));
//	    this._movePacketMap.remove(Integer.valueOf(entity.getEntityId()));
//	    this._moveTempMap.remove(Integer.valueOf(entity.getEntityId()));
	  }

	  public void reApplyDisguise(final DisguiseBase disguise)
	  {
	    for (Player player : Bukkit.getOnlinePlayers())
	    {
	      if (disguise.GetEntity() != ((CraftPlayer)player).getHandle())
	      {
	        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();

	        entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[] { disguise.GetEntityId() }));
	      }
	    }
	    List tempArmor = new ArrayList();

	    if (((disguise instanceof DisguiseInsentient)) && ((disguise.GetEntity() instanceof LivingEntity)))
	    {
	      if (((DisguiseInsentient)disguise).armorVisible())
	      {
	        for (Packet armorPacket : ((DisguiseInsentient)disguise).getArmorPackets()) {
	          tempArmor.add(armorPacket);
	        }
	      }
	    }
	    final List<Packet> armorPackets = tempArmor;

	    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getInstance(), new Runnable()
	    {
	      public void run()
	      {
	        for (Player player : Bukkit.getOnlinePlayers())
	        {
	          if (disguise.GetEntity() != ((CraftPlayer)player).getHandle())
	          {
	            EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
	            entityPlayer.playerConnection.sendPacket(disguise.GetSpawnPacket());

	            for (Packet packet : armorPackets)
	            {
	              entityPlayer.playerConnection.sendPacket(packet);
	            }
	          }
	        }
	      }
	    });
	  }
	  

	  public void reApplyDisguiseOnly(final Player player,final DisguiseBase disguise)
	  {
	      if (disguise.GetEntity() != ((CraftPlayer)player).getHandle())
	      {
	        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();

	        entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[] { disguise.GetEntityId() }));
	      }
	    List tempArmor = new ArrayList();

	    if (((disguise instanceof DisguiseInsentient)) && ((disguise.GetEntity() instanceof LivingEntity)))
	    {
	      if (((DisguiseInsentient)disguise).armorVisible())
	      {
	        for (Packet armorPacket : ((DisguiseInsentient)disguise).getArmorPackets()) {
	          tempArmor.add(armorPacket);
	        }
	      }
	    }
	    final List<Packet> armorPackets = tempArmor;

	    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getInstance(), new Runnable()
	    {
	      public void run()
	      {
	          if (disguise.GetEntity() != ((CraftPlayer)player).getHandle())
	          {
	            EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
	            entityPlayer.playerConnection.sendPacket(disguise.GetSpawnPacket());

	            for (Packet packet : armorPackets)
	            {
	              entityPlayer.playerConnection.sendPacket(packet);
	            }
	        }
	      }
	    });
	  }

	  public void updateDisguise(DisguiseBase disguise) {
	    for (Player player : Bukkit.getOnlinePlayers())
	    {
	      if (disguise.GetEntity() != ((CraftPlayer)player).getHandle())
	      {
	        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();

	        entityPlayer.playerConnection.sendPacket(disguise.GetMetaDataPacket());
	      }
	    }
	  }
	  
	  @EventHandler
	  public void TeleportDisguises(UpdateEvent event)
	  {
	    if (event.getType() != UpdateType.SEC) {
	      return;
	    }
	    for (Player player : Bukkit.getOnlinePlayers())
	    {
	      for (Player otherPlayer : Bukkit.getOnlinePlayers())
	      {
	        if (player != otherPlayer)
	        {
	          if (otherPlayer.getLocation().subtract(0.0D, 0.5D, 0.0D).getBlock().getTypeId() != 0)
	            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityTeleport(((CraftPlayer)otherPlayer).getHandle()));
	        }
	      }
	    }
	  }
	  
	  @EventHandler(priority=EventPriority.HIGHEST)
	  public void PlayerJoin(final PlayerJoinEvent ev){
		  Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), new Runnable(){

			@Override
			public void run() {
				 for(DisguiseBase disguise : getDisguise().values()){
					 System.err.println("FERTIG");
					 reApplyDisguiseOnly(ev.getPlayer(), disguise);
				 }
			}
			  
		  },20*2);
	  }
	  
	  @EventHandler
	  public void PlayerQuit(PlayerQuitEvent event){
	    undisguise(event.getPlayer());
	  }
	  
	  @EventHandler
	  public void ChunkUnload(ChunkUnloadEvent event) {
	    for (org.bukkit.entity.Entity entity : event.getChunk().getEntities())
	    {
	      if (this.disguise.containsKey(Integer.valueOf(entity.getEntityId())))
	      {
	        //this._entityDisguiseMap.put(entity.getUniqueId().toString(), (DisguiseBase)this.getDisguise().get(Integer.valueOf(entity.getEntityId())));
	        this.disguise.remove(Integer.valueOf(entity.getEntityId()));
	      }
	    }
	  }
	  
}

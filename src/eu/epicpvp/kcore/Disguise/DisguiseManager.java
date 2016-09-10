package eu.epicpvp.kcore.Disguise;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Disguise.Events.DisguiseCreateEvent;
import eu.epicpvp.kcore.Disguise.Events.DisguiseEntityLivingEvent;
import eu.epicpvp.kcore.Disguise.disguises.DisguiseBase;
import eu.epicpvp.kcore.Disguise.disguises.DisguiseInsentient;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguisePlayer;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPacketPlayOutEntityDestroy;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPacketPlayOutEntityMetadata;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPacketPlayOutEntityTeleport;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPacketPlayOutNamedEntitySpawn;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPacketPlayOutSpawnEntityLiving;
import eu.epicpvp.kcore.PacketAPI.packetlistener.event.PacketListenerSendEvent;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

public class DisguiseManager extends kListener {

	@Getter
	private Plugin instance;
	@Getter
	private HashMap<Integer, DisguiseBase> disguise = new HashMap<>();
	@Getter
	@Setter
	private DisguiseShop disguiseShop;

	public DisguiseManager(Plugin instance) {
		super(instance, "DisguiseManager");
		this.instance = instance;
	}

	@EventHandler
	public void Send(PacketListenerSendEvent ev) {
		if (ev.getPlayer() != null && ev.getPacket() != null) {
			try {
				if (ev.getPacket() instanceof PacketPlayOutSpawnEntityLiving) {
					WrapperPacketPlayOutSpawnEntityLiving entityLiving = new WrapperPacketPlayOutSpawnEntityLiving(
							((PacketPlayOutSpawnEntityLiving) ev.getPacket()));

					if (ev.getPlayer().getEntityId() != entityLiving.getEntityID()
							&& getDisguise().containsKey(entityLiving.getEntityID())
							&& getDisguise().get(entityLiving.getEntityID()) != null) {
						if (getDisguise().get(entityLiving.getEntityID()) instanceof DisguisePlayer){
							DisguisePlayer dplayer = ((DisguisePlayer) getDisguise().get(entityLiving.getEntityID()));
							sendPacket(ev.getPlayer(), dplayer.getTabList());
							if(!dplayer.isTab()){
								Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), new Runnable() {
									
									@Override
									public void run() {
										sendPacket(ev.getPlayer(),dplayer.removeFromTablist());
										
									}
								},20*4);
							}
						}
						
						ev.setPacket(getDisguise().get(entityLiving.getEntityID()).GetSpawnPacket().getPacket());
					}
					entityLiving.setPacket(null);
					entityLiving = null;
				} else if (ev.getPacket() instanceof PacketPlayOutNamedEntitySpawn) {
					WrapperPacketPlayOutNamedEntitySpawn namedEntitySpawn = new WrapperPacketPlayOutNamedEntitySpawn(
							((PacketPlayOutNamedEntitySpawn) ev.getPacket()));

					if (ev.getPlayer().getEntityId() != namedEntitySpawn.getEntityID()
							&& getDisguise().containsKey(namedEntitySpawn.getEntityID())) {
						if (getDisguise().get(namedEntitySpawn.getEntityID()) instanceof DisguisePlayer)
							sendPacket(ev.getPlayer(),
									((DisguisePlayer) getDisguise().get(namedEntitySpawn.getEntityID())).getTabList());
						ev.setPacket(getDisguise().get(namedEntitySpawn.getEntityID()).GetSpawnPacket().getPacket());
					}
					namedEntitySpawn.setPacket(null);
					namedEntitySpawn = null;
				} else if (ev.getPacket() instanceof PacketPlayOutEntityMetadata) {
					WrapperPacketPlayOutEntityMetadata entityMetadata = new WrapperPacketPlayOutEntityMetadata(
							((PacketPlayOutEntityMetadata) ev.getPacket()));

					if (ev.getPlayer().getEntityId() != entityMetadata.getEntityID()
							&& getDisguise().containsKey(entityMetadata.getEntityID())) {
						ev.setPacket(getDisguise().get(entityMetadata.getEntityID()).GetMetaDataPacket().getPacket());
					}
					entityMetadata.setPacket(null);
					entityMetadata = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isDisguise(LivingEntity entity) {
		return getDisguise().containsKey(entity.getEntityId());
	}

	public DisguiseBase getDisguise(LivingEntity entity) {
		if (!isDisguise(entity))
			return null;
		return getDisguise().get(entity.getEntityId());
	}

	public void sendPacket(Player player, PacketWrapper packet) {
		UtilPlayer.sendPacket(player, packet);
	}

	private void disguise(Player player, DisguiseBase disguise) {
		if (!getDisguise().containsKey(disguise.GetEntityId()))
			getDisguise().put(disguise.GetEntityId(), disguise);
		sendPacket(player, new WrapperPacketPlayOutEntityDestroy(new int[] { disguise.GetEntityId() }));
		if (disguise instanceof DisguisePlayer)
			sendPacket(player, ((DisguisePlayer) disguise).getTabList());
		sendPacket(player, disguise.GetSpawnPacket());
		Bukkit.getPluginManager().callEvent(new DisguiseEntityLivingEvent(this, disguise, player));
	}

	public void disguise(LivingEntity entity, DisguiseType type) {
		undisguise(entity);
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type, new String[] { entity.getName() });
		Bukkit.getPluginManager().callEvent(new DisguiseCreateEvent(this, disguise, entity));
		if (!getDisguise().containsKey(disguise.GetEntityId()))
			getDisguise().put(disguise.GetEntityId(), disguise);
		for (Player player : UtilServer.getPlayers()) {
			if (disguise.GetEntity() != ((CraftPlayer) player).getHandle()) {
				disguise(player, disguise);
			}
		}
		Bukkit.getPluginManager().callEvent(new DisguiseEntityLivingEvent(this, disguise, entity));
	}

	public void disguise(LivingEntity entity, DisguiseType type, Object[] o) {
		undisguise(entity);
		DisguiseBase disguise = DisguiseType.newDisguise(entity, type, o);
		Bukkit.getPluginManager().callEvent(new DisguiseCreateEvent(this, disguise, entity));
		if (!getDisguise().containsKey(entity.getEntityId()))
			getDisguise().put(entity.getEntityId(), disguise);
		for (Player player : UtilServer.getPlayers()) {
			if (disguise.GetEntity() != ((CraftPlayer) player).getHandle()) {
				disguise(player, disguise);
			}
		}
		Bukkit.getPluginManager().callEvent(new DisguiseEntityLivingEvent(this, disguise, entity));
	}

	public void disguise(DisguiseBase disguise) {
		if (!getDisguise().containsKey(disguise.GetEntityId()))
			getDisguise().put(disguise.GetEntityId(), disguise);
		for (Player player : UtilServer.getPlayers()) {
			if (disguise.GetEntity() != ((CraftPlayer) player).getHandle()) {
				disguise(player, disguise);
			}
		}
		if (disguise.GetEntity() instanceof LivingEntity)
			Bukkit.getPluginManager()
					.callEvent(new DisguiseEntityLivingEvent(this, disguise, (LivingEntity) disguise.GetEntity()));
	}

	public void undisguiseAll() {
		for (Player player : UtilServer.getPlayers()) {
			undisguise(player);
		}
		getDisguise().clear();
	}


	public void undisguise(int entityId) {
		if(getDisguise().containsKey(entityId)){
			DisguiseBase disguise = getDisguise().get(entityId);
			getDisguise().remove(entityId);
			WrapperPacketPlayOutEntityDestroy de = new WrapperPacketPlayOutEntityDestroy(new int[] { entityId });
			for (Player player : UtilServer.getPlayers()){
				sendPacket(player, de);
				if (disguise instanceof DisguisePlayer)
					sendPacket(player, ((DisguisePlayer) disguise).removeFromTablist());
			}
		}
	}
	
	public void undisguise(LivingEntity entity) {
		if (isDisguise(entity)) {
			DisguiseBase disguise = getDisguise(entity);
			getDisguise().remove(entity.getEntityId());
			WrapperPacketPlayOutEntityDestroy de = new WrapperPacketPlayOutEntityDestroy(new int[] { entity.getEntityId() });
			WrapperPacketPlayOutNamedEntitySpawn s = new WrapperPacketPlayOutNamedEntitySpawn(((CraftPlayer) entity).getHandle());
			for (Player player : UtilServer.getPlayers()) {
				if (entity.getEntityId() != player.getEntityId()) {
					sendPacket(player, de);
					if (disguise instanceof DisguisePlayer)
						sendPacket(player, ((DisguisePlayer) disguise).removeFromTablist());
					sendPacket(player, s);
					if (entity instanceof Player) {
						player.showPlayer(((Player) entity));
					}
				}
			}
		}
	}

	public void updateDisguise(DisguiseBase disguise) {
		for (Player player : UtilServer.getPlayers()) {
			if (disguise.GetEntity() != ((CraftPlayer) player).getHandle()) {
				sendPacket(player, disguise.GetMetaDataPacket());
			}
		}
	}

	@EventHandler
	public void create(DisguiseCreateEvent ev) {
		if (ev.getEntity() instanceof Player && ev.getBase() instanceof DisguiseInsentient) {
			UtilPlayer.sendHovbarText(((Player) ev.getEntity()),
					"§eDisguise §7§ §a§l" + ((DisguiseInsentient) ev.getBase()).GetEntityTypeId().name());
		}
	}

	@EventHandler
	public void TeleportDisguises(UpdateEvent event) {
		if (event.getType() != UpdateType.SEC)
			return;
		for (Player player : UtilServer.getPlayers()) {
			for (Player otherPlayer : UtilServer.getPlayers()) {
				if (player != otherPlayer) {
					if (otherPlayer.getLocation().subtract(0.0D, 0.5D, 0.0D).getBlock().getTypeId() != 0)
						UtilPlayer.sendPacket(player,
								new WrapperPacketPlayOutEntityTeleport(((CraftPlayer) otherPlayer).getHandle()));
				}
			}
		}
	}

	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event) {
		if (isDisguise(event.getPlayer())) {
			DisguiseBase disguise = getDisguise(event.getPlayer());
			getDisguise().remove(event.getPlayer().getEntityId());
			WrapperPacketPlayOutEntityDestroy de = new WrapperPacketPlayOutEntityDestroy(
					new int[] { event.getPlayer().getEntityId() });
			for (Player player : UtilServer.getPlayers()) {
				if (event.getPlayer().getEntityId() != player.getEntityId()) {
					sendPacket(player, de);
					if (disguise instanceof DisguisePlayer)
						sendPacket(player, ((DisguisePlayer) disguise).removeFromTablist());
				}
			}
		}
	}
}

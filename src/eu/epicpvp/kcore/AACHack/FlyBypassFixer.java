package eu.epicpvp.kcore.AACHack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.comphenix.packetwrapper.WrapperPlayServerEntityTeleport;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import eu.epicpvp.kcore.AACHack.util.MaterialUtil;
import eu.epicpvp.kcore.Util.UtilServer;
import me.konsolas.aac.api.AACAPI;
import me.konsolas.aac.api.AACAPIProvider;
import me.konsolas.aac.api.HackType;
import org.bukkit.util.Vector;

class FlyBypassFixer extends PacketAdapter implements Listener {

	private Cache<UUID, Vector> doNotFlagShort = CacheBuilder.newBuilder()
			.expireAfterWrite(250, TimeUnit.MILLISECONDS)
			.build();

	private Cache<UUID, Location> doNotFlagJoinTeleportDeath = CacheBuilder.newBuilder()
			.expireAfterWrite(5, TimeUnit.SECONDS)
			.build();

	public FlyBypassFixer() {
		super(new AdapterParameteters()
				.plugin(UtilServer.getPluginInstance())
				.clientSide()
				.listenerPriority(ListenerPriority.HIGHEST)
				.types(PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.FLYING));
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		doNotFlagJoinTeleportDeath.put(event.getPlayer().getUniqueId(), event.getPlayer().getLocation());
	}

	@EventHandler(ignoreCancelled = true)
	public void onTeleport(PlayerTeleportEvent event) {
		doNotFlagJoinTeleportDeath.put(event.getPlayer().getUniqueId(), event.getTo());
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		doNotFlagJoinTeleportDeath.put(event.getEntity().getUniqueId(), event.getEntity().getLocation());
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		doNotFlagJoinTeleportDeath.put(event.getPlayer().getUniqueId(), event.getRespawnLocation());
	}

	@Override
	public void onPacketReceiving(PacketEvent event) {
		Player plr = event.getPlayer();
		if (plr.getGameMode() == GameMode.CREATIVE || plr.getGameMode() == GameMode.SPECTATOR || plr.getAllowFlight() || plr.getVehicle() != null) {
			return;
		}
		if (!AACAPIProvider.isAPILoaded()) {
			return;
		}
		AACAPI api = AACAPIProvider.getAPI();
		if (!api.isEnabled(HackType.NOFALL)) {
			return;
		}
		final PacketContainer packet = event.getPacket();
		Location location;
		StructureModifier<Double> doubles = packet.getDoubles();
		if (event.getPacketType() != PacketType.Play.Client.POSITION_LOOK && event.getPacketType() != PacketType.Play.Client.POSITION) {
			location = plr.getLocation();
		} else {
			location = new Location(plr.getWorld(), doubles.read(0), doubles.read(1), doubles.read(2));
		}
		int baseChunkX = location.getBlockX() >> 4;
		int baseChunkZ = location.getBlockZ() >> 4;
		//@formatter:off
		if (       !location.getWorld().isChunkLoaded(baseChunkX - 1, baseChunkZ - 1)
				|| !location.getWorld().isChunkLoaded(baseChunkX - 1, baseChunkZ)
				|| !location.getWorld().isChunkLoaded(baseChunkX - 1, baseChunkZ + 1)
				|| !location.getWorld().isChunkLoaded(baseChunkX,     baseChunkZ - 1)
				|| !location.getWorld().isChunkLoaded(baseChunkX,     baseChunkZ)
				|| !location.getWorld().isChunkLoaded(baseChunkX,     baseChunkZ + 1)
				|| !location.getWorld().isChunkLoaded(baseChunkX + 1, baseChunkZ - 1)
				|| !location.getWorld().isChunkLoaded(baseChunkX + 1, baseChunkZ)
				|| !location.getWorld().isChunkLoaded(baseChunkX + 1, baseChunkZ + 1)) {
			return;
		}
		//@formatter:on
		Location noFlagLocation = doNotFlagJoinTeleportDeath.getIfPresent(plr.getUniqueId());
		Vector doNotFlagShortPresent = doNotFlagShort.getIfPresent(plr.getUniqueId());
		if (noFlagLocation == null && doNotFlagShortPresent != null) {
			doubles.write(0, doNotFlagShortPresent.getX());
			doubles.write(1, doNotFlagShortPresent.getY() - .97);
			doubles.write(2, doNotFlagShortPresent.getZ());
			WrapperPlayServerEntityTeleport teleport = new WrapperPlayServerEntityTeleport();
			teleport.setEntityID(plr.getEntityId());
			teleport.setX(doNotFlagShortPresent.getX());
			teleport.setY(doNotFlagShortPresent.getY() - .97);
			teleport.setZ(doNotFlagShortPresent.getZ());
			teleport.setYaw(90);
			teleport.setPitch(90);
			teleport.sendPacket(plr);
		}
		Boolean sentOnGround = packet.getBooleans().read(0);
		if (!sentOnGround) {
			return;
		}
		try {
			boolean couldBeOnGroundSimple = couldBeOnGroundSimple(location);
			if (couldBeOnGroundSimple && api.getViolationLevel(plr, HackType.NOFALL) < 2) {
				return;
			}
			boolean onGround;
			try {
				onGround = AACAccessor.isOnGround(location);
			} catch (ReflectiveOperationException ex) {
				ex.printStackTrace();
				return;
			}
			if (!onGround) {
				packet.getBooleans().write(0, false);
				plr.getVelocity().setX(0).setY(0).setZ(0);
				Bukkit.getScheduler().runTaskLater(UtilServer.getPluginInstance(), () -> plr.getVelocity().setX(0).setY(0).setZ(0), 1);
				Bukkit.getScheduler().runTaskLater(UtilServer.getPluginInstance(), () -> plr.getVelocity().setX(0).setY(0).setZ(0), 2);
				if (!couldBeOnGroundSimple) {
					//Revert wrong movement without server-side teleport
					Location setbackLocation = plr.getLocation();
					doubles.write(0, setbackLocation.getX());
					doubles.write(1, setbackLocation.getY() - .97);
					doubles.write(2, setbackLocation.getZ());
					WrapperPlayServerEntityTeleport teleport = new WrapperPlayServerEntityTeleport();
					teleport.setEntityID(plr.getEntityId());
					teleport.setX(setbackLocation.getX());
					teleport.setY(setbackLocation.getY() - .97);
					teleport.setZ(setbackLocation.getZ());
					teleport.setYaw(setbackLocation.getYaw());
					teleport.setPitch(setbackLocation.getPitch());
					teleport.sendPacket(plr);
					if (noFlagLocation != null && (!noFlagLocation.getWorld().equals(location.getWorld()) || location.distanceSquared(noFlagLocation) < 5)) {
						return;
					}
					if (doNotFlagShortPresent != null) {
						return;
					}
					System.out.println("[FlyBypassFixer] " + plr.getName() + " is suspected for trying to bypass the fly check");
					doNotFlagShort.put(plr.getUniqueId(), setbackLocation.toVector());
					try {
						AACAccessor.increaseAllViolationsAndNotify(plr.getUniqueId(), 1, HackType.NOFALL, "(Custom) (FlyBypassFixer) " + plr.getName() + " is suspected for trying to bypass the fly check");
					} catch (ReflectiveOperationException ex) {
						ex.printStackTrace();
					}
				}
			}
		} catch (IllegalStateException ex) {
			ex.printStackTrace();
		}
	}

	private boolean couldBeOnGroundSimple(Location loc) {
		Block block = loc.getBlock();
		if (!MaterialUtil.canNeverStandOn(block)) {
			return true;
		}
		Block blockDown = block.getRelative(BlockFace.DOWN);
		if (!MaterialUtil.canNeverStandOn(blockDown)) {
			return true;
		}
		List<BlockFace> faces = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_WEST);
		for (BlockFace face : faces) {
			if (!MaterialUtil.canNeverStandOn(block.getRelative(face))) {
				return true;
			}
			if (!MaterialUtil.canNeverStandOn(blockDown.getRelative(face))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		//This needs to be here due to a ProtocolLib issue when fake-recieving packets (it might be fixed in later versions but AAC is not compatible)
	}
}

package eu.epicpvp.kcore.AACHack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import eu.epicpvp.kcore.AACHack.util.MaterialUtil;
import eu.epicpvp.kcore.Util.UtilServer;
import me.konsolas.aac.api.AACAPI;
import me.konsolas.aac.api.AACAPIProvider;
import me.konsolas.aac.api.HackType;
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

class FlyBypassFixer extends PacketAdapter implements Listener {

	private Cache<UUID, Boolean> doNotFlag1s = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.SECONDS)
			.build();

	private Cache<UUID, Boolean> doNotFlag3s = CacheBuilder.newBuilder()
			.expireAfterWrite(3, TimeUnit.SECONDS)
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
		doNotFlag3s.put(event.getPlayer().getUniqueId(), Boolean.TRUE);
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		doNotFlag3s.put(event.getPlayer().getUniqueId(), Boolean.TRUE);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		doNotFlag3s.put(event.getEntity().getUniqueId(), Boolean.TRUE);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		doNotFlag3s.put(event.getPlayer().getUniqueId(), Boolean.TRUE);
	}

	@Override
	public void onPacketReceiving(PacketEvent event) {
		Player plr = event.getPlayer();
		if (plr.getGameMode() == GameMode.CREATIVE || plr.getGameMode() == GameMode.SPECTATOR || plr.getAllowFlight() || plr.getVehicle() != null) {
			return;
		}
		AACAPI api = AACAPIProvider.getAPI();
		if (!api.isEnabled(HackType.NOFALL)) {
			return;
		}
		final PacketContainer packet = event.getPacket();
		Location location;
		if (event.getPacketType() != PacketType.Play.Client.POSITION_LOOK && event.getPacketType() != PacketType.Play.Client.POSITION) {
			location = plr.getLocation();
		} else {
			location = new Location(plr.getWorld(), packet.getDoubles().read(0), packet.getDoubles().read(1), packet.getDoubles().read(2));
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
				boolean doNotFlag1sPresent = doNotFlag1s.getIfPresent(plr.getUniqueId()) != null;
				boolean doNotFlag3sPresent = doNotFlag3s.getIfPresent(plr.getUniqueId()) != null;
				System.out.println("[FlyBypassFixer] " + plr.getName() + " is suspected for trying to bypass the fly check; simple onground:" + couldBeOnGroundSimple + ", noFlag1s: " + doNotFlag1s + ", noFlag3s: " + doNotFlag3s);
				if (!couldBeOnGroundSimple) {
					if (doNotFlag1sPresent) {
						return;
					}
					if (doNotFlag3sPresent) {
						return;
					}
					doNotFlag1s.put(plr.getUniqueId(), Boolean.TRUE);
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

package eu.epicpvp.kcore.AACHack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import dev.wolveringer.client.debug.Debugger;
import eu.epicpvp.kcore.kCore;
import me.konsolas.aac.AAC;
import me.konsolas.aac.api.AACAPI;
import me.konsolas.aac.api.AACAPIProvider;
import me.konsolas.aac.api.HackType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class FlyBypassFixer extends PacketAdapter {

	public FlyBypassFixer() {
		super(new AdapterParameteters()
				.plugin(kCore.getInstance())
				.clientSide()
				.listenerPriority(ListenerPriority.HIGHEST)
				.types(PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.FLYING));
	}

	@Override
	public void onPacketReceiving(PacketEvent event) {
		Player plr = event.getPlayer();
		if (!event.isFiltered() || plr.getGameMode() == GameMode.CREATIVE || plr.getGameMode() == GameMode.SPECTATOR || plr.getAllowFlight() || plr.getVehicle() != null) {
			return;
		}
		AACAPI api = AACAPIProvider.getAPI();
		if (!api.isEnabled(HackType.NOFALL)) {
			return;
		}
		if (api.getViolationLevel(plr, HackType.NOFALL) < 1) {
			return;
		}
		final PacketContainer packet = event.getPacket();
		Location location;
		if (event.getPacketType() != PacketType.Play.Client.POSITION_LOOK && event.getPacketType() != PacketType.Play.Client.POSITION) {
			location = plr.getLocation();
		} else {
			location = new Location(plr.getWorld(), packet.getDoubles().read(0), packet.getDoubles().read(1), packet.getDoubles().read(2));
		}

		Boolean sentOnGround = packet.getBooleans().read(0);
		if (!sentOnGround) {
			return;
		}
		boolean onGround = false;
		try {
			onGround = AACAccessor.isOnGround(location);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return;
		}
		if (!onGround) {
			packet.getBooleans().write(0, false);
			try {
				AACAccessor.increaseAllViolationsAndNotify(plr.getUniqueId(), 1, HackType.NOFALL, "(Custom) (FlyBypassFixer) " + plr.getName() + " is suspected for trying to bypass the fly check");
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		//This needs to be here due to a ProtocolLib issue when fake-recieving packets (its fixed but AAC is not compatible with those newer versions)
	}
}

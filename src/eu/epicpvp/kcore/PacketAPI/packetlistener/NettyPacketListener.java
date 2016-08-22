package eu.epicpvp.kcore.PacketAPI.packetlistener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.PacketAPI.UtilPacket;
import eu.epicpvp.kcore.PacketAPI.packetlistener.event.PacketListenerReceiveEvent;
import eu.epicpvp.kcore.PacketAPI.packetlistener.event.PacketListenerSendEvent;
import eu.epicpvp.kcore.PacketAPI.packetlistener.handler.PacketHandler;
import eu.epicpvp.kcore.PacketAPI.packetlistener.handler.ReceivedPacket;
import eu.epicpvp.kcore.PacketAPI.packetlistener.handler.SentPacket;
import lombok.Getter;

public class NettyPacketListener extends kListener implements IPacketEventHandler {

	@Getter
	private Plugin instance;
	private Injector injector;

	public NettyPacketListener(Plugin instance) {
		super(instance, "kPacketListener");
		this.instance = instance;
		this.injector = new Injector();
		boolean injected = injector.inject();

		if (injected) {
			injector.addServerConnectionChannel();
		} else {
			logMessage("Es ist ein Fehler passiert beim Injecten des custum Channel handler");
		}

		for (Player p : Bukkit.getOnlinePlayers()) {
			injector.addChannel(p);
		}
	}

	public void Disable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			injector.removeChannel(p);
		}

		while (!PacketHandler.getHandlers().isEmpty()) {
			PacketHandler.removeHandler(PacketHandler.getHandlers().get(0));
		}
	}

	/**
	 * @see PacketHandler#addHandler(PacketHandler)
	 */
	public boolean addPacketHandler(PacketHandler handler) {
		return PacketHandler.addHandler(handler);
	}

	/**
	 * @see PacketHandler#removeHandler(PacketHandler)
	 */
	public boolean removePacketHandler(PacketHandler handler) {
		return PacketHandler.removeHandler(handler);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		injector.addChannel(e.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		injector.removeChannel(e.getPlayer());
	}

	public Object onPacketReceive(Player p, Object packet, Cancellable cancellable) {
		if (!packet.getClass().getName().startsWith("net.minecraft.server."))
			return packet;

		PacketListenerReceiveEvent event = new PacketListenerReceiveEvent(packet, cancellable, p);
		UtilPacket.callEvent(event);
		ReceivedPacket pckt = new ReceivedPacket(packet, cancellable, p);
		PacketHandler.notifyHandlers(pckt);
		return pckt.getPacket();
	}

	public Object onPacketSend(Player p, Object packet, Cancellable cancellable) {
		if (!packet.getClass().getName().startsWith("net.minecraft.server."))
			return packet;
		PacketListenerSendEvent event = new PacketListenerSendEvent(packet, cancellable, p);
		UtilPacket.callEvent(event);

		SentPacket pckt = new SentPacket(packet, cancellable, p);
		PacketHandler.notifyHandlers(pckt);
		return pckt.getPacket();
	}

}

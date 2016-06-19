package eu.epicpvp.kcore.PacketAPI.packetlistener.handler;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.PacketAPI.packetlistener.Cancellable;

/**
 * Wrapper class or received packets
 *
 * @see Packet
 * @see SentPacket
 */
public class ReceivedPacket extends Packet {

	public ReceivedPacket(Object packet, Cancellable cancel, Player player) {
		super(packet, cancel, player);
	}

}

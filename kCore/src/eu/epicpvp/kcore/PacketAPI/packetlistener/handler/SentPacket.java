package eu.epicpvp.kcore.PacketAPI.packetlistener.handler;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.PacketAPI.packetlistener.Cancellable;

/**
 * Wrapper class for sent packets
 *
 * @see Packet
 * @see ReceivedPacket
 */
public class SentPacket extends Packet {

	public SentPacket(Object packet, Cancellable cancel, Player player) {
		super(packet, cancel, player);
	}

}

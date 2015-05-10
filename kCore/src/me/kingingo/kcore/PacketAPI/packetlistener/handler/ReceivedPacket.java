package me.kingingo.kcore.PacketAPI.packetlistener.handler;

import me.kingingo.kcore.PacketAPI.packetlistener.Cancellable;

import org.bukkit.entity.Player;

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

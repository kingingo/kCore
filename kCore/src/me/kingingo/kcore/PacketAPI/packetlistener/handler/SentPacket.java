package me.kingingo.kcore.PacketAPI.packetlistener.handler;

import me.kingingo.kcore.PacketAPI.packetlistener.Cancellable;

import org.bukkit.entity.Player;

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

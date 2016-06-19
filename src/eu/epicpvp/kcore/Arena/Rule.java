package eu.epicpvp.kcore.Arena;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Packets.PacketArenaStatus;

public interface Rule {
	public boolean onRule(Player owner, Player player,ArenaType type, PacketArenaStatus status,Object object);
}
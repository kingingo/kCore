package me.kingingo.kcore.Arena;

import me.kingingo.kcore.Packet.Packets.ARENA_STATUS;

import org.bukkit.entity.Player;

public interface Rule {
	public boolean onRule(Player owner, Player player,ArenaType type, ARENA_STATUS status,Object object);
}
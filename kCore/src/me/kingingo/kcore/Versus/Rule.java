package me.kingingo.kcore.Versus;

import me.kingingo.kcore.Packet.Packets.ARENA_STATUS;

import org.bukkit.entity.Player;

public interface Rule {
	public boolean onRule(Player owner, Player player, ARENA_STATUS status,Object object);
}
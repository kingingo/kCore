package me.kingingo.kcore.Packet.Events;

import lombok.Getter;
import me.kingingo.kcore.Packet.Packet;
import me.kingingo.kcore.Packet.PacketManager;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketReceiveEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Packet packet;
	@Getter
	private PacketManager packetManager;
	
	public PacketReceiveEvent(Packet packet,PacketManager packetManager){
		this.packet=packet;
		this.packetManager=packetManager;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}
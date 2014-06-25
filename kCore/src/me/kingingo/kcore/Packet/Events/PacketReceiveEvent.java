package me.kingingo.kcore.Packet.Events;

import me.kingingo.kcore.Packet.Packet;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketReceiveEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	private Packet packet;
	
	public PacketReceiveEvent(Packet packet){
		this.packet=packet;
	}
	
	public Packet getPacket(){
		return this.packet;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}
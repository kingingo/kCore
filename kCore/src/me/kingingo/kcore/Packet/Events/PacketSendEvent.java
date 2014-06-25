package me.kingingo.kcore.Packet.Events;

import me.kingingo.kcore.Packet.Packet;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketSendEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	private Packet packet;
	private String toServer;
	
	public PacketSendEvent(Packet packet,String toServer){
		this.packet=packet;
		this.toServer=toServer;
	}
	
	public String toServer(){
		return this.toServer;
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
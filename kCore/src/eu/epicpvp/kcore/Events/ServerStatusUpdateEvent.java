package eu.epicpvp.kcore.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dev.wolveringer.dataserver.protocoll.packets.PacketInServerStatus;
import lombok.Getter;

public class ServerStatusUpdateEvent  extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private PacketInServerStatus packet;
	
	public ServerStatusUpdateEvent(PacketInServerStatus packet){
		this.packet=packet;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

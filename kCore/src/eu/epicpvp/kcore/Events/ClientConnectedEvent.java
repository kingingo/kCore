package eu.epicpvp.kcore.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dev.wolveringer.client.ClientWrapper;
import lombok.Getter;

public class ClientConnectedEvent  extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private ClientWrapper client;
	
	public ClientConnectedEvent(ClientWrapper client){
		this.client=client;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

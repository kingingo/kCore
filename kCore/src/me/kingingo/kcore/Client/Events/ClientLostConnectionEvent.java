package me.kingingo.kcore.Client.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClientLostConnectionEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }
	
}

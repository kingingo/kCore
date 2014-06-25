package me.kingingo.kcore.MySQL.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MySQLDisconnectEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

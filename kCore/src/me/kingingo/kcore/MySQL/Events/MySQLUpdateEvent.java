package me.kingingo.kcore.MySQL.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MySQLUpdateEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	private String q;
	
	public MySQLUpdateEvent(String q){
		this.q=q;
	}
	
	public String getUpdate(){
		return this.q;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

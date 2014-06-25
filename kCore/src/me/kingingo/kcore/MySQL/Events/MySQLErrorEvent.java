package me.kingingo.kcore.MySQL.Events;

import me.kingingo.kcore.MySQL.MySQLErr;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MySQLErrorEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	private MySQLErr err;
	private Exception error;
	
	public MySQLErrorEvent(MySQLErr err,Exception error){
		this.err=err;
		this.error=error;
	}
	
	public Exception getException(){
		return this.error;
	}
	
	public MySQLErr getError(){
		return this.err;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

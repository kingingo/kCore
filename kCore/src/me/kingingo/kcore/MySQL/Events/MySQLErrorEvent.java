package me.kingingo.kcore.MySQL.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MySQLErrorEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	@Setter
	private MySQLErr error;
	@Getter
	@Setter
	private Exception exception;
	@Getter
	private MySQL mysql;
	
	public MySQLErrorEvent(MySQLErr error,Exception exception,MySQL mysql){
		this.error=error;
		this.exception=exception;
		this.mysql=mysql;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

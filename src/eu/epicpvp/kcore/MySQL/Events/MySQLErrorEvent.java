package eu.epicpvp.kcore.MySQL.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.MySQL.MySQLErr;
import lombok.Getter;
import lombok.Setter;

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

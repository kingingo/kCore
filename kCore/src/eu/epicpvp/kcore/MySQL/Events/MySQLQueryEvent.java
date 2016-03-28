package eu.epicpvp.kcore.MySQL.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import eu.epicpvp.kcore.MySQL.MySQL;

public class MySQLQueryEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	@Setter
	private String query;
	@Getter
	private MySQL mysql;
	
	public MySQLQueryEvent(String query,MySQL mysql){
		this.query=query;
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

package eu.epicpvp.kcore.MySQL.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.MySQL.MySQL;
import lombok.Getter;

public class MySQLDisconnectEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private MySQL mysql;
	
	public MySQLDisconnectEvent(MySQL mysql){
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

package eu.epicpvp.kcore.MySQL.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.MySQL.MySQL;
import lombok.Getter;
import lombok.Setter;

public class MySQLUpdateEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	@Setter
	private String updater;
	@Getter
	private MySQL mysql;
	
	public MySQLUpdateEvent(String updater,MySQL mysql){
		this.updater=updater;
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

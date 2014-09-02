package me.kingingo.kcore.MySQL.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.MySQL.MySQL;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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

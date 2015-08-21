package me.kingingo.kcore.MySQL.Events;

import lombok.Getter;
import me.kingingo.kcore.MySQL.MySQL;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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

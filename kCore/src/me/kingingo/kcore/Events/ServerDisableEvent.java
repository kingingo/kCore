package me.kingingo.kcore.Events;

import lombok.Getter;
import me.kingingo.kcore.Client.Client;
import me.kingingo.kcore.MySQL.MySQL;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerDisableEvent  extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private MySQL mysql;
	@Getter
	private Client client;
	
	public ServerDisableEvent(MySQL mysql,Client client){
		this.client=client;
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

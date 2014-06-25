package me.kingingo.kcore.Client.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClientReceiveMessageEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	private String msg;
	
	public ClientReceiveMessageEvent(String msg){
		this.msg=msg;
	}
	
	public String getMessage(){
		return this.msg;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}
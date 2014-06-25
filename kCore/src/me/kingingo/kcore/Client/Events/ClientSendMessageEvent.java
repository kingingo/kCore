package me.kingingo.kcore.Client.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClientSendMessageEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	private String msg;
	
	public ClientSendMessageEvent(String msg){
		this.msg=msg;
	}
	
	public String getMessage(){
		return this.msg;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}

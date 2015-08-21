package me.kingingo.kcore.Gilden.Events;

import lombok.Getter;
import me.kingingo.kcore.Gilden.GildenManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GildeLoadEvent extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private GildenManager manager;
	@Getter
	private String gilde;
	
	public GildeLoadEvent(String gilde,GildenManager manager){
		this.manager=manager;
		this.gilde=gilde;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

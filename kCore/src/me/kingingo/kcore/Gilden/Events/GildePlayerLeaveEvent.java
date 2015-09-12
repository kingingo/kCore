package me.kingingo.kcore.Gilden.Events;

import lombok.Getter;
import me.kingingo.kcore.Gilden.GildenManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GildePlayerLeaveEvent extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private GildenManager manager;
	@Getter
	private String gilde;
	@Getter
	private Player player;
	
	public GildePlayerLeaveEvent(String gilde,Player player,GildenManager manager){
		this.manager=manager;
		this.gilde=gilde;
		this.player=player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

package me.kingingo.kcore.Gilden.Events;

import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Gilden.GildenManager;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GildePlayerLeaveEvent extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private GildenManager manager;
	@Getter
	private String gilde;
	@Getter
	private UUID uuid;
	@Getter
	private String player;
	
	public GildePlayerLeaveEvent(String gilde,String player,UUID uuid,GildenManager manager){
		this.manager=manager;
		this.gilde=gilde;
		this.uuid=uuid;
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

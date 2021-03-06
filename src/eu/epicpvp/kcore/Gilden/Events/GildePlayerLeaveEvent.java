package eu.epicpvp.kcore.Gilden.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Gilden.GildenManager;
import lombok.Getter;

public class GildePlayerLeaveEvent extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private GildenManager manager;
	@Getter
	private String gilde;
	@Getter
	private int playerId;
	@Getter
	private String player;
	
	public GildePlayerLeaveEvent(String gilde,String player,int playerId,GildenManager manager){
		this.manager=manager;
		this.gilde=gilde;
		this.playerId=playerId;
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

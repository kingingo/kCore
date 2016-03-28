package eu.epicpvp.kcore.Gilden.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Gilden.GildenManager;
import lombok.Getter;

public class GildePlayerJoinEvent extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private GildenManager manager;
	@Getter
	private String gilde;
	@Getter
	private Player player;
	
	public GildePlayerJoinEvent(String gilde,Player player,GildenManager manager){
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

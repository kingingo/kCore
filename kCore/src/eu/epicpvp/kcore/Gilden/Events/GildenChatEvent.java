package eu.epicpvp.kcore.Gilden.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Gilden.GildenManager;
import lombok.Getter;
import lombok.Setter;

public class GildenChatEvent extends Event implements Cancellable{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private GildenManager manager;
	@Getter
	@Setter
	private String gilde;
	@Getter
	@Setter
	private Player player;
	@Getter
	@Setter
	private String message;
	@Getter
	@Setter
	private boolean cancelled=false;
	
	public GildenChatEvent(Player player,String message,String gilde,GildenManager manager){
		this.manager=manager;
		this.gilde=gilde;
		this.player=player;
		this.message=message;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

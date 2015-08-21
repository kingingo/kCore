package me.kingingo.kcore.Gilden.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Gilden.GildenManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GildenPlayerTeleportEvent extends Event implements Cancellable{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private GildenManager manager;
	@Getter
	private Player player;
	@Getter
	@Setter
	private boolean cancelled=false;
	@Getter
	@Setter
	private String reason=null;
	
	public GildenPlayerTeleportEvent(Player player,GildenManager manager){
		this.player=player;
		this.manager=manager;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

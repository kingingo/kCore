package me.kingingo.kcore.TeleportManager.Events;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerTeleportEvent extends Event implements Cancellable{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	private Location to;
	@Getter
	@Setter
	private boolean cancelled=false;
	@Getter
	@Setter
	private String reason=null;
	
	public PlayerTeleportEvent(Player player, Location to){
		this.player=player;
		this.to=to;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

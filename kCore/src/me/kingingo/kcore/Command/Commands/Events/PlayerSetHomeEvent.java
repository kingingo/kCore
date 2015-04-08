package me.kingingo.kcore.Command.Commands.Events;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSetHomeEvent  extends Event implements Cancellable{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	@Setter
	private boolean cancelled=false;
	@Getter
	@Setter
	private String reason=null;
	@Getter
	private Location home;
	@Getter
	private String name;
	
	public PlayerSetHomeEvent(Player player,Location home,String name){
		this.player=player;
		this.home=home;
		this.name=name;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

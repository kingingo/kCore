package me.kingingo.kcore.Command.Commands.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerHomeEvent  extends Event implements Cancellable{
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
	private kConfig config;
	
	public PlayerHomeEvent(Player player,Location home,kConfig config){
		this.player=player;
		this.home=home;
		this.config=config;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

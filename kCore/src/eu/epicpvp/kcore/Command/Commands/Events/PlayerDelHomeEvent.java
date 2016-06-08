package eu.epicpvp.kcore.Command.Commands.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class PlayerDelHomeEvent  extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	private kConfig config;
	@Getter
	private String name;
	@Getter
	private Location home;
	
	public PlayerDelHomeEvent(Player player,Location home,String name,kConfig config){
		this.player=player;
		this.name=name;
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

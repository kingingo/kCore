package eu.epicpvp.kcore.Command.Commands.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

public class AddKitEvent  extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	@Setter
	private String kit;
	@Getter
	@Setter
	private long delay;
	
	public AddKitEvent(Player player,String kit,long l){
		this.player=player;
		this.kit=kit;
		this.delay=l;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

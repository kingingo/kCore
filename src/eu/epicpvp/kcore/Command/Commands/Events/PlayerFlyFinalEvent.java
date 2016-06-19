package eu.epicpvp.kcore.Command.Commands.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

public class PlayerFlyFinalEvent  extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	@Setter
	private boolean allowFlight;
	@Getter
	@Setter
	private boolean toggle;
	
	public PlayerFlyFinalEvent(Player player,boolean toggle){
		this.player=player;
		this.toggle=toggle;
		this.allowFlight=player.getAllowFlight();
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

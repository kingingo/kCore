package eu.epicpvp.kcore.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dev.wolveringer.dataclient.gamestats.GameType;
import lombok.Getter;

public class ServerChangeGameTypeEvent  extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private GameType type;
	
	public ServerChangeGameTypeEvent(GameType type){
		this.type=type;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

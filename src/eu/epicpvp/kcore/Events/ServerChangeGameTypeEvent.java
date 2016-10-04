package eu.epicpvp.kcore.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameType;
import lombok.Getter;

public class ServerChangeGameTypeEvent  extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private GameType type;
	@Getter
	private String subType;

	public ServerChangeGameTypeEvent(GameType type,String subType){
		this.type=type;
		this.subType=(subType == null ? "none" : subType);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }

}

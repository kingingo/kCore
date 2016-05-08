package eu.epicpvp.kcore.Teams.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Teams.Team;
import lombok.Getter;

public class TeamLoadedEvent extends Event{

	@Getter
	private Team team;
	
	public TeamLoadedEvent(Team team){
		this.team=team;
	}

	//static things for bukkit events
	@Getter
	private static final HandlerList handlerList = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}

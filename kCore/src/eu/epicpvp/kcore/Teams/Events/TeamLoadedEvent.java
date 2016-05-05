package eu.epicpvp.kcore.Teams.Events;

import eu.epicpvp.kcore.Teams.Team;
import eu.epicpvp.kcore.Teams.TeamManager;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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

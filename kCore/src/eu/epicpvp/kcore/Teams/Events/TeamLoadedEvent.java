package eu.epicpvp.kcore.Teams.Events;

import eu.epicpvp.kcore.Teams.Team;
import eu.epicpvp.kcore.Teams.TeamManager;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamLoadedEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	@Getter
	private Team team;
	
	public TeamLoadedEvent(Team team){
		this.team=team;
	}
	
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}

package eu.epicpvp.kcore.Teams.Events;

import eu.epicpvp.kcore.Teams.Team;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamPlayerQuitEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	@Getter
	private Team team;
	@Getter
	private Player player;
	
	public TeamPlayerQuitEvent(Team team,Player player){
		this.team=team;
		this.player=player;
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

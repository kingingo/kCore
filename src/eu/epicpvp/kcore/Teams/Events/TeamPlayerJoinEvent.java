package eu.epicpvp.kcore.Teams.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Teams.Team;
import lombok.Getter;

public class TeamPlayerJoinEvent extends Event{

	@Getter
	private Team team;
	@Getter
	private Player player;
	
	public TeamPlayerJoinEvent(Team team,Player player){
		this.team=team;
		this.player=player;
	}

	//static things for bukkit events
	@Getter
	private static final HandlerList handlerList = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}

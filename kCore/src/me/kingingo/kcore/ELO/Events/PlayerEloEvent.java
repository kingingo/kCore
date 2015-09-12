package me.kingingo.kcore.ELO.Events;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerEloEvent  extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	@Setter
	private double elo_from;
	@Getter
	@Setter
	private double elo_to;
	
	public PlayerEloEvent(Player player,double elo_from,double elo_to){
		this.player=player;
		this.elo_from=elo_from;
		this.elo_to=elo_to;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

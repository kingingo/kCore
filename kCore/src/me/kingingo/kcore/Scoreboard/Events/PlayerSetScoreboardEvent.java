package me.kingingo.kcore.Scoreboard.Events;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSetScoreboardEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	
	public PlayerSetScoreboardEvent(Player player){
		this.player=player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}
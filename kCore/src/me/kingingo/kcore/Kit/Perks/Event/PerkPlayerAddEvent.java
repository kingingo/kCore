package me.kingingo.kcore.Kit.Perks.Event;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PerkPlayerAddEvent extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	
	public PerkPlayerAddEvent(Player player){
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

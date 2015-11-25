package me.kingingo.kcore.Kit.Perks.Event;

import java.util.ArrayList;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PerkStartEvent extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private ArrayList<Player> players;
	
	public PerkStartEvent(ArrayList<Player> players){
		this.players=players;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

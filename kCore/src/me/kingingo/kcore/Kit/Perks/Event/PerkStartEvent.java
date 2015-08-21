package me.kingingo.kcore.Kit.Perks.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PerkStartEvent extends Event {
	private static HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

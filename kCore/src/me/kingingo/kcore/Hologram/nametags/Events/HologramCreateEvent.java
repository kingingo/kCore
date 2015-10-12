package me.kingingo.kcore.Hologram.nametags.Events;

import lombok.Getter;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HologramCreateEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private NameTagMessage nts;
	
	public HologramCreateEvent(NameTagMessage nts){
		this.nts=nts;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

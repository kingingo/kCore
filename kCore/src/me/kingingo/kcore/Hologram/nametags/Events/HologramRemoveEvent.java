package me.kingingo.kcore.Hologram.nametags.Events;

import lombok.Getter;
import me.kingingo.kcore.Hologram.nametags.NameTagSpawner;
import me.kingingo.kcore.ItemFake.ItemFake;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HologramRemoveEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private NameTagSpawner nts;
	
	public HologramRemoveEvent(NameTagSpawner nts){
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
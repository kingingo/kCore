package me.kingingo.kcore.ItemFake.Events;

import lombok.Getter;
import me.kingingo.kcore.ItemFake.ItemFake;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ItemFakeCreateEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private ItemFake itemfake;
	
	public ItemFakeCreateEvent(ItemFake itemfake){
		this.itemfake=itemfake;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

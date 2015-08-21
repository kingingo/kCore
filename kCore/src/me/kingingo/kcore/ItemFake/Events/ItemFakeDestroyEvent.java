package me.kingingo.kcore.ItemFake.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.ItemFake.ItemFake;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ItemFakeDestroyEvent extends Event implements Cancellable{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private ItemFake itemfake;
	@Getter
	@Setter
	private boolean cancelled=false;
	
	public ItemFakeDestroyEvent(ItemFake itemfake){
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

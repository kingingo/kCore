package eu.epicpvp.kcore.ItemFake.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.ItemFake.ItemFake;
import lombok.Getter;

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

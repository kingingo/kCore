package me.kingingo.kcore.ItemFake.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.ItemFake.ItemFake;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ItemFakePickupEvent extends Event implements Cancellable{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	private Item item;
	@Getter
	private ItemFake itemfake;
	@Getter
	@Setter
	private boolean cancelled=false;
	
	public ItemFakePickupEvent(Item item,Player player,ItemFake itemfake){
		this.item=item;
		this.itemfake=itemfake;
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

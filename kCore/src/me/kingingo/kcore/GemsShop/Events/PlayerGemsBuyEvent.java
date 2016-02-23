package me.kingingo.kcore.GemsShop.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

public class PlayerGemsBuyEvent  extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	@Setter
	private ItemStack item;
	@Getter
	@Setter
	private int price;
	
	public PlayerGemsBuyEvent(Player player,ItemStack item,int price){
		this.player=player;
		this.item=item;
		this.price=price;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}

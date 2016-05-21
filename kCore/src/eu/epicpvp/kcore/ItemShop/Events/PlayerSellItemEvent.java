package eu.epicpvp.kcore.ItemShop.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.ItemShop.ItemShop;
import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Kit.PerkData;
import lombok.Getter;

public class PlayerSellItemEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private ItemStack item;
	@Getter
	private int price;
	@Getter
	private Player player;
	
	public PlayerSellItemEvent(Player player,ItemStack item,int price){
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

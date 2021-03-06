package eu.epicpvp.kcore.SignShop.Events;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

import eu.epicpvp.kcore.SignShop.SignShopAction;
import lombok.Getter;
import lombok.Setter;

public class SignShopUseEvent extends Event implements Cancellable{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Sign sign;
	@Getter
	private Player player;
	@Getter
	private PlayerInteractEvent interactEvent;
	@Getter
	@Setter
	private boolean cancelled;
	@Getter
	private SignShopAction action;
	
	public SignShopUseEvent(Sign sign,SignShopAction action,Player player,PlayerInteractEvent interactEvent){
		this.sign=sign;
		this.interactEvent=interactEvent;
		this.player=player;
		this.action=action;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}
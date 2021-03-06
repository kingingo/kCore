package eu.epicpvp.kcore.UserStores.Events;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

public class PlayerUserShopBuyEvent  extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	@Setter
	private Sign sign;
	
	public PlayerUserShopBuyEvent(Player player,Sign sign){
		this.player=player;
		this.sign=sign;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}
